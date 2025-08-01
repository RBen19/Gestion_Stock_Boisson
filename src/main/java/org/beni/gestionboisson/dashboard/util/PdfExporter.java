package org.beni.gestionboisson.dashboard.util;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import org.beni.gestionboisson.dashboard.dto.DashboardStatsDTO;
import org.beni.gestionboisson.dashboard.dto.LotAnalyticsDTO;
import org.beni.gestionboisson.dashboard.dto.MouvementAnalyticsDTO;

import java.io.ByteArrayOutputStream;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class PdfExporter {

    public static byte[] createPdfDashboardData(DashboardStatsDTO stats) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());

        document.add(new Paragraph("Rapport de Statistiques").setBold().setFontSize(20));
        document.add(new Paragraph("Généré le: " + formatter.format(Instant.now())));

        // Global Stats
        document.add(new Paragraph("\nStatistiques Globales").setBold().setFontSize(16));
        document.add(new Paragraph("Utilisateurs: " + stats.getTotalUtilisateurs()));
        document.add(new Paragraph("Rôles: " + stats.getTotalRoles()));
        document.add(new Paragraph("Boissons: " + stats.getTotalBoissons()));
        document.add(new Paragraph("Catégories: " + stats.getTotalCategories()));
        document.add(new Paragraph("Unités de Mesure: " + stats.getTotalUnitesDeMesure()));
        document.add(new Paragraph("Fournisseurs: " + stats.getTotalFournisseurs()));
        document.add(new Paragraph("Emplacements: " + stats.getTotalEmplacements()));
        document.add(new Paragraph("Types de Mouvement: " + stats.getTotalTypeMouvements()));
        document.add(new Paragraph("Lots: " + stats.getTotalLots()));
        document.add(new Paragraph("Statuts de Lot: " + stats.getTotalTypeLotStatus()));


        document.close();

        return baos.toByteArray();
    }
}