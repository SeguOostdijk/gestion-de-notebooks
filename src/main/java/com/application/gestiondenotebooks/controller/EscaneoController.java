package com.application.gestiondenotebooks.controller;

import com.application.gestiondenotebooks.enums.EstadoDevolucion;
import com.application.gestiondenotebooks.enums.TipoEquipo;
import com.application.gestiondenotebooks.model.Equipo;
import com.application.gestiondenotebooks.model.Prestamo;
import com.application.gestiondenotebooks.model.PrestamoEquipo;
import com.application.gestiondenotebooks.repository.EquipoRepository;
import com.application.gestiondenotebooks.repository.PrestamoEquipoRepository;
import com.application.gestiondenotebooks.repository.PrestamoRepository;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

@Component
public class EscaneoController implements Initializable {
    @Autowired
    private EquipoRepository repository;
    private Equipo equipoSeleccionado;
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
    private Long idReferencia;
    @FXML
    private Pane principalPaneNP;

    @Autowired
    private EquipoRepository equipoRepo;

    @Autowired
    private PrestamoRepository prestamoRepo;

    @Autowired
    private PrestamoEquipoRepository prestamoEquipoRepo;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        actualizarLista();
        configurarCMB();
        listEquipos.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if(newValue!=null) {
                equipoSeleccionado=newValue;
                btnEliminar.setVisible(true);
                btnCancelar.setVisible(true);
                btnCrearPrestamo.setVisible(false);
            }
        }));
    }
    public void init(String nroReferencia) {
        this.idReferencia = prestamoRepo.findIdByNroReferencia(nroReferencia).get();/* cargar datos, etc. */
    }

    public void configurarCMB(){
        cmbEquipos.setPromptText("NOTEBOOK");
        cmbEquipos.setItems(FXCollections.observableArrayList(TipoEquipo.NOTEBOOK,TipoEquipo.CARGADOR,TipoEquipo.MOUSE));

    }
    public void actualizarLista(){
        labelContador.setText(String.valueOf(listEquipos.getItems().size()));
        if (!listEquipos.getItems().isEmpty())
            btnCrearPrestamo.setDisable(false);
        else
            btnCrearPrestamo.setDisable(true);
    }
    public void eliminarEquipo() {
        listEquipos.getItems().remove(equipoSeleccionado);
        btnEliminar.setVisible(false);
        btnCancelar.setVisible(false);
        btnCrearPrestamo.setVisible(true);
        actualizarLista();
        if (listEquipos.getItems().isEmpty())
            btnCrearPrestamo.setDisable(true);
    }

    public void agregarEquipoManual(){
        if (txtEquipos.getText().isEmpty())
            labelCampoOblig.setVisible(true);
        else {
            labelCampoOblig.setVisible(false);
            if(cmbEquipos.getSelectionModel().isEmpty())
                cmbEquipos.getSelectionModel().select(TipoEquipo.NOTEBOOK);
            Optional<Equipo> equipoOpt=equipoRepo.findByTipoAndNroEquipo(cmbEquipos.getSelectionModel().getSelectedItem(),Integer.valueOf(txtEquipos.getText()));
            if (equipoOpt.isEmpty())
                mostrarWarn("Error en el ingreso","Error","No existe un equipo " + cmbEquipos.getSelectionModel().getSelectedItem().toString() +" Nº " + txtEquipos.getText() +" en inventario.");
            else{
                Equipo equipo = equipoOpt.get();
                boolean yaEnLista = listEquipos.getItems().stream()
                        .anyMatch(e -> e.getId().equals(equipo.getId()));
                if (yaEnLista) {
                    mostrarWarn("Error en el ingreso","Error","Ese equipo ya fue agregado a este préstamo.");
                }
                else{
                    listEquipos.getItems().add(equipo);
                    actualizarLista();
                }
            }
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
    @FXML
    private void crearPrestamoEquipos(){
        Prestamo prestamoActual=prestamoRepo.getReferenceById(idReferencia);
        for (int i=0;i<listEquipos.getItems().size();i++){
            PrestamoEquipo prestamoEquipo=new PrestamoEquipo();
            prestamoEquipo.setEquipo(listEquipos.getItems().get(i));
            prestamoEquipo.setEstadoDevolucion(EstadoDevolucion.EN_PRESTAMO);
            prestamoEquipo.setPrestamo(prestamoActual);
            prestamoEquipoRepo.save(prestamoEquipo);
        }
    }
    

}
