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

  /*
  *   public static byte[] createPdf(DashboardStatsDTO stats, LotAnalyticsDTO lotAnalytics, MouvementAnalyticsDTO mouvementAnalytics) {
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

        // Lot Analytics
     //   document.add(new Paragraph("\nAnalyse des Lots").setBold().setFontSize(16));
      //  document.add(new Paragraph("Lots créés aujourd'hui: " + lotAnalytics.getLotsCreatedToday()));
       // document.add(new Paragraph("Lots créés cette semaine: " + lotAnalytics.getLotsCreatedThisWeek()));
        //document.add(new Paragraph("Lots créés ce mois-ci: " + lotAnalytics.getLotsCreatedThisMonth()));
        //document.add(new Paragraph("Lots créés cette année: " + lotAnalytics.getLotsCreatedThisYear()));

        // Mouvement Analytics
     //   document.add(new Paragraph("\nAnalyse des Mouvements").setBold().setFontSize(16));
      //  document.add(new Paragraph("Mouvements créés aujourd'hui: " + mouvementAnalytics.getMouvementsCreatedToday()));
       // document.add(new Paragraph("Mouvements créés cette semaine: " + mouvementAnalytics.getMouvementsCreatedThisWeek()));
       // document.add(new Paragraph("Mouvements créés ce mois-ci: " + mouvementAnalytics.getMouvementsCreatedThisMonth()));
       // document.add(new Paragraph("Mouvements créés cette année: " + mouvementAnalytics.getMouvementsCreatedThisYear()));

        document.close();

        return baos.toByteArray();
    }
  *
  * */
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

        // Lot Analytics
        //   document.add(new Paragraph("\nAnalyse des Lots").setBold().setFontSize(16));
        //  document.add(new Paragraph("Lots créés aujourd'hui: " + lotAnalytics.getLotsCreatedToday()));
        // document.add(new Paragraph("Lots créés cette semaine: " + lotAnalytics.getLotsCreatedThisWeek()));
        //document.add(new Paragraph("Lots créés ce mois-ci: " + lotAnalytics.getLotsCreatedThisMonth()));
        //document.add(new Paragraph("Lots créés cette année: " + lotAnalytics.getLotsCreatedThisYear()));

        // Mouvement Analytics
        //   document.add(new Paragraph("\nAnalyse des Mouvements").setBold().setFontSize(16));
        //  document.add(new Paragraph("Mouvements créés aujourd'hui: " + mouvementAnalytics.getMouvementsCreatedToday()));
        // document.add(new Paragraph("Mouvements créés cette semaine: " + mouvementAnalytics.getMouvementsCreatedThisWeek()));
        // document.add(new Paragraph("Mouvements créés ce mois-ci: " + mouvementAnalytics.getMouvementsCreatedThisMonth()));
        // document.add(new Paragraph("Mouvements créés cette année: " + mouvementAnalytics.getMouvementsCreatedThisYear()));

        document.close();

        return baos.toByteArray();
    }
}