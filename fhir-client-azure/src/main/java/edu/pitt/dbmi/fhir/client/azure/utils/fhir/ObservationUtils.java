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
package edu.pitt.dbmi.fhir.client.azure.utils.fhir;

import edu.pitt.dbmi.fhir.client.azure.dto.BasicObservationDTO;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Observation;

/**
 *
 * May 29, 2022 5:26:22 AM
 *
 * @author Kevin V. Bui (kvb2univpitt@gmail.com)
 */
public final class ObservationUtils {

    private ObservationUtils() {
    }

    public static List<BasicObservationDTO> getBasicObservationInfo(List<Observation> observations) {
        return observations.stream()
                .map(observation -> {
                    List<String> categories = new LinkedList<>();
                    observation.getCategory().forEach(type -> {
                        type.getCoding().forEach(coding -> {
                            categories.add(coding.getDisplay());
                        });
                    });

                    List<String> codes = new LinkedList<>();
                    CodeableConcept code = observation.getCode();
                    if (code != null) {
                        code.getCoding().forEach(coding -> {
                            codes.add(coding.getDisplay());
                        });
                    }

                    BasicObservationDTO observationDTO = new BasicObservationDTO();
                    observationDTO.setId(observation.getIdElement().getIdPart());
                    observationDTO.setStatus(observation.getStatus().getDisplay());
                    observationDTO.setCategory(categories.stream().collect(Collectors.joining(", ")));
                    observationDTO.setObservation(codes.stream().collect(Collectors.joining(", ")));

                    return observationDTO;
                })
                .collect(Collectors.toList());
    }

}
