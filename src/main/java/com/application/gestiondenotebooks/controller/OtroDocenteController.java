package com.application.gestiondenotebooks.controller;

import com.application.gestiondenotebooks.model.Docente;
import com.application.gestiondenotebooks.model.DocenteMateria;
import com.application.gestiondenotebooks.model.Materia;
import com.application.gestiondenotebooks.repository.DocenteMateriaRepository;
import com.application.gestiondenotebooks.repository.DocenteRepository;
import com.application.gestiondenotebooks.repository.MateriaRepository;
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
import java.util.ResourceBundle;

@Component
public class OtroDocenteController implements Initializable {

    @FXML
    private TextField txtNombre, txtApellido, txtDNI;

    @FXML
    private ListView<Materia> listaMaterias;

    @FXML
    private Button btnGuardar, btnEliminar, btnCancelar, btnAgregarMateria, btnLimpiar;

    @FXML
    private ComboBox<Materia> cmbMaterias;

    @FXML
    private Label campoObligNombre, campoObligApellido, campoObligDNI;

    @Autowired
    private DocenteRepository docenteRepo;

    @Autowired
    private DocenteMateriaRepository docenteMateriaRepo;

    @Autowired
    private ApplicationContext context; // Spring

    @Autowired
    private MateriaRepository materiaRepo;

    @Autowired
    private NuevoPrestamoController nuevoPrestamoController;

    private Materia seleccionadaLista;

    private Docente nuevoDocente;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configurarCmb();
        listaMaterias.getSelectionModel().selectedItemProperty().addListener((obs, old, nuevo) -> {
            if (nuevo != null) {
                seleccionadaLista = nuevo;
                btnEliminar.setVisible(true);
                btnCancelar.setVisible(true);
                btnGuardar.setVisible(false);
                btnLimpiar.setVisible(false);
            }
        });
    }

    private void configurarCmb() {
        cmbMaterias.setCellFactory(lv -> {
            ListCell<Materia> cell = new ListCell<>() {
                @Override protected void updateItem(Materia it, boolean empty) {
                    super.updateItem(it, empty);
                    setText(empty || it == null ? null : it.toString());
                    setWrapText(true);
                }
            };
            cell.prefWidthProperty().bind(cmbMaterias.widthProperty().subtract(30)); // clave
            return cell;
        });
        cmbMaterias.setVisibleRowCount(5);
        cargarMaterias();
    }

    private void cargarMaterias() {
        List<Materia> materias=materiaRepo.findAll();
        cmbMaterias.setItems(FXCollections.observableArrayList(materias));
    }

    @FXML
    private void agregarMateria(ActionEvent event) {
        Materia seleccionada = cmbMaterias.getValue();
        if (seleccionada == null) {
            mostrarWarn("Error en el ingreso","Error","Debe seleccionar una materia para añadirla a la lista");
        }
        else {
        // Evitar duplicados
          if (!listaMaterias.getItems().contains(seleccionada)) {
            listaMaterias.getItems().add(seleccionada);
            cmbMaterias.getItems().remove(seleccionada);
            cmbMaterias.setValue(null);
          }
          else
            mostrarWarn("Error en el ingreso", "Error","La materia seleccionada ya está en la lista");
        }
    }

    @FXML
    private void eliminarMateria(ActionEvent event) {
        listaMaterias.getItems().remove(seleccionadaLista);
        if (!cmbMaterias.getItems().contains(seleccionadaLista)) {
            cmbMaterias.getItems().add(seleccionadaLista);
        }
        listaMaterias.getSelectionModel().clearSelection();
        btnLimpiar.setVisible(true);
        btnGuardar.setVisible(true);
        btnEliminar.setVisible(false);
        btnCancelar.setVisible(false);
    }

    @FXML
    private void cancelarSeleccion(ActionEvent event){
        listaMaterias.getSelectionModel().clearSelection();
        btnLimpiar.setVisible(true);
        btnGuardar.setVisible(true);
        btnEliminar.setVisible(false);
        btnCancelar.setVisible(false);
    }
    @FXML
    private void limpiar(ActionEvent event){
        txtNombre.clear();
        txtApellido.clear();
        txtDNI.clear();
        cmbMaterias.getSelectionModel().clearSelection();
    }

    @FXML
    private void guardarDocente(javafx.event.ActionEvent e)  {
        if (hayCamposIncompletos()) {
            if (txtNombre.getText().isEmpty())
                campoObligNombre.setVisible(true);
            if (txtDNI.getText().isEmpty())
                campoObligDNI.setVisible(true);
            if (txtApellido.getText().isEmpty())
                campoObligApellido.setVisible(true);
        }
        else {
            nuevoDocente=new Docente();
            nuevoDocente.setNombre(txtNombre.getText().toUpperCase());
            nuevoDocente.setApellido(txtApellido.getText().toUpperCase());
            nuevoDocente.setDni(txtDNI.getText());
            docenteRepo.save(nuevoDocente);
            if(!listaMaterias.getItems().isEmpty()){
                for (int i=0;i<listaMaterias.getItems().size();i++){
                    DocenteMateria docenteMateria=new DocenteMateria();
                    docenteMateria.setDocente(nuevoDocente);
                    docenteMateria.setMateria(listaMaterias.getItems().get(i));
                    docenteMateriaRepo.save(docenteMateria);
                }
            }
            javafx.application.Platform.runLater(() -> irANuevoPrestamo(e));
        }
    }

    @FXML
    private void irANuevoPrestamo(javafx.event.ActionEvent e){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com.application.gestiondenotebooks/NuevoPrestamo.fxml"));
            loader.setControllerFactory(context::getBean);
            Parent root = loader.load();
            if (nuevoDocente!=null)
              nuevoPrestamoController.setDocenteCmb(nuevoDocente);

            javafx.application.Platform.runLater(() -> {
                Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.centerOnScreen();
                stage.show();
            });

        } catch (IOException ex) {
            ex.printStackTrace();
            mostrarWarn("Error", "No se pudo volver a nuevo prestamo.","Intentelo de nuevo");
        }
    }

    private boolean hayCamposIncompletos() {
        return  txtNombre.getText().isEmpty() ||
                txtDNI.getText().isEmpty() ||
                txtApellido.getText().isEmpty();
    }

    private void mostrarWarn(String titulo, String cabecera, String contenido) {
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setTitle(titulo);
        a.setHeaderText(cabecera);
        a.setContentText(contenido);
        a.showAndWait();
    }



}
