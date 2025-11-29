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
import java.time.LocalDateTime;
import java.util.List;
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

    @Autowired
    private PrestamoEquipoRepository prestamoEquipoRepo;

    @FXML
    private Label lblProblemasQR, lblDeshabilitarManual, labelNroRef,labelNroRefPrest;

    @FXML
    private ListView<PrestamoEquipo> listEquipos;

    @FXML
    private TextField txtScan;

    @FXML
    private Button btnVolver, btnFinalizarPrestamo, btnIngresoManual,
            btnDevolverManual, btnVolverQR, btnCancelarSeleccion;

    private Prestamo prestamo;
    private PrestamoEquipo equipoSeleccionado;

    // =============================================================
    // INITIALIZE
    // =============================================================
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Platform.runLater(() -> {
            txtScan.requestFocus();
            txtScan.positionCaret(txtScan.getText().length());
        });

        txtScan.setOnAction(e -> {
            String raw = txtScan.getText();
            String codigo = raw == null ? "" : raw.replace("\r", "")
                    .replace("\n", "").trim();

            if (!codigo.isEmpty()) manejarCodigoEscaneado(codigo);

            txtScan.clear();
            txtScan.requestFocus();
        });

        listEquipos.getSelectionModel().selectedItemProperty()
                .addListener((obs, old, nuevo) -> {
                    if (nuevo != null) {
                        equipoSeleccionado = nuevo;
                        btnDevolverManual.setVisible(true);
                        btnCancelarSeleccion.setVisible(true);
                        btnVolverQR.setVisible(false);
                        lblDeshabilitarManual.setVisible(false);
                        btnIngresoManual.setVisible(false);
                        lblProblemasQR.setVisible(false);
                    }
                });
    }

    // =============================================================
    // MANEJAR QR
    // =============================================================
    @FXML
    private void manejarCodigoEscaneado(String codigo) {
        Optional<Equipo> equipoOpt = equipoRepo.findByCodigoQr(txtScan.getText());

        if (equipoOpt.isEmpty()) {
            mostrarMensaje("Error", "Código QR inválido",
                    "No existe un equipo asociado al QR: " + codigo, Alert.AlertType.WARNING);
            return;
        }

        Equipo equipo = equipoOpt.get();

        boolean pertenece = listEquipos.getItems().stream()
                .anyMatch(e -> e.getEquipo().getId().equals(equipo.getId()));

        if (!pertenece) {
            mostrarMensaje("Error", "Equipo incorrecto",
                    "El equipo escaneado no pertenece a este préstamo.", Alert.AlertType.WARNING);
            return;
        }

        PrestamoEquipo pe = prestamoEquipoRepo.findByEquipoAndPrestamo(equipo, prestamo).get();
        pe.setEstadoDevolucion(EstadoDevolucion.DEVUELTO);
        prestamoEquipoRepo.save(pe);

        listEquipos.getItems().removeIf(e -> e.getEquipo().getId().equals(equipo.getId()));

        actualizarLista();
    }

    // =============================================================
    // CARGAR DATOS
    // =============================================================
    public void setPrestamo(Prestamo prestamo) {
        this.prestamo = prestamo;
        cargarDatos();
        iniciarLista();
    }

    private void cargarDatos() {
        labelNroRefPrest.setText(prestamo.getNroReferencia());
    }

    private void iniciarLista() {
        listEquipos.setItems(FXCollections.observableArrayList(prestamo.getEquipos()));
    }

    // =============================================================
    // DEVOLUCIÓN MANUAL
    // =============================================================
    @FXML
    private void devolverEquipoManual() {
        equipoSeleccionado.setEstadoDevolucion(EstadoDevolucion.DEVUELTO);
        prestamoEquipoRepo.save(equipoSeleccionado);

        listEquipos.getItems().remove(equipoSeleccionado);

        btnDevolverManual.setVisible(false);
        btnCancelarSeleccion.setVisible(false);
        btnVolverQR.setVisible(true);

        listEquipos.getSelectionModel().clearSelection();
        actualizarLista();
    }

    public void actualizarLista() {
        if (listEquipos.getItems().isEmpty()) {
            btnFinalizarPrestamo.setVisible(true);
            btnVolverQR.setVisible(false);
            lblDeshabilitarManual.setVisible(false);
            btnIngresoManual.setVisible(false);
        }
    }

    // =============================================================
    // FINALIZAR PRÉSTAMO (ACTUALIZADO)
    // =============================================================
    @FXML
    private void finalizarPrestamo(ActionEvent e) {

        prestamo.setEstado(EstadoPrestamo.CERRADO);

        // ★ NUEVO: Fecha de cierre
        prestamo.setFechaFin(LocalDateTime.now());
        prestamoRepository.save(prestamo);
        mostrarMensaje("Éxito","Docente registrado correctamente","", Alert.AlertType.INFORMATION);
        irAPrestamosActivos(e);
    }

    private void mostrarMensaje(String titulo, String cabecera, String contenido, Alert.AlertType alertType) {
        Alert a = new Alert(alertType);
        a.setTitle(titulo);
        a.setHeaderText(cabecera);
        a.setContentText(contenido);
        a.showAndWait();
    }
    // =============================================================
    // NAVEGAR
    // =============================================================
    @FXML
    public void irAPrestamosActivos(ActionEvent e) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/com.application.gestiondenotebooks/PrestamosActivos.fxml"));
            loader.setControllerFactory(context::getBean);

            Parent root = loader.load();

            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Préstamos Activos");
            stage.centerOnScreen();
            stage.show();

        } catch (IOException ex) {
            ex.printStackTrace();
            mostrarMensaje("Error", "No se pudo cargar la pantalla.",
                    "Intentelo nuevamente.", Alert.AlertType.WARNING);
        }
    }

    // =============================================================
    // MANUAL / QR
    // =============================================================
    @FXML
    private void cambiarAIngresoManual(ActionEvent e) {
        lblProblemasQR.setVisible(false);
        btnIngresoManual.setVisible(false);
        btnVolverQR.setVisible(true);
        lblDeshabilitarManual.setVisible(true);
        btnDevolverManual.setDisable(false);
    }

    @FXML
    private void cambiarAIngresoQR(ActionEvent e) {
        btnDevolverManual.setDisable(true);
        lblProblemasQR.setVisible(true);
        btnIngresoManual.setVisible(true);
        btnVolverQR.setVisible(false);
        lblDeshabilitarManual.setVisible(false);
    }

    @FXML
    public void cancelarSeleccion() {
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
