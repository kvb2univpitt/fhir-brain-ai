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

import edu.pitt.dbmi.fhir.client.azure.dto.BasicEncounterDTO;
import edu.pitt.dbmi.fhir.client.azure.utils.DateFormatters;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.hl7.fhir.r4.model.Encounter;

/**
 *
 * May 29, 2022 1:29:53 AM
 *
 * @author Kevin V. Bui (kvb2univpitt@gmail.com)
 */
public final class EncounterUtils {

    private EncounterUtils() {
    }

    public static List<BasicEncounterDTO> getBasicEncounterInfo(List<Encounter> encounters) {
        return encounters.stream()
                .map(encounter -> {
                    List<String> types = new LinkedList<>();
                    encounter.getType().forEach(type -> {
                        type.getCoding().forEach(coding -> {
                            types.add(coding.getDisplay());
                        });
                    });

                    BasicEncounterDTO encounterDTO = new BasicEncounterDTO();
                    encounterDTO.setId(encounter.getIdElement().getIdPart());
                    encounterDTO.setEnd(DateFormatters.formatToMonthDayYearHourMinute(encounter.getPeriod().getEnd()));
                    encounterDTO.setStart(DateFormatters.formatToMonthDayYearHourMinute(encounter.getPeriod().getStart()));
                    encounterDTO.setStatus(encounter.getStatus().getDisplay());
                    encounterDTO.setType(types.stream().collect(Collectors.joining(", ")));

                    return encounterDTO;
                })
                .collect(Collectors.toList());
    }

}
