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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class PdfGenerator {

    public static File generarFormularioPrestamo(
            String nroReferencia,
            String docente,
            String catedra,
            String horario,
            String aula,
            List<Integer> listaEquipos
    ) throws IOException {

        String nombreArchivo = "prestamo_" + nroReferencia + ".pdf";
        File pdfFile = new File(nombreArchivo);

        PdfWriter writer = new PdfWriter(pdfFile);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc, PageSize.A4);
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

        for (int i = 0; i <listaEquipos.size() ; i++) {
            tabla.addCell(String.valueOf(listaEquipos.get(i)));
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
        Paragraph firma = new Paragraph("Firma de entrega de los equipos").setTextAlignment(TextAlignment.CENTER)
                .setMarginTop(15);
        document.add(firma);

        // ===== PIE DE PÁGINA =====
        Paragraph footer = new Paragraph(
                "Este formulario debe ser completado por cada estudiante que reciba un equipo y entregado al Área de Sistemas al finalizar la clase.")
                .setFontSize(9)
                .setTextAlignment(TextAlignment.JUSTIFIED)
                .setMarginTop(20);
        document.add(footer);

        document.close();
        return pdfFile;
    }
}

