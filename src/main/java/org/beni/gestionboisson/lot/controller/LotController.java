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
import org.beni.gestionboisson.lot.exceptions.DuplicateLotNumeroException;
import org.beni.gestionboisson.lot.exceptions.InvalidLotRequestException;
import org.beni.gestionboisson.lot.exceptions.LotCreationException;
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
    //TODO: penser à rajouter une colonne volumeParUnite sur le modèle de Lot qui sera un String résultant
    // de la concaténation du volume donner par l'utilisateur en Double et de l'unite de mesure
    // Ajouter cette colonne à ligneMouvement aussi
    @POST
    @Secured
    public Response createLot(LotDTO lotDTO) {
        String username = TokenContext.getUsername();
        String role = TokenContext.getRole();
        String email = TokenContext.getEmail();
        if (username == null || role == null || email == null) {
            logger.warning("User details not found in token context.");
            return Response.status(Response.Status.UNAUTHORIZED).entity(ApiResponse.error("User authentication details are missing.", Response.Status.UNAUTHORIZED.getStatusCode())).build();
        }

        logger.info("User '" + username + "' (Role: " + role + ", Email: " + email + ") is attempting to create a new lot.");
        lotDTO.setUtilisateurEmail(email);

        try {
            LotResponseDTO createdLot = lotService.createLot(lotDTO);
            logger.info("Successfully created lot with number: " + createdLot.getNumeroLot());
            return Response.status(Response.Status.CREATED).entity(ApiResponse.success(createdLot)).build();
        } catch (InvalidLotRequestException e) {
            logger.log(java.util.logging.Level.WARNING, "Invalid lot request from user '" + username + "': " + e.getMessage(), e);
            return Response.status(Response.Status.BAD_REQUEST).entity(ApiResponse.error(e.getMessage(), Response.Status.BAD_REQUEST.getStatusCode())).build();
        } catch (DuplicateLotNumeroException e) {
            logger.log(java.util.logging.Level.WARNING, "Duplicate lot number detected for user '" + username + "': " + e.getMessage(), e);
            return Response.status(Response.Status.CONFLICT).entity(ApiResponse.error(e.getMessage(), Response.Status.CONFLICT.getStatusCode())).build();
        } catch (LotCreationException e) {
            logger.log(java.util.logging.Level.SEVERE, "Lot persistence error for user '" + username + "': " + e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ApiResponse.error("A server error occurred while creating the lot.", Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())).build();
        } catch (Exception e) {
            logger.log(java.util.logging.Level.SEVERE, "An unexpected error occurred while creating a lot for user '" + username + "': " + e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ApiResponse.error("An unexpected error occurred.", Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())).build();
        }
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
        return Response.ok(ApiResponse.success(lots)).build();
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
        return Response.ok(ApiResponse.success(lots)).build();
    }

    @GET
    @Path("/strategy/lifo")
    @Secured
    public Response findAvailableLotsLifo(@QueryParam("boissonCode") String boissonCode, @QueryParam("uniteDeMesureCode") Optional<String> uniteDeMesureCode) {
        logger.info("Received request for LIFO lots for boisson: " + boissonCode + " and unit: " + uniteDeMesureCode.orElse("N/A"));
        List<LotResponseDTO> lots = lotService.findAvailableLotsLifo(boissonCode, uniteDeMesureCode);
        return Response.ok(ApiResponse.success(lots)).build();
    }

    @GET
    @Path("/strategy/fefo")
    @Secured
    public Response findAvailableLotsFefo(@QueryParam("boissonCode") String boissonCode, @QueryParam("uniteDeMesureCode") Optional<String> uniteDeMesureCode) {
        logger.info("Received request for FEFO lots for boisson: " + boissonCode + " and unit: " + uniteDeMesureCode.orElse("N/A"));
        List<LotResponseDTO> lots = lotService.findAvailableLotsFefo(boissonCode, uniteDeMesureCode);
        return Response.ok(ApiResponse.success(lots)).build();
    }
}
