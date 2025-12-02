package com.application.gestiondenotebooks.controller;

import com.application.gestiondenotebooks.enums.TipoEquipo;
import com.application.gestiondenotebooks.service.ConteoService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

@Component
public class VentanaPrincipalController implements Initializable {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private ConteoService conteoService;

    @FXML private Label ntbCountLabel;
    @FXML private Label cgdCountLabel;
    @FXML private Label mouseCountLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cargarConteoEquipos();
    }

    private void cargarConteoEquipos() {
        try {
            Map<String, Long> disponibles = conteoService.getEquiposDisponiblesPorTipo();

            ntbCountLabel.setText(disponibles.getOrDefault(TipoEquipo.NOTEBOOK.name(), 0L).toString());
            cgdCountLabel.setText(disponibles.getOrDefault(TipoEquipo.CARGADOR.name(), 0L).toString());
            mouseCountLabel.setText(disponibles.getOrDefault(TipoEquipo.MOUSE.name(), 0L).toString());

        } catch (Exception e) {
            System.err.println("Error al cargar el conteo de equipos: " + e.getMessage());
            ntbCountLabel.setText("N/D");
            cgdCountLabel.setText("N/D");
            mouseCountLabel.setText("N/D");
        }
    }


    // ============================================================
    //      NAVEGACIÓN A NUEVO PRÉSTAMO
    // ============================================================
    public void irANuevoPrestamo(javafx.event.ActionEvent e) throws IOException {

        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        boolean estabaEnPantallaCompleta = stage.isFullScreen(); // Capturar estado

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com.application.gestiondenotebooks/NuevoPrestamo.fxml"));
        loader.setControllerFactory(context::getBean);
        Parent root = loader.load();

        Scene scene = new Scene(root);
        stage.setTitle("Agregar Nuevo Préstamo");
        stage.setScene(scene);
        stage.setFullScreen(estabaEnPantallaCompleta); // Reaplicar estado
        stage.centerOnScreen();
        stage.show();
    }

    // ============================================================
    //      NAVEGACIÓN A PRÉSTAMOS ACTIVOS
    // ============================================================
    public void irAPrestamosActivos(javafx.event.ActionEvent e) throws IOException {

        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        boolean estabaEnPantallaCompleta = stage.isFullScreen(); // Capturar estado

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com.application.gestiondenotebooks/PrestamosActivos.fxml"));
        loader.setControllerFactory(context::getBean);
        Parent root = loader.load();

        Scene scene = new Scene(root);
        stage.setTitle("Prestamos Activos");
        stage.setScene(scene);
        stage.setFullScreen(estabaEnPantallaCompleta); // Reaplicar estado
        stage.centerOnScreen();
        stage.show();
    }

    // ============================================================
    //      NAVEGACIÓN A HISTÓRICO
    // ============================================================
    public void irAHistorico(javafx.event.ActionEvent e) throws IOException {

        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        boolean estabaEnPantallaCompleta = stage.isFullScreen(); // Capturar estado

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com.application.gestiondenotebooks/Historico.fxml"));
        loader.setControllerFactory(context::getBean);
        Parent root = loader.load();

        stage.setScene(new Scene(root));
        stage.setFullScreen(estabaEnPantallaCompleta); // Reaplicar estado
        stage.setTitle("Historial de Préstamos");
        stage.centerOnScreen();
        stage.show();
    }
}