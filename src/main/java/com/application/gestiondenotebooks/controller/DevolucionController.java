package com.application.gestiondenotebooks.controller;

import com.application.gestiondenotebooks.enums.EstadoDevolucion;
import com.application.gestiondenotebooks.enums.EstadoPrestamo;
import com.application.gestiondenotebooks.model.Equipo;
import com.application.gestiondenotebooks.model.Prestamo;
import com.application.gestiondenotebooks.model.PrestamoEquipo;
import com.application.gestiondenotebooks.repository.EquipoRepository;
import com.application.gestiondenotebooks.repository.PrestamoEquipoRepository;
import com.application.gestiondenotebooks.repository.PrestamoRepository;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
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
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

@Component
public class DevolucionController implements Initializable {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private PrestamoRepository prestamoRepository;

    @Autowired
    private EquipoRepository equipoRepo;


    @FXML
    private Label lblNroRef, lblDocente, lblMateria, lblProblemasQR, lblDeshabilitarManual;

    @FXML
    private ListView<PrestamoEquipo> listEquipos;

    @FXML
    private TextField txtScan;

    @FXML
    private Button btnVolver, btnFinalizarPrestamo, btnIngresoManual, btnDevolverManual, btnVolverQR, btnCancelarSeleccion;

    private Prestamo prestamo;

    private PrestamoEquipo equipoSeleccionado;

    @Autowired
    private PrestamoEquipoRepository prestamoEquipoRepo;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            txtScan.requestFocus();
            txtScan.positionCaret(txtScan.getText().length());
        });
        txtScan.setOnAction(e -> {
            String raw = txtScan.getText();
            String codigo = raw == null ? "" : raw.replace("\r", "").replace("\n", "").trim();

            if (!codigo.isEmpty()) {
                manejarCodigoEscaneado(codigo); // <-- TU lógica
            }
            txtScan.clear();
            txtScan.requestFocus(); // listo para el próximo
        });
        listEquipos.getSelectionModel().selectedItemProperty().addListener((obs, old, nuevo) -> {
            if (nuevo != null) {
                btnDevolverManual.setVisible(true);
                equipoSeleccionado = nuevo;
                btnCancelarSeleccion.setVisible(true);
                btnVolverQR.setVisible(false);
                lblDeshabilitarManual.setVisible(false);
                btnIngresoManual.setVisible(false);
                lblProblemasQR.setVisible(false);
            }
        });
    }

    @FXML
    private void manejarCodigoEscaneado(String codigo) {
        Optional<Equipo> equipoOpt=equipoRepo.findByCodigoQr(txtScan.getText());
        if (equipoOpt.isEmpty())
            mostrarWarn("Error en el ingreso","Error","No existe un equipo asociado al código qr: "+codigo);
        else{
            Equipo equipo = equipoOpt.get();
            boolean EnLista = listEquipos.getItems().stream()
                    .anyMatch(e -> e.getEquipo().getId().equals(equipo.getId()));
            if (EnLista) {
                PrestamoEquipo prestamoEquipo=prestamoEquipoRepo.findByEquipoAndPrestamo(equipo,prestamo).get();
                prestamoEquipo.setEstadoDevolucion(EstadoDevolucion.DEVUELTO);
                prestamoEquipoRepo.save(prestamoEquipo);
                listEquipos.getItems().removeIf(e ->
                        e.getEquipo().getTipo() == equipo.getTipo() && e.getEquipo().getNroEquipo() == equipo.getNroEquipo());
                actualizarLista();
            }
            else{
               mostrarWarn("Error en el ingreso","Error","El equipo no está asociado a este prestamo");
            }
        }
        System.out.println("Leido: "+codigo);
    }

    private void iniciarLista() {
        List<PrestamoEquipo> items = prestamo.getEquipos();
        listEquipos.setItems(FXCollections.observableArrayList(items));
    }


    public void setPrestamo(Prestamo prestamo) {
        this.prestamo = prestamo;
        cargarDatos();
        iniciarLista();
    }

    private void cargarDatos() {
        if (prestamo == null) return;
        lblNroRef.setText(String.valueOf(prestamo.getNroReferencia()));
        lblDocente.setText(prestamo.getDocente() != null ? prestamo.getDocente().getNombre() : "-");
        lblMateria.setText(prestamo.getMateria() != null ? prestamo.getMateria().getNombre() : "-");
    }

    @FXML
    private void devolverEquipoManual(){
        equipoSeleccionado.setEstadoDevolucion(EstadoDevolucion.DEVUELTO);
        prestamoEquipoRepo.save(equipoSeleccionado);
        listEquipos.getItems().remove(equipoSeleccionado);
        btnDevolverManual.setVisible(false);
        btnCancelarSeleccion.setVisible(false);
        btnVolverQR.setVisible(true);
        actualizarLista();
    }
    public void actualizarLista(){
        if (listEquipos.getItems().isEmpty()) {
            btnFinalizarPrestamo.setVisible(true);
            btnVolverQR.setVisible(false);
            lblDeshabilitarManual.setVisible(false);
            btnIngresoManual.setVisible(false);
            lblProblemasQR.setVisible(false);
        }
    }

    private void mostrarWarn(String titulo, String cabecera, String contenido) {
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setTitle(titulo);
        a.setHeaderText(cabecera);
        a.setContentText(contenido);
        a.showAndWait();
    }


    @FXML
    private void finalizarPrestamo(javafx.event.ActionEvent e) {
        prestamo.setEstado(EstadoPrestamo.CERRADO);
        prestamoRepository.save(prestamo);
        javafx.application.Platform.runLater(() -> irAPrestamosActivos(e));
    }

    @FXML
    private void cambiarAIngresoManual(ActionEvent actionEvent){
        lblProblemasQR.setVisible(false);
        btnIngresoManual.setVisible(false);
        btnVolverQR.setVisible(true);
        lblDeshabilitarManual.setVisible(true);
        btnDevolverManual.setDisable(false);
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
            mostrarWarn("Error", "No se pudo volver a la lista de préstamos activos.","Intentelo de nuevo");
        }
    }

    @FXML
    public void cambiarAIngresoQR(ActionEvent actionEvent) {
        btnDevolverManual.setDisable(true);
        lblProblemasQR.setVisible(true);
        btnIngresoManual.setVisible(true);
        btnVolverQR.setVisible(false);
        lblDeshabilitarManual.setVisible(false);
    }

    @FXML
    public void cancelarSeleccion(){
        listEquipos.getSelectionModel().clearSelection();
        btnDevolverManual.setVisible(false);
        if (btnDevolverManual.isDisable()) {
            btnIngresoManual.setVisible(true);
            lblProblemasQR.setVisible(true);
        } else {
            btnVolverQR.setVisible(true);
            lblDeshabilitarManual.setVisible(true);
        }
        btnCancelarSeleccion.setVisible(false);
    }
}

