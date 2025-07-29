package org.beni.gestionboisson.dashboard.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.beni.gestionboisson.dashboard.dto.DashboardStatsDTO;
import org.beni.gestionboisson.dashboard.service.DashboardService;
import org.beni.gestionboisson.dashboard.util.PdfExporter;

@Path("/dashboard2")
public class DashboardController2 {

    @Inject
    private DashboardService dashboardService;
    @GET
    public String hello() {
        return "Hello, World!";
    }
    @GET
    @Path("/stats")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDashboardStats() {
        return Response.ok(dashboardService.getDashboardStats()).build();
    }
    @GET
    @Path("/export")
    @Produces("application/dashbord/pdf")
    public Response exportPdf() {
        DashboardStatsDTO stats = dashboardService.getDashboardStats();
        //   LotAnalyticsDTO lotAnalytics = dashboardService.getLotAnalytics();
        //  MouvementAnalyticsDTO mouvementAnalytics = dashboardService.getMouvementAnalytics();

        byte[] pdf = PdfExporter.createPdfDashboardData(stats);

        return Response.ok(pdf)
                .header("Content-Disposition", "attachment; filename=dashboard_report.pdf")
                .build();
    }




    /*
    *
    * @GET
    @Path("/lots/summary")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLotAnalytics() {
        return Response.ok(dashboardService.getLotAnalytics()).build();
    }

    @GET
    @Path("/mouvements/summary")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMouvementAnalytics() {
        return Response.ok(dashboardService.getMouvementAnalytics()).build();
    }

    @GET
    @Path("/export")
    @Produces("application/pdf")
    public Response exportPdf() {
        DashboardStatsDTO stats = dashboardService.getDashboardStats();
        LotAnalyticsDTO lotAnalytics = dashboardService.getLotAnalytics();
        MouvementAnalyticsDTO mouvementAnalytics = dashboardService.getMouvementAnalytics();

        byte[] pdf = PdfExporter.createPdf(stats, lotAnalytics, mouvementAnalytics);

        return Response.ok(pdf)
                .header("Content-Disposition", "attachment; filename=dashboard_report.pdf")
                .build();
    }
    * */

}
