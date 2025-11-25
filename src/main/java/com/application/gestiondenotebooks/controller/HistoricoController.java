package com.application.gestiondenotebooks.controller;

import com.application.gestiondenotebooks.enums.EstadoPrestamo;
import com.application.gestiondenotebooks.model.Prestamo;
import com.application.gestiondenotebooks.repository.PrestamoRepository;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.List;

@Component
public class HistoricoController implements Initializable {

    @Autowired
    private PrestamoRepository prestamoRepository;

    @Autowired
    private ApplicationContext context;

    @FXML
    private ListView<Prestamo> listHistorial;

    @FXML
    private Label lblNroReferencia;
    @FXML
    private Label lblDocente;
    @FXML
    private Label lblMateria;
    @FXML
    private Label lblHorario;
    @FXML
    private Label lblAula;
    @FXML
    private Label lblEquipos;
    @FXML
    private Label lblFechaFin;

    private Prestamo prestamoSeleccionado;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarHistorial();

        listHistorial.getSelectionModel().selectedItemProperty().addListener((obs, oldV, nuevo) -> {
            if (nuevo != null) {
                prestamoSeleccionado = nuevo;
                mostrarDetalle(nuevo);
            }
        });
    }

    private void cargarHistorial() {
        List<Prestamo> cerrados = prestamoRepository.findByEstadoOrderByFechaFinDesc(EstadoPrestamo.CERRADO);
        listHistorial.setItems(FXCollections.observableArrayList(cerrados));
    }

    private void mostrarDetalle(Prestamo p) {
        lblNroReferencia.setText(String.valueOf(p.getNroReferencia()));
        lblDocente.setText(p.getDocente().getNombre());
        lblMateria.setText(p.getMateria().getNombre());
        lblHorario.setText(p.getTurno().name());
        lblAula.setText(p.getAula().getCodigo_aula());
        lblEquipos.setText(p.getResumenEquipos());
        lblFechaFin.setText(p.getFechaFin() != null ? p.getFechaFin().toString() : "—");
    }

    public void irAVentanaPrincipal(javafx.event.ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com.application.gestiondenotebooks/VentanaPrincipal.fxml"));
        loader.setControllerFactory(context::getBean);  // <-- para que Spring cree el controller
        Parent root = loader.load();
        // obtener el Stage actual desde el botón que disparó el evento
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Gestión de Notebooks CAECE");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

}