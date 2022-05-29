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
package edu.pitt.dbmi.fhir.client.azure.ctrlr.rest;

import edu.pitt.dbmi.fhir.client.azure.dto.BasicEncounterDTO;
import edu.pitt.dbmi.fhir.client.azure.service.fhir.EncounterResourceService;
import edu.pitt.dbmi.fhir.client.azure.utils.fhir.EncounterUtils;
import java.util.List;
import org.hl7.fhir.r4.model.Encounter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * May 29, 2022 1:08:49 AM
 *
 * @author Kevin V. Bui (kvb2univpitt@gmail.com)
 */
@RestController
@RequestMapping("/fhir/api/encounter")
public class EncounterRestController {

    private final EncounterResourceService encounterResourceService;

    @Autowired
    public EncounterRestController(EncounterResourceService encounterResourceService) {
        this.encounterResourceService = encounterResourceService;
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<BasicEncounterDTO> getBasicEncounters(@RegisteredOAuth2AuthorizedClient("azure") final OAuth2AuthorizedClient authorizedClient) {
        List<Encounter> encounters = encounterResourceService.getEncounters(authorizedClient.getAccessToken());

        return EncounterUtils.getBasicEncounterInfo(encounters);
    }

}
