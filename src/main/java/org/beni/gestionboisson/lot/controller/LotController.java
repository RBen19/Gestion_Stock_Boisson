package org.beni.gestionboisson.lot.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.beni.gestionboisson.auth.security.Secured;
import org.beni.gestionboisson.auth.security.TokenContext;
import org.beni.gestionboisson.lot.dto.LotDTO;
import org.beni.gestionboisson.lot.dto.LotResponseDTO;
import org.beni.gestionboisson.lot.dto.LotSearchStrategyDTO;
import org.beni.gestionboisson.lot.dto.LotStatusUpdateDTO;
import org.beni.gestionboisson.lot.service.LotService;
import org.beni.gestionboisson.shared.response.ApiResponse;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Path("/lots")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LotController {

    private static final Logger logger = Logger.getLogger(LotController.class.getName());

    @Inject
    private LotService lotService;

    @POST
    @Secured
    public Response createLot(LotDTO lotDTO) {
        String username = TokenContext.getUsername();
        String role = TokenContext.getRole();
        String email = TokenContext.getEmail();
        if(username == null || role == null || email == null){
            logger.warning("User with username : "+username+" with role has : "+ role + " and email "+  email +"  requested to create a new lot.");
            return Response.status(Response.Status.NOT_FOUND).entity(ApiResponse.error("User not found", Response.Status.NOT_FOUND.getStatusCode())).build();
        }


        logger.info("User with username : "+username+" with role has : "+ role + " and email "+  email +"  requested to create a new lot.");
        lotDTO.setUtilisateurEmail(email);
        LotResponseDTO createdLot = lotService.createLot(lotDTO);
        return Response.status(Response.Status.CREATED).entity(ApiResponse.success(createdLot)).build();
    }

    @GET
    @Path("/{id}")
    @Secured
    public Response getLotById(@PathParam("id") Long id) {
        logger.info("Received request to get lot by ID: " + id);
        LotResponseDTO lot = lotService.getLotById(id);
        return Response.ok(lot).build();
    }

    @GET
    @Secured
    public Response getAllLots() {
        logger.info("Received request to get all lots.");
        List<LotResponseDTO> lots = lotService.getAllLots();
        return Response.ok(lots).build();
    }

    @PUT
    @Path("/{id}")
    @Secured
    public Response updateLot(@PathParam("id") Long id, LotDTO lotDTO) {
        logger.info("Received request to update lot with ID: " + id);
        LotResponseDTO updatedLot = lotService.updateLot(id, lotDTO);
        return Response.ok( updatedLot).build();
    }

    @DELETE
    @Path("/{id}")
    @Secured
    public Response deleteLot(@PathParam("id") Long id) {
        logger.info("Received request to delete lot with ID: " + id);
        lotService.deleteLot(id);
        return Response.noContent().entity( "Lot deleted successfully").build();
    }

    @POST
    @Path("/validate-quantity")
    @Secured
    public Response validateQuantity(@QueryParam("boissonCode") String boissonCode, @QueryParam("requestedQty") Double requestedQty) {
        logger.info("Received request to validate quantity for boisson: " + boissonCode + " with requested quantity: " + requestedQty);
        lotService.validateAvailableQuantity(boissonCode, requestedQty);
        return Response.ok( "Quantity validated successful").build();
    }

    @PUT
    @Path("/{id}/status")
    @Secured
    public Response changeLotStatus(@PathParam("id") Long lotId, LotStatusUpdateDTO statusUpdateDTO) {
        logger.info("Received request to change status for lot ID: " + lotId + " to: " + statusUpdateDTO.getNewStatusLibelle());
        LotResponseDTO updatedLot = lotService.changeLotStatus(lotId, statusUpdateDTO);
        return Response.ok(updatedLot).build();
    }

    @GET
    @Path("/strategy/fifo")
    @Secured
    //GET /lot/available/fifo?boissonCode=COCA123&uniteDeMesureCode=CAN
    public Response findAvailableLotsFifo(@QueryParam("boissonCode") String boissonCode, @QueryParam("uniteDeMesureCode") Optional<String> uniteDeMesureCode) {
        logger.info("Received request for FIFO lots for boisson: " + boissonCode + " and unit: " + uniteDeMesureCode.orElse("N/A"));
        List<LotResponseDTO> lots = lotService.findAvailableLotsFifo(boissonCode, uniteDeMesureCode);
        return Response.ok(lots).build();
    }

    @GET
    @Path("/strategy/lifo")
    @Secured
    public Response findAvailableLotsLifo(@QueryParam("boissonCode") String boissonCode, @QueryParam("uniteDeMesureCode") Optional<String> uniteDeMesureCode) {
        logger.info("Received request for LIFO lots for boisson: " + boissonCode + " and unit: " + uniteDeMesureCode.orElse("N/A"));
        List<LotResponseDTO> lots = lotService.findAvailableLotsLifo(boissonCode, uniteDeMesureCode);
        return Response.ok(lots).build();
    }

    @GET
    @Path("/strategy/fefo")
    @Secured
    public Response findAvailableLotsFefo(@QueryParam("boissonCode") String boissonCode, @QueryParam("uniteDeMesureCode") Optional<String> uniteDeMesureCode) {
        logger.info("Received request for FEFO lots for boisson: " + boissonCode + " and unit: " + uniteDeMesureCode.orElse("N/A"));
        List<LotResponseDTO> lots = lotService.findAvailableLotsFefo(boissonCode, uniteDeMesureCode);
        return Response.ok(lots).build();
    }
}
