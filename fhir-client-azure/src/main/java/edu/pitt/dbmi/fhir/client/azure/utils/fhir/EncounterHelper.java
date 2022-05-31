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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.pitt.dbmi.fhir.client.azure.utils.DateFormatters;
import edu.pitt.dbmi.fhir.client.azure.utils.fhir.model.Code;
import edu.pitt.dbmi.fhir.client.azure.utils.fhir.model.Type;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;

/**
 *
 * May 30, 2022 7:50:36 PM
 *
 * @author Kevin V. Bui (kvb2univpitt@gmail.com)
 */
public class EncounterHelper {

    private final ObjectMapper objectMapper;

    public EncounterHelper() {
        this.objectMapper = new ObjectMapper();
    }

    public String getClassAsJson(Coding coding) {
        List<Type> customTypes = Collections.singletonList(
                new Type(coding.getCode(), Collections.singletonList(
                        new Code(coding.getSystem(), coding.getCode(), (coding.getDisplay() == null) ? "" : coding.getDisplay()))));

        try {
            return objectMapper.writeValueAsString(customTypes);
        } catch (JsonProcessingException exception) {
            return exception.getLocalizedMessage();
        }
    }

    public String getTypeAsJson(List<CodeableConcept> types) {
        List<Type> customTypes = types.stream()
                .map(type -> new Type(type.getText(), codingToCode(type.getCoding())))
                .collect(Collectors.toList());

        try {
            return objectMapper.writeValueAsString(customTypes);
        } catch (JsonProcessingException exception) {
            return exception.getLocalizedMessage();
        }
    }

    public String formatDateTime(Date date) {
        return DateFormatters.formatToMonthDayYearHourMinute(date);
    }

    public String displayTypes(List<CodeableConcept> types) {
        return types.stream()
                .map(type -> type.getText())
                .collect(Collectors.joining(", "));
    }

    private List<Code> codingToCode(List<Coding> codings) {
        return codings.stream()
                .map(coding -> new Code(coding.getSystem(), coding.getCode(), coding.getDisplay()))
                .collect(Collectors.toList());
    }

}
