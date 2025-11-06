package com.application.gestiondenotebooks.controller;

import com.application.gestiondenotebooks.model.Prestamo;
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
import java.util.ResourceBundle;

@Component
public class PrestamosActivosController implements Initializable {

    @Autowired
    private PrestamoRepository prestamoRepository;
    @Autowired
    private ApplicationContext context; // Spring

    @FXML
    private ListView<Prestamo> listPrestamos;

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
    private Label lblEquiposDetalle;

    @FXML
    private Button btnFinalizar;
    @FXML
    private Button btnCancelar;

    private Prestamo prestamoSeleccionado;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarPrestamos();

        listPrestamos.getSelectionModel().selectedItemProperty().addListener((obs, old, nuevo) -> {
            if (nuevo != null) {
                prestamoSeleccionado = nuevo;
                mostrarDetallePrestamo(nuevo);
                btnFinalizar.setVisible(true);
                btnCancelar.setVisible(true);
            }
        });

        btnFinalizar.setVisible(false);
        btnCancelar.setVisible(false);
    }

    private void cargarPrestamos() {
        //listPrestamos.setItems(FXCollections.observableArrayList(prestamoRepository.findByActivoTrue()));
    }

    private void mostrarDetallePrestamo(Prestamo p) {
        lblNroReferencia.setText(String.valueOf(p.getNroReferencia()));
        lblDocente.setText(p.getDocente().getNombre());
        lblMateria.setText(p.getMateria().getNombre());
        lblHorario.setText(p.getTurno().name());
        lblAula.setText(p.getAula().getCodigo_aula());
       // lblEquiposDetalle.setText(p.getResumenEquipos());
    }

    @FXML
    private void finalizarPrestamo() { //Cambiarlo para gestionar devolucion
        if (prestamoSeleccionado != null) {
            //prestamoSeleccionado.setActivo(false);
            prestamoRepository.save(prestamoSeleccionado);
            cargarPrestamos();
            limpiarDetalle();
            btnFinalizar.setVisible(false);
            btnCancelar.setVisible(false);
        }
    }

    @FXML
    private void cancelarSeleccion() {
        limpiarDetalle();
        btnFinalizar.setVisible(false);
        btnCancelar.setVisible(false);
    }

    private void limpiarDetalle() {
        lblNroReferencia.setText("");
        lblDocente.setText("");
        lblMateria.setText("");
        lblHorario.setText("");
        lblAula.setText("");
        lblEquiposDetalle.setText("");
        listPrestamos.getSelectionModel().clearSelection();
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
