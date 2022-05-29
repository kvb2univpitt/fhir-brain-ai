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

import edu.pitt.dbmi.fhir.client.azure.dto.BasicObservationDTO;
import edu.pitt.dbmi.fhir.client.azure.service.fhir.ObservationResourceService;
import edu.pitt.dbmi.fhir.client.azure.utils.fhir.ObservationUtils;
import java.util.List;
import org.hl7.fhir.r4.model.Observation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * May 29, 2022 1:09:03 AM
 *
 * @author Kevin V. Bui (kvb2univpitt@gmail.com)
 */
@RestController
@RequestMapping("/fhir/api/observation")
public class ObservationRestController {

    private final ObservationResourceService observationResourceService;

    @Autowired
    public ObservationRestController(ObservationResourceService observationResourceService) {
        this.observationResourceService = observationResourceService;
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<BasicObservationDTO> getBasicObservations(@RegisteredOAuth2AuthorizedClient("azure") final OAuth2AuthorizedClient authorizedClient) {
        List<Observation> observations = observationResourceService.getObservations(authorizedClient.getAccessToken());

        return ObservationUtils.getBasicObservationInfo(observations);
    }

}
