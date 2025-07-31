package org.beni.gestionboisson.config;


import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Provider
public class CorsFilter   implements ContainerRequestFilter, ContainerResponseFilter {
/*
*     private static final List<String> allowedOrigins = Arrays.asList(
            "http://localhost:5173",
            "https://statuesque-crisp-80ec76.netlify.app"
    );

    public void filter(ContainerRequestContext requestContext) throws IOException {
        // Gérer les requêtes préflight OPTIONS
        String origin = requestContext.getHeaderString("Origin");
        if (requestContext.getMethod().equalsIgnoreCase("OPTIONS")  && allowedOrigins.contains(origin)) {
            requestContext.abortWith(
                    jakarta.ws.rs.core.Response.ok()
                            .header("Access-Control-Allow-Origin", origin)
                          //  .header("Access-Control-Allow-Origin", "https://statuesque-crisp-80ec76.netlify.app")
                            .header("Access-Control-Allow-Credentials", "true")
                            .header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization, x-refresh-token")
                            .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD,PATCH")
                            .build()
            );
        }
    }


    @Override
    public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext) throws IOException {
        String origin = containerRequestContext.getHeaderString("Origin");

        if (origin != null && allowedOrigins.contains(origin)) {
            containerResponseContext.getHeaders().add("Access-Control-Allow-Credentials", "true");
            containerResponseContext.getHeaders().add("Access-Control-Allow-Headers", "origin, content-type, accept, authorization,x-refresh-token");
            containerResponseContext.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD,PATCH");
            containerResponseContext.getHeaders().add("Access-Control-Allow-Origin", origin);
            //responseContext.getHeaders().add("Access-Control-Allow-Credentials", "true");

        }
      //  containerResponseContext.getHeaders().add("Access-Control-Allow-Origin", "http://localhost:5173");
     //   containerResponseContext.getHeaders().add("Access-Control-Allow-Origin", "https://statuesque-crisp-80ec76.netlify.app");

    }
*
* */
private static final List<String> allowedOrigins = Arrays.asList(
        "http://localhost:5173",
        "https://statuesque-crisp-80ec76.netlify.app"
);

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String origin = requestContext.getHeaderString("Origin");

        if (origin != null && allowedOrigins.contains(origin)
                && "OPTIONS".equalsIgnoreCase(requestContext.getMethod())) {

            requestContext.abortWith(
                    jakarta.ws.rs.core.Response.ok()
                            .header("Access-Control-Allow-Origin", origin)
                            .header("Access-Control-Allow-Credentials", "true")
                            .header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization, x-refresh-token")
                            .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD, PATCH")
                            .build()
            );
        }
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        String origin = requestContext.getHeaderString("Origin");

        if (origin != null && allowedOrigins.contains(origin)) {
            responseContext.getHeaders().add("Access-Control-Allow-Origin", origin);
            responseContext.getHeaders().add("Access-Control-Allow-Credentials", "true");
            responseContext.getHeaders().add("Access-Control-Allow-Headers", "origin, content-type, accept, authorization, x-refresh-token");
            responseContext.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD, PATCH");
        }
    }
}