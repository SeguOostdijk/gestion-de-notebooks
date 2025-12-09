package com.application.gestiondenotebooks.controller;

import com.application.gestiondenotebooks.GestiondenotebooksApplication;
import com.application.gestiondenotebooks.enums.EstadoDevolucion;
import com.application.gestiondenotebooks.enums.TipoEquipo;
import com.application.gestiondenotebooks.model.Equipo;
import com.application.gestiondenotebooks.model.Prestamo;
import com.application.gestiondenotebooks.model.PrestamoEquipo;
import com.application.gestiondenotebooks.pdf.PdfGenerator;
import com.application.gestiondenotebooks.repository.EquipoRepository;
import com.application.gestiondenotebooks.repository.PrestamoEquipoRepository;
import com.application.gestiondenotebooks.repository.PrestamoRepository;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;


@Component
public class EscaneoController implements Initializable {


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
    private Label labelCampoOblig, labelNroRefPrest;
    @FXML
    private TextField txtEquipos;
    @FXML
    private Button btnCancelar;
    @FXML
    private Button btnCrearPrestamo;
    private Long idReferencia;
    @FXML
    private Pane principalPaneNP;
    @FXML
    private TextField txtScan;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private EquipoRepository equipoRepo;

    @Autowired
    private PrestamoRepository prestamoRepo;

    @Autowired
    private PrestamoEquipoRepository prestamoEquipoRepo;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            txtScan.requestFocus();
            txtScan.positionCaret(txtScan.getText().length());
        });
        txtScan.setOnAction(e -> {
            String raw = txtScan.getText();
            String codigo = raw == null ? "" : raw.replace("\r","").replace("\n","").trim();

            if (!codigo.isEmpty()) {
                manejarCodigoEscaneado(codigo);
            }
            txtScan.clear();
            txtScan.requestFocus();
        });
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

    private void manejarCodigoEscaneado(String codigo) {
        Optional<Equipo> equipoOpt=equipoRepo.findByCodigoQr(codigo);
        if (equipoOpt.isEmpty())
            mostrarMensaje("Error en el ingreso","Error","No existe un equipo asociado al código qr: "+codigo, Alert.AlertType.WARNING);
        else{
            Equipo equipo = equipoOpt.get();
            boolean yaEnLista = listEquipos.getItems().stream()
                    .anyMatch(e -> e.getId().equals(equipo.getId()));
            if (yaEnLista) {
                mostrarMensaje("Error en el ingreso","Error","Ese equipo ya fue agregado a este préstamo.", Alert.AlertType.WARNING);
            }
            else{
                listEquipos.getItems().add(equipo);
                actualizarLista();
            }
        }
        System.out.println("Leido: "+codigo);
    }

    public void init(String nroReferencia) {
        this.idReferencia = prestamoRepo.findIdByNroReferencia(nroReferencia).get();
        labelNroRefPrest.setText(nroReferencia);
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
                mostrarMensaje("Error en el ingreso","Error","No existe un equipo " + cmbEquipos.getSelectionModel().getSelectedItem().toString() +" Nº " + txtEquipos.getText() +" en inventario.", Alert.AlertType.WARNING);
            else{
                Equipo equipo = equipoOpt.get();
                boolean yaEnLista = listEquipos.getItems().stream()
                        .anyMatch(e -> e.getId().equals(equipo.getId()));
                if (yaEnLista) {
                    mostrarMensaje("Error en el ingreso","Error","Ese equipo ya fue agregado a este préstamo.", Alert.AlertType.WARNING);
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
    private void mostrarMensaje(String titulo, String cabecera, String contenido, Alert.AlertType alertType) {
        Alert a = new Alert(alertType);
        a.setTitle(titulo);
        a.setHeaderText(cabecera);
        a.setContentText(contenido);
        a.showAndWait();
    }
    @FXML
    private void crearPrestamoEquipos(javafx.event.ActionEvent e){
        List<Integer> listaNotebooks = new ArrayList<>();
        Prestamo prestamoActual=prestamoRepo.findDetalleById(idReferencia)
                .orElseThrow(() -> new IllegalArgumentException("Préstamo no existe"));
        for (int i=0;i<listEquipos.getItems().size();i++){
            Equipo equipoActual=listEquipos.getItems().get(i);
            PrestamoEquipo prestamoEquipo=new PrestamoEquipo();
            prestamoEquipo.setEquipo(equipoActual);
            prestamoEquipo.setEstadoDevolucion(EstadoDevolucion.EN_PRESTAMO);
            prestamoEquipo.setPrestamo(prestamoActual);
            prestamoEquipoRepo.save(prestamoEquipo);
            if(equipoActual.getTipo().equals(TipoEquipo.NOTEBOOK))
                listaNotebooks.add(equipoActual.getNroEquipo());
        }
        mostrarMensaje("Éxito","Préstamo registrado correctamente", "",Alert.AlertType.INFORMATION);
        try {
            File pdf = PdfGenerator.generarFormularioPrestamo(
                    prestamoActual.getNroReferencia(),
                    prestamoActual.getDocente().getNombre()+" "+prestamoActual.getDocente().getApellido(),
                    prestamoActual.getMateria().getNombre(),
                    prestamoActual.getTurno().name(),
                    prestamoActual.getAula().getCodigo_aula(),
                    listaNotebooks
            );

            GestiondenotebooksApplication.getHostServicesInstance()
                    .showDocument(pdf.toURI().toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        javafx.application.Platform.runLater(() -> irAVentanaPrincipal(e));
    }

    public void irAVentanaPrincipal(javafx.event.ActionEvent e) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com.application.gestiondenotebooks/VentanaPrincipal.fxml"));
            loader.setControllerFactory(context::getBean);  // <-- para que Spring cree el controller
            Parent root = loader.load();
            // obtener el Stage actual desde el botón que disparó el evento
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            Scene scene = stage.getScene();        // reutilizás la misma escena
            scene.setRoot(root);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    @FXML
    private void volver(javafx.event.ActionEvent e) {
        ButtonType btnAceptar  = new ButtonType("Aceptar", ButtonBar.ButtonData.OK_DONE);
        ButtonType btnCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "¿Desea cancelar el préstamo actual?",
                btnAceptar, btnCancelar);
        alert.setTitle("Cancelar préstamo");
        alert.setHeaderText("¿Cancelar el préstamo?");
        Optional<ButtonType> resultado = alert.showAndWait();
        if (resultado.isPresent() && resultado.get() == btnAceptar) {
           prestamoRepo.deleteById(idReferencia);
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com.application.gestiondenotebooks/NuevoPrestamo.fxml"));
                loader.setControllerFactory(context::getBean);
                Parent root = loader.load();
                Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                Scene scene = stage.getScene();
                scene.setRoot(root);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}


