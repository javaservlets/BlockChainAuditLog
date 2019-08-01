/*
 * Copyright 2015-2017 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */

package org.forgerock.http.servlet.example;

import static io.swagger.models.Scheme.HTTP;
import static org.forgerock.http.handler.Handlers.chainOf;
import static org.forgerock.http.routing.RouteMatchers.requestUriMatcher;
import static org.forgerock.util.promise.Promises.newResultPromise;

import java.util.Date;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.forgerock.http.ApiProducer;
import org.forgerock.http.DescribedHttpApplication;
import org.forgerock.http.Handler;
import org.forgerock.http.HttpApplication;
import org.forgerock.http.example.DescribedOauth2Endpoint;
import org.forgerock.http.io.Buffer;
import org.forgerock.http.protocol.Response;
import org.forgerock.http.protocol.Status;
import org.forgerock.http.routing.Router;
import org.forgerock.http.routing.RoutingMode;
import org.forgerock.http.routing.UriRouterContext;
import org.forgerock.http.swagger.OpenApiRequestFilter;
import org.forgerock.http.swagger.SwaggerApiProducer;
import org.forgerock.util.Factory;

import io.swagger.models.Info;
import io.swagger.models.Swagger;

/**
 * Example single {@link HttpApplication} deployment which registers a
 * {@link Handler} that returns the application name and matched portion of
 * the request uri.
 *
 * <p>The application name is {@literal default} for single
 * {@code HttpApplication} deployments and can be set for multiple
 * {@code HttpApplication} deployments.</p>
 */
public class ExampleHttpApplication implements DescribedHttpApplication {
    private final String applicationName;

    public ExampleHttpApplication() {
        this("default");
    }

    ExampleHttpApplication(String applicationName) {
        this.applicationName = applicationName;
    }

    @Override
    public Handler start() {
        Router router = new Router();
        router.addRoute(requestUriMatcher(RoutingMode.STARTS_WITH, "oauth2"), new DescribedOauth2Endpoint());
        router.setDefaultRoute((context, request) -> {
            Map<String, String> content = new HashMap<>();
            content.put("applicationName", applicationName);
            content.put("matchedUri", context.asContext(UriRouterContext.class).getBaseUri());
            return newResultPromise(new Response(Status.OK).setEntity(content));
        });

        try {
            Date date = new Date();
            long time = date.getTime();
            Timestamp timestamp = new Timestamp(time);

            BlockChainUtils http_utils = new BlockChainUtils();
            http_utils.postValue("app name: " + applicationName + " " + timestamp.toString());
        } catch (Exception e) {

        }

        return chainOf(router, new OpenApiRequestFilter());
    }

    @Override
    public Factory<Buffer> getBufferFactory() {
        return null;
    }

    @Override
    public void stop() {

    }

    @Override
    public ApiProducer<Swagger> getApiProducer() {
        return new SwaggerApiProducer(new Info().title("Example HTTP Application"), "/servlet", "localhost", HTTP);
    }
}
