package org.beni.gestionboisson.config;


import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;

@Provider
public class CorsFilter implements ContainerResponseFilter {

    public void filter(ContainerRequestContext requestContext) throws IOException {
        // Gérer les requêtes préflight OPTIONS
        if (requestContext.getMethod().equalsIgnoreCase("OPTIONS")) {
            requestContext.abortWith(
                    jakarta.ws.rs.core.Response.ok()
                            .header("Access-Control-Allow-Origin", "http://localhost:5173")
                            .header("Access-Control-Allow-Origin", "https://statuesque-crisp-80ec76.netlify.app")
                            .header("Access-Control-Allow-Credentials", "true")
                            .header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization, x-refresh-token")
                            .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD,PATCH")
                            .build()
            );
        }
    }


    @Override
    public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext) throws IOException {
        containerResponseContext.getHeaders().add("Access-Control-Allow-Origin", "http://localhost:5173");
        containerResponseContext.getHeaders().add("Access-Control-Allow-Origin", "https://statuesque-crisp-80ec76.netlify.app");
        containerResponseContext.getHeaders().add("Access-Control-Allow-Credentials", "true");
        containerResponseContext.getHeaders().add("Access-Control-Allow-Headers", "origin, content-type, accept, authorization,x-refresh-token");
        containerResponseContext.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD,PATCH");
    }
}