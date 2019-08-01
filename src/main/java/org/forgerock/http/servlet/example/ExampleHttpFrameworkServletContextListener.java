/*
 * Copyright 2015-2017 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */

package org.forgerock.http.servlet.example;

import java.util.HashMap;
import java.util.Map;

import org.forgerock.http.HttpApplication;
import org.forgerock.http.servlet.HttpFrameworkServletContextListener;

/**
 * Example implementation of the {@link HttpFrameworkServletContextListener}
 * which registers two {@link HttpApplication}s.
 */
public class ExampleHttpFrameworkServletContextListener extends HttpFrameworkServletContextListener {

    @Override
    protected Map<String, org.forgerock.http.HttpApplication> getHttpApplications() {
        Map<String, org.forgerock.http.HttpApplication> applications = new HashMap<>();
        applications.put("adminApp", new ExampleHttpApplication("adminApp"));
        applications.put("app", new ExampleHttpApplication("app"));
        return applications;
    }
}
