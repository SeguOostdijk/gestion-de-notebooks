package com.application.gestiondenotebooks.controller;

import com.application.gestiondenotebooks.enums.TipoEquipo;
import com.application.gestiondenotebooks.model.Equipo;
import com.application.gestiondenotebooks.repository.EquipoRepository;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
public class EscaneoController implements Initializable {
    @Autowired
    private EquipoRepository repository;
    private Equipo equipo;
    private Long id;
    @FXML
    private ListView<Equipo> listEquipos;
    @FXML
    private ComboBox<TipoEquipo> cmbEquipos;
    @FXML
    private Label labelContador;
    @FXML
    private Button btnEliminar;
    @FXML
    private Label labelCampoOblig;
    @FXML
    private TextField txtEquipos;
    @FXML
    private Button btnCancelar;
    @FXML
    private Button btnCrearPrestamo;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        actualizarLista();
        configurarCMB();
        listEquipos.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if(newValue!=null) {
                id = newValue.getId();
                equipo=repository.findById(id).get();
                btnEliminar.setVisible(true);
                btnCancelar.setVisible(true);
                btnCrearPrestamo.setVisible(false);
            }
        }));
    }

    public void configurarCMB(){
        cmbEquipos.setPromptText("NOTEBOOK");
        cmbEquipos.setItems(FXCollections.observableArrayList(TipoEquipo.NOTEBOOK,TipoEquipo.CARGADOR,TipoEquipo.MOUSE));

    }
    public void actualizarLista(){
        listEquipos.setItems(FXCollections.observableArrayList(repository.findAll()));
        labelContador.setText(String.valueOf(listEquipos.getItems().size()));
    }
    public void eliminarEquipo(){
        repository.delete(equipo);
        btnEliminar.setVisible(false);
        btnCancelar.setVisible(false);
        btnCrearPrestamo.setVisible(true);
        actualizarLista();
        if(listEquipos.getItems().isEmpty())
            btnCrearPrestamo.setDisable(true);
    }

    public void agregarEquipoManual(){
        if (txtEquipos.getText().isEmpty())
            labelCampoOblig.setVisible(true);
        else {
            if (labelCampoOblig.isVisible())
                labelCampoOblig.setVisible(false);
            if (repository.existsByTipoAndNroEquipo(cmbEquipos.getSelectionModel().getSelectedItem(),Integer.valueOf(txtEquipos.getText()))){
                mostrarWarn("Duplicado",
                        "Ya existe un " + cmbEquipos.getSelectionModel().getSelectedItem() + " con número " + Integer.valueOf(txtEquipos.getText()) + ".",
                        "Verificá el código escaneado o el número ingresado.");
                return;
            }
            Equipo equipo=new Equipo();
            equipo.setNroEquipo(Integer.valueOf(txtEquipos.getText()));
            if (cmbEquipos.getSelectionModel().getSelectedItem()==null)
                equipo.setTipo(TipoEquipo.valueOf(cmbEquipos.getPromptText()));
            else
                equipo.setTipo(cmbEquipos.getSelectionModel().getSelectedItem());
            repository.save(equipo);
            if (btnCrearPrestamo.isDisable())
                btnCrearPrestamo.setDisable(false);
            actualizarLista();
        }
    }
    public void cancelarSeleccion(){
        btnCancelar.setVisible(false);
        btnEliminar.setVisible(false);
        btnCrearPrestamo.setVisible(true);
    }
    private void mostrarWarn(String titulo, String cabecera, String contenido) {
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setTitle(titulo);
        a.setHeaderText(cabecera);
        a.setContentText(contenido);
        a.showAndWait();
    }

}
