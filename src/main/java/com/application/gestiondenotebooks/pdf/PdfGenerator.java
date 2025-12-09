package com.application.gestiondenotebooks.pdf;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PdfGenerator {

    /**
     * Genera el formulario de préstamo en una carpeta segura del usuario,
     * por ejemplo: C:\Users\Usuario\GestionNotebooks\prestamos\prestamo_XXX.pdf
     */
    public static File generarFormularioPrestamo(
            String nroReferencia,
            String docente,
            String catedra,
            String horario,
            String aula,
            List<Integer> listaEquipos
    ) throws IOException {

        // Carpeta base donde se van a ir guardando los PDF
        Path baseDir = Paths.get(System.getProperty("user.home"),
                "GestionNotebooks", "prestamos");

        // Crea la carpeta si no existe (no falla si ya está creada)
        Files.createDirectories(baseDir);

        // Nombre de archivo y ruta absoluta
        String nombreArchivo = "prestamo_" + nroReferencia + ".pdf";
        Path pdfPath = baseDir.resolve(nombreArchivo);

        // Crear el PDF (try-with-resources para cerrar todo bien)
        try (PdfWriter writer = new PdfWriter(pdfPath.toString());
             PdfDocument pdfDoc = new PdfDocument(writer);
             Document document = new Document(pdfDoc, PageSize.A4)) {

            document.setMargins(50, 50, 50, 50);

            // ===== ENCABEZADO =====
            Paragraph titulo = new Paragraph("Universidad CAECE")
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(14);
            document.add(titulo);

            Paragraph subtitulo = new Paragraph("Registro de Uso - Notebooks en Préstamo")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20);
            document.add(subtitulo);

            // ===== DATOS PRINCIPALES =====
            Table datos = new Table(new float[]{150, 300});
            datos.setWidth(450);

            datos.addCell("Número de Referencia:");
            datos.addCell("REF-" + nroReferencia);

            datos.addCell("Fecha:");
            datos.addCell(LocalDate.now().format(DateTimeFormatter.ofPattern("d/M/yyyy")));

            datos.addCell("Docente:");
            datos.addCell(docente);

            datos.addCell("Cátedra:");
            datos.addCell(catedra);

            datos.addCell("Horario:");
            datos.addCell(horario);

            datos.addCell("Aula:");
            datos.addCell(aula);

            document.add(datos);
            document.add(new Paragraph("\n"));

            // ===== TABLA DE EQUIPOS =====
            Paragraph tituloEquipos = new Paragraph("Distribución de equipos")
                    .setBold()
                    .setMarginBottom(5);
            document.add(tituloEquipos);

            Table tabla = new Table(new float[]{60, 100, 100, 80, 100});
            tabla.setWidth(440);

            tabla.addHeaderCell("N° Equipo");
            tabla.addHeaderCell("Nombre");
            tabla.addHeaderCell("Apellido");
            tabla.addHeaderCell("DNI");
            tabla.addHeaderCell("Firma");

            for (Integer nroEquipo : listaEquipos) {
                tabla.addCell(String.valueOf(nroEquipo));
                tabla.addCell("");
                tabla.addCell("");
                tabla.addCell("");
                tabla.addCell("");
            }

            document.add(tabla);
            document.add(new Paragraph("\n"));

            // ===== SECCIÓN DEVOLUCIÓN =====
            Paragraph devolucion = new Paragraph("Devolución de equipos")
                    .setBold()
                    .setMarginTop(10)
                    .setMarginBottom(5);
            document.add(devolucion);

            Paragraph detalles = new Paragraph("Detalles observados en los equipos (de uso o inconvenientes):")
                    .setMarginBottom(5);
            document.add(detalles);

            // Líneas para escribir
            for (int i = 0; i < 4; i++) {
                LineSeparator ls = new LineSeparator(new SolidLine());
                ls.setMarginBottom(25);
                ls.setMarginTop(25);
                document.add(ls);
            }

            // ===== FIRMA =====
            Paragraph firma = new Paragraph("Firma de entrega de los equipos")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(15);
            document.add(firma);

            // ===== PIE DE PÁGINA =====
            Paragraph footer = new Paragraph(
                    "Este formulario debe ser completado por cada estudiante que reciba un equipo "
                            + "y entregado al Área de Sistemas al finalizar la clase.")
                    .setFontSize(9)
                    .setTextAlignment(TextAlignment.JUSTIFIED)
                    .setMarginTop(20);
            document.add(footer);
        }

        // Devolvemos el File con ruta absoluta
        return pdfPath.toFile();
    }
}

