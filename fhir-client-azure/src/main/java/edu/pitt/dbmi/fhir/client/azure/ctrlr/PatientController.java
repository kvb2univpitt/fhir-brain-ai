/*
 * Copyright (C) 2022 University of Pittsburgh.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package edu.pitt.dbmi.fhir.client.azure.ctrlr;

import ca.uhn.fhir.parser.IParser;
import edu.pitt.dbmi.fhir.client.azure.service.fhir.PatientResourceService;
import org.hl7.fhir.r4.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 *
 * May 20, 2022 10:47:40 AM
 *
 * @author Kevin V. Bui (kvb2univpitt@gmail.com)
 */
@Controller
public class PatientController {

    private final PatientResourceService patientResourceService;
    private final IParser jsonParser;

    @Autowired
    public PatientController(PatientResourceService patientResourceService, IParser jsonParser) {
        this.patientResourceService = patientResourceService;
        this.jsonParser = jsonParser;
    }

    @GetMapping("/fhir/patient/{id}")
    public String showPatientResourceLPage(
            @PathVariable final String id,
            @RegisteredOAuth2AuthorizedClient("azure") final OAuth2AuthorizedClient authorizedClient,
            final Model model) {
        Patient patient = patientResourceService.getPatient(authorizedClient.getAccessToken(), id);

        model.addAttribute("authenName", authorizedClient.getPrincipalName());
        model.addAttribute("patient", patient);

        return "fhir/patient";
    }

    @GetMapping("/fhir/patient")
    public String showPatientResourceListPage(
            @RegisteredOAuth2AuthorizedClient("azure") final OAuth2AuthorizedClient authorizedClient,
            final Model model) {
        model.addAttribute("authenName", authorizedClient.getPrincipalName());

        return "fhir/patients";
    }

}
