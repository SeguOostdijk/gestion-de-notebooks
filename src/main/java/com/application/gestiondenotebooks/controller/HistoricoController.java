package com.application.gestiondenotebooks.controller;

import com.application.gestiondenotebooks.enums.EstadoPrestamo;
import com.application.gestiondenotebooks.model.Prestamo;
import com.application.gestiondenotebooks.repository.PrestamoRepository;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.util.Callback;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

@Component
public class HistoricoController implements Initializable {

    @Autowired
    private PrestamoRepository prestamoRepository;

    @Autowired
    private ApplicationContext context;

    @FXML
    private ListView<Prestamo> listHistorico;

    @FXML
    private Label lblNroReferencia, lblDocente, lblMateria,
            lblHorario, lblAula, lblEquipos, lblFechaFin;

    private final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        configurarListaCompacta();
        cargarHistorial();

        listHistorico.getSelectionModel().selectedItemProperty()
                .addListener((obs, old, nuevo) -> {
                    if (nuevo != null) mostrarDetalle(nuevo);
                });
    }

    // ============================================================
    //      LISTA COMPACTA
    // ============================================================
    private void configurarListaCompacta() {

        listHistorico.setCellFactory(new Callback<ListView<Prestamo>, ListCell<Prestamo>>() {
            @Override
            public ListCell<Prestamo> call(ListView<Prestamo> list) {

                return new ListCell<Prestamo>() {

                    @Override
                    protected void updateItem(Prestamo item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty || item == null) {
                            setText(null);
                            return;
                        }

                        String docente = item.getDocente() != null ? item.getDocente().getNombre() + " " + item.getDocente().getApellido() : "—";
                        String materia = item.getMateria() != null ? item.getMateria().getNombre() : "—";

                        setText("REF-" + item.getNroReferencia()
                                + ";  " + docente
                                + ";  " + materia);
                    }
                };
            }
        });
    }

    // ============================================================
    //      CARGAR HISTORIAL
    // ============================================================
    private void cargarHistorial() {
        List<Prestamo> cerrados =
                prestamoRepository.findByEstadoOrderByFechaFinDesc(EstadoPrestamo.CERRADO);

        listHistorico.setItems(FXCollections.observableArrayList(cerrados));
    }

    // ============================================================
    //          MOSTRAR DETALLE
    // ============================================================
    private void mostrarDetalle(Prestamo p) {

        lblNroReferencia.setText(p.getNroReferencia());
        lblDocente.setText(p.getDocente().getNombre());
        lblMateria.setText(p.getMateria().getNombre());
        lblHorario.setText(p.getTurno().name());
        lblAula.setText(p.getAula().getCodigo_aula());
        lblEquipos.setText(p.getResumenEquipos());

        lblFechaFin.setText(
                p.getFechaFin() != null ? p.getFechaFin().format(formatter) : "—"
        );
    }

    // ============================================================
    //      VOLVER (IR A VENTANA PRINCIPAL)
    // ============================================================
    public void irAVentanaPrincipal(javafx.event.ActionEvent e) throws IOException {

        // 1. Obtener la Stage actual y su estado de pantalla completa
        Stage actualStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        boolean estabaEnPantallaCompleta = actualStage.isFullScreen(); // Capturar estado

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com.application.gestiondenotebooks/VentanaPrincipal.fxml")
        );

        loader.setControllerFactory(context::getBean);

        Parent root = loader.load();

        // 2. Reutilizar la Stage, establecer la nueva Scene
        actualStage.setTitle("Gestión de Notebooks CAECE");
        actualStage.setScene(new Scene(root));

        // 3. Reaplicar el estado de pantalla completa
        actualStage.setFullScreen(estabaEnPantallaCompleta); // Reaplicar estado

        actualStage.centerOnScreen();
        actualStage.show();
    }
}