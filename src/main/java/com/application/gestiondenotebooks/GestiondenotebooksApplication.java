package com.application.gestiondenotebooks;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class GestiondenotebooksApplication extends Application {


	public static ConfigurableApplicationContext context;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void init() {
		context = SpringApplication.run(GestiondenotebooksApplication.class);
	}

	@Override
	public void start(Stage stage) throws Exception {
		FXMLLoader fxml = new FXMLLoader(getClass().getResource(("/com.application.gestiondenotebooks/ventanaPrincipal.fxml")));
		fxml.setControllerFactory(context::getBean); // Controllers como beans Spring
		Scene scene=new Scene(fxml.load());
		stage.setTitle("Sistema de Gestión de Notebooks - CAECE");
		stage.setScene(scene);
		stage.show();
	}

	@Override
	public void stop(){
		context.stop();
	}
}
