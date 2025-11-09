package com.application.gestiondenotebooks.controller;

import com.application.gestiondenotebooks.enums.EstadoDevolucion;
import com.application.gestiondenotebooks.enums.EstadoPrestamo;
import com.application.gestiondenotebooks.model.Prestamo;
import com.application.gestiondenotebooks.model.PrestamoEquipo;
import com.application.gestiondenotebooks.repository.PrestamoEquipoRepository;
import com.application.gestiondenotebooks.repository.PrestamoRepository;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@Component
public class DevolucionController implements Initializable {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private PrestamoRepository prestamoRepository;

    @Autowired
    private PrestamoEquipoRepository prestamoEquipoRepository;

    @FXML
    private Label lblRef, lblDocente, lblMateria;

    @FXML
    private ListView<String> listEquipos;

    @FXML
    private TextField txtCodigoEquipo;

    @FXML
    private Button btnRegistrarDevolucion, btnVolver, btnFinalizarPrestamo;

    private Prestamo prestamo;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnFinalizarPrestamo.setVisible(false);
    }

    public void setPrestamo(Prestamo prestamo) {
        this.prestamo = prestamo;
        cargarDatos();
    }

    private void cargarDatos() {
        if (prestamo == null) return;

        lblRef.setText(String.valueOf(prestamo.getNroReferencia()));
        lblDocente.setText(prestamo.getDocente() != null ? prestamo.getDocente().getNombre() : "-");
        lblMateria.setText(prestamo.getMateria() != null ? prestamo.getMateria().getNombre() : "-");

        actualizarLista();
    }

    /**
     * Muestra los equipos asociados al préstamo, identificándolos por su equipo_id
     */
    private void actualizarLista() {
        List<String> items = prestamo.getEquipos().stream()
                .map(eq -> "ID " + eq.getEquipo().getId() + " - " +
                        eq.getEquipo().getTipo() + " (" + eq.getEstadoDevolucion() + ")")
                .collect(Collectors.toList());

        listEquipos.setItems(FXCollections.observableArrayList(items));
    }

    /**
     * Busca el equipo según su ID (equipo_id) en lugar de nro_equipo
     */
    @FXML
    private void registrarDevolucion() {
        String codigo = txtCodigoEquipo.getText().trim();
        if (codigo.isEmpty()) return;

        PrestamoEquipo encontrado = prestamo.getEquipos().stream()
                .filter(eq -> String.valueOf(eq.getEquipo().getId()).equals(codigo))
                .findFirst()
                .orElse(null);

        if (encontrado != null && encontrado.getEstadoDevolucion() != EstadoDevolucion.DEVUELTO) {
            encontrado.setEstadoDevolucion(EstadoDevolucion.DEVUELTO);
            prestamoEquipoRepository.save(encontrado);
            txtCodigoEquipo.clear();
            actualizarLista();

            boolean todosDevueltos = prestamo.getEquipos().stream()
                    .allMatch(eq -> eq.getEstadoDevolucion() == EstadoDevolucion.DEVUELTO);
            btnFinalizarPrestamo.setVisible(todosDevueltos);
        } else {
            mostrarAlerta("Código no válido", "No se encontró un equipo con ese ID asociado a este préstamo.");
            txtCodigoEquipo.clear();
            txtCodigoEquipo.requestFocus();
        }

    }

    private void mostrarAlerta(String titulo, String mensaje) {
        javafx.application.Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(titulo);
            alert.setHeaderText(null);
            alert.setContentText(mensaje);
            alert.showAndWait();
        });
    }


    @FXML
    private void finalizarPrestamo(javafx.event.ActionEvent e) {
        prestamo.setEstado(EstadoPrestamo.CERRADO);
        prestamoRepository.save(prestamo);

        javafx.application.Platform.runLater(() -> irAPrestamosActivos(e));
    }



    @FXML
    public void irAPrestamosActivos(javafx.event.ActionEvent e) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com.application.gestiondenotebooks/PrestamosActivos.fxml"));
            loader.setControllerFactory(context::getBean);
            Parent root = loader.load();

            javafx.application.Platform.runLater(() -> {
                Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Préstamos Activos");
                stage.centerOnScreen();
                stage.show();
            });

        } catch (IOException ex) {
            ex.printStackTrace();
            mostrarAlerta("Error", "No se pudo volver a la lista de préstamos activos.");
        }
    }

}

