package com.application.gestiondenotebooks.controller;

import com.application.gestiondenotebooks.model.Materia;
import com.application.gestiondenotebooks.repository.MateriaRepository;
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

@Component
public class OtraMateriaController implements Initializable {

    @FXML
    private ComboBox<String> cmbPlan;
    @FXML
    private TextField txtNombre, txtOtroPlan;
    @FXML
    private Button btnGuardar,btnLimpiar;
    @FXML
    private Label labelCampoOblig;
    private Materia nuevaMateria;

    @Autowired
    private MateriaRepository materiaRepo;
    @Autowired
    private ApplicationContext context;
    @Autowired
    private NuevoPrestamoController nuevoPrestamoController;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configurarCmb();
    }

    private void configurarCmb() {
        List<String> planes=materiaRepo.findAllPlanId();
        cmbPlan.setItems(FXCollections.observableArrayList(planes));
        cmbPlan.setVisibleRowCount(5);
    }

    @FXML
    private void limpiar(){
        txtNombre.clear();
        cmbPlan.getSelectionModel().clearSelection();
        txtOtroPlan.clear();
    }

    @FXML
    private void guardar(javafx.event.ActionEvent e){
        if (hayCamposIncompletos())
            labelCampoOblig.setVisible(true);
        else {
            if(!txtOtroPlan.getText().isEmpty()&& !cmbPlan.getSelectionModel().isEmpty())
                mostrarMensaje("Error","Error en el ingreso","Debe seleccionar un plan existente o crear uno nuevo", Alert.AlertType.WARNING);
            else {
                String plan;
                if (cmbPlan.getSelectionModel().isEmpty())
                    plan=txtOtroPlan.getText();
                else
                    plan=cmbPlan.getSelectionModel().getSelectedItem();
                if (materiaRepo.existsByNombreAndPlanId(txtNombre.getText(),plan))
                    mostrarMensaje("Error en el ingreso", "Error", "La materia ya está registrada", Alert.AlertType.WARNING);
                else {
                    nuevaMateria = new Materia();
                    nuevaMateria.setNombre(txtNombre.getText().toUpperCase());
                    nuevaMateria.setPlanId(plan.toUpperCase());
                    materiaRepo.save(nuevaMateria);
                    mostrarMensaje("Éxito","Materia registrada correctamente","", Alert.AlertType.INFORMATION);
                    javafx.application.Platform.runLater(() -> irANuevoPrestamo(e));
                }
            }
        }
    }
    private void mostrarMensaje(String titulo, String cabecera, String contenido, Alert.AlertType alertType) {
        Alert a = new Alert(alertType);
        a.setTitle(titulo);
        a.setHeaderText(cabecera);
        a.setContentText(contenido);
        a.showAndWait();
    }

    private boolean hayCamposIncompletos(){
        return txtNombre.getText().isEmpty() ||
                (cmbPlan.getSelectionModel().isEmpty() &&
                 txtOtroPlan.getText().isEmpty());
    }

    @FXML
    private void irANuevoPrestamo(javafx.event.ActionEvent e){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com.application.gestiondenotebooks/NuevoPrestamo.fxml"));
            loader.setControllerFactory(context::getBean);
            Parent root = loader.load();
            if (nuevaMateria!=null)
                nuevoPrestamoController.setMateriaCmb(nuevaMateria);

            javafx.application.Platform.runLater(() -> {
                Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.centerOnScreen();
                stage.show();
            });

        } catch (IOException ex) {
            ex.printStackTrace();
            mostrarMensaje("Error", "No se pudo volver a nuevo prestamo.","", Alert.AlertType.WARNING);
        }
    }

}
