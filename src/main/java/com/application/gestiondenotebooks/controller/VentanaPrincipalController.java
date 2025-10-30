package com.application.gestiondenotebooks.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class VentanaPrincipalController {

    @Autowired
    private ApplicationContext context; // Spring

    public void irANuevoPrestamo(javafx.event.ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com.application.gestiondenotebooks/NuevoPrestamo.fxml"));
        loader.setControllerFactory(context::getBean);  // <-- para que Spring cree el controller
        Parent root = loader.load();
        // obtener el Stage actual desde el botón que disparó el evento
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Agregar Nuevo Préstamo");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }
}
