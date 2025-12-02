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
import javafx.scene.control.Button;
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
import java.util.List;
import java.util.ResourceBundle;

@Component
public class PrestamosActivosController implements Initializable {

    @Autowired
    private PrestamoRepository prestamoRepository;

    @Autowired
    private ApplicationContext context;

    @FXML
    private ListView<Prestamo> listPrestamos;

    @FXML
    private Label lblNroReferencia;
    @FXML
    private Label lblDocente;
    @FXML
    private Label lblMateria;
    @FXML
    private Label lblHorario;
    @FXML
    private Label lblAula;
    @FXML
    private Label lblEquiposDetalle;

    @FXML
    private Button btnFinalizar;
    @FXML
    private Button btnCancelar;

    private Prestamo prestamoSeleccionado;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        configurarListaCompacta();
        cargarPrestamos();

        listPrestamos.getSelectionModel().selectedItemProperty().addListener((obs, old, nuevo) -> {
            if (nuevo != null) {
                prestamoSeleccionado = nuevo;
                mostrarDetallePrestamo(nuevo);
                btnFinalizar.setVisible(true);
                btnCancelar.setVisible(true);
            }
        });

        btnFinalizar.setVisible(false);
        btnCancelar.setVisible(false);
    }

    // ============================================================
    //      CELL FACTORY PARA LISTA COMPACTA
    // ============================================================
    private void configurarListaCompacta() {

        listPrestamos.setCellFactory(new Callback<ListView<Prestamo>, ListCell<Prestamo>>() {
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

                        String docente = item.getDocente() != null ? item.getDocente().getNombre()+" "+item.getDocente().getApellido() : "—";
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
    //                   CARGAR PRÉSTAMOS
    // ============================================================
    private void cargarPrestamos() {
        EstadoPrestamo estado = EstadoPrestamo.ABIERTO;
        List<Prestamo> activos = prestamoRepository.findByEstado(estado);
        listPrestamos.setItems(FXCollections.observableArrayList(activos));
    }

    // ============================================================
    //                   MOSTRAR DETALLE
    // ============================================================
    private void mostrarDetallePrestamo(Prestamo p) {

        lblNroReferencia.setText(
                p.getNroReferencia() != null ? p.getNroReferencia() : "—"
        );

        lblDocente.setText(
                p.getDocente() != null ? p.getDocente().getNombre() : "—"
        );

        lblMateria.setText(
                p.getMateria() != null ? p.getMateria().getNombre() : "—"
        );

        lblHorario.setText(
                p.getTurno() != null ? p.getTurno().name() : "—"
        );

        lblAula.setText(
                p.getAula() != null ? p.getAula().getCodigo_aula() : "—"
        );

        lblEquiposDetalle.setText(
                p.getResumenEquipos() != null ? p.getResumenEquipos() : "—"
        );
    }

    // ============================================================
    //                   GESTIONAR DEVOLUCIÓN
    // ============================================================
    @FXML
    private void gestionarDevolucion(javafx.event.ActionEvent e) throws IOException {

        if (prestamoSeleccionado != null) {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com.application.gestiondenotebooks/Devolucion.fxml")
            );
            loader.setControllerFactory(context::getBean);

            Parent root = loader.load();

            DevolucionController controller = loader.getController();
            controller.setPrestamo(prestamoSeleccionado);

            Stage stage = new Stage();
            stage.setTitle("Devolución - Ref: " + prestamoSeleccionado.getNroReferencia());
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.show();

            // Cerrar ventana actual
            Stage actual = (Stage) ((Node) e.getSource()).getScene().getWindow();
            actual.close();
        }
    }

    // ============================================================
    //                   CANCELAR SELECCIÓN
    // ============================================================
    @FXML
    private void cancelarSeleccion() {
        limpiarDetalle();
        btnFinalizar.setVisible(false);
        btnCancelar.setVisible(false);
    }

    private void limpiarDetalle() {
        lblNroReferencia.setText("");
        lblDocente.setText("");
        lblMateria.setText("");
        lblHorario.setText("");
        lblAula.setText("");
        lblEquiposDetalle.setText("");
        listPrestamos.getSelectionModel().clearSelection();
    }

    // ============================================================
    //                    VOLVER A PRINCIPAL (ACTUALIZADO EL ESTADO)
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