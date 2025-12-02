package com.application.gestiondenotebooks.controller;

import com.application.gestiondenotebooks.enums.EstadoPrestamo;
import com.application.gestiondenotebooks.enums.Turno;
import com.application.gestiondenotebooks.model.Aula;
import com.application.gestiondenotebooks.model.Docente;
import com.application.gestiondenotebooks.model.Materia;
import com.application.gestiondenotebooks.model.Prestamo;
import com.application.gestiondenotebooks.repository.AulaRepository;
import com.application.gestiondenotebooks.repository.DocenteRepository;
import com.application.gestiondenotebooks.repository.MateriaRepository;
import com.application.gestiondenotebooks.repository.PrestamoRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;


@Component
public class NuevoPrestamoController implements Initializable {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private MateriaRepository materiaRepo;

    @Autowired
    private AulaRepository aulaRepo;

    @Autowired
    private DocenteRepository docenteRepo;

    @Autowired
    private PrestamoRepository prestamoRepo;

    @FXML
    private ComboBox<Materia> cmbMateria;
    @FXML
    private ComboBox<Aula> cmbAula;
    @FXML
    private ComboBox<Turno> cmbHorario;
    @FXML
    private ComboBox<Docente> cmbDocente;
    @FXML
    private Label labelCampoOblig;
    @Autowired
    private PrestamoRepository prestamoRepository;
    private String nroReferencia;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configurarCmb();
    }
    public void volverAlMenuPrincipal(javafx.event.ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com.application.gestiondenotebooks/VentanaPrincipal.fxml"));
        loader.setControllerFactory(context::getBean);  // <-- para que Spring cree el controller
        Parent root = loader.load();
        // obtener el Stage actual desde el botón que disparó el evento
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        boolean estabaEnPantallaCompleta = stage.isFullScreen(); // Capturar estado
        stage.setTitle("Sistema de Gestión de Notebooks - CAECE");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setFullScreen(estabaEnPantallaCompleta);
        stage.centerOnScreen();
        stage.show();
    }

    public void irAEscaneo(javafx.event.ActionEvent e) throws Exception{
        if(hayCamposIncompletos())
            labelCampoOblig.setVisible(true);
        else {
            crearPrestamo();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com.application.gestiondenotebooks/Escaneo.fxml"));
            loader.setControllerFactory(context::getBean);  // <-- para que Spring cree el controller
            Parent root = loader.load();
            EscaneoController esc = loader.getController();
            esc.init(nroReferencia);
            // obtener el Stage actual desde el botón que disparó el evento
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setTitle("Escanear equipos");
            stage.setScene(scene);
            stage.setFullScreen(stage.isFullScreen());
            stage.centerOnScreen();
            stage.show();
        }
    }
    public boolean hayCamposIncompletos() {
        return cmbDocente.getValue()==null ||
                cmbMateria.getValue()==null ||
                cmbAula.getValue()==null ||
                cmbHorario.getValue()==null;
    }
    public void configurarCmb(){
        var todasLasMaterias = FXCollections.observableArrayList(materiaRepo.findAll());
        var todosLosDocentes = FXCollections.observableArrayList(docenteRepo.findAll());
        cmbAula.setItems(FXCollections.observableArrayList(aulaRepo.findAll()));
        cmbHorario.setItems(FXCollections.observableArrayList(Turno.MAÑANA,Turno.TARDE,Turno.NOCHE));
        cmbMateria.setItems(todasLasMaterias);
        cmbDocente.setItems(todosLosDocentes);
        cmbMateria.setVisibleRowCount(5);
        cmbDocente.setVisibleRowCount(5);
        cmbAula.setVisibleRowCount(5);
        cmbMateria.setCellFactory(lv -> {
            ListCell<Materia> cell = new ListCell<>() {
                @Override protected void updateItem(Materia it, boolean empty) {
                    super.updateItem(it, empty);
                    setText(empty || it == null ? null : it.toString());
                    setWrapText(true);
                }
            };
            cell.prefWidthProperty().bind(cmbMateria.widthProperty().subtract(30)); // clave
            return cell;
        });
    }
    public void limpiar(){
        cmbAula.getSelectionModel().clearSelection();
        cmbDocente.getSelectionModel().clearSelection();
        cmbMateria.getSelectionModel().clearSelection();
        cmbHorario.getSelectionModel().clearSelection();
    }

    public void crearPrestamo() {
        var aulaSel    = cmbAula.getSelectionModel().getSelectedItem();
        var docenteSel = cmbDocente.getSelectionModel().getSelectedItem();
        var materiaSel = cmbMateria.getSelectionModel().getSelectedItem();
        var horarioSel = cmbHorario.getSelectionModel().getSelectedItem(); // String/Enum


        var aulaRef    = aulaRepo.getReferenceById(aulaSel.getId());
        var docenteRef = docenteRepo.getReferenceById(docenteSel.getId());
        var materiaRef = materiaRepo.getReferenceById(materiaSel.getId());

        Prestamo p = new Prestamo();
        nroReferencia=UUID.randomUUID().toString().toUpperCase().substring(0,6);
        p.setNroReferencia(nroReferencia);
        p.setDocente(docenteRef);
        p.setMateria(materiaRef);
        p.setFecha(LocalDate.now());
        p.setTurno(horarioSel);
        p.setAula(aulaRef);
        p.setEstado(EstadoPrestamo.ABIERTO);
        prestamoRepo.save(p);

    }

    @FXML
    private void irANuevoDocente(javafx.event.ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com.application.gestiondenotebooks/OtroDocente.fxml"));
        loader.setControllerFactory(context::getBean);  // <-- para que Spring cree el controller
        Parent root = loader.load();
        // obtener el Stage actual desde el botón que disparó el evento
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Agregar un nuevo docente");
        stage.setScene(scene);
        stage.setFullScreen(stage.isFullScreen());
        stage.centerOnScreen();
        stage.show();
    }

    @FXML
    private void irANuevaMateria(javafx.event.ActionEvent e) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com.application.gestiondenotebooks/OtraMateria.fxml"));
        loader.setControllerFactory(context::getBean);  // <-- para que Spring cree el controller
        Parent root = loader.load();
        // obtener el Stage actual desde el botón que disparó el evento
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Agregar una nueva materia");
        stage.setScene(scene);
        stage.setFullScreen(stage.isFullScreen());
        stage.centerOnScreen();
        stage.show();
    }
    public void setDocenteCmb(Docente nuevoDocente) {
        cmbDocente.getSelectionModel().select(nuevoDocente);
    }

    public void setMateriaCmb(Materia nuevaMateria) {
        cmbMateria.getSelectionModel().select(nuevaMateria);
    }
}
