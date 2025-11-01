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
public class NuevoPrestamoController {

    @Autowired
    private ApplicationContext context;

    public void volverAlMenuPrincipal(javafx.event.ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com.application.gestiondenotebooks/VentanaPrincipal.fxml"));
        loader.setControllerFactory(context::getBean);  // <-- para que Spring cree el controller
        Parent root = loader.load();
        // obtener el Stage actual desde el botón que disparó el evento
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void irAEscaneo(javafx.event.ActionEvent e) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com.application.gestiondenotebooks/Escaneo.fxml"));
        loader.setControllerFactory(context::getBean);  // <-- para que Spring cree el controller
        Parent root = loader.load();
        // obtener el Stage actual desde el botón que disparó el evento
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Escanear equipos");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }
}
