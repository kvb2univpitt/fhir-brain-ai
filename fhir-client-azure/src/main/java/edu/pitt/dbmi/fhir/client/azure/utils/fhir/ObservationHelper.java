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
import edu.pitt.dbmi.fhir.client.azure.utils.fhir.model.Category;
import edu.pitt.dbmi.fhir.client.azure.utils.fhir.model.Code;
import edu.pitt.dbmi.fhir.client.azure.utils.fhir.model.Type;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.hl7.fhir.r4.model.CodeableConcept;

/**
 *
 * May 31, 2022 12:28:06 PM
 *
 * @author Kevin V. Bui (kvb2univpitt@gmail.com)
 */
public class ObservationHelper {

    private final ObjectMapper objectMapper;

    public ObservationHelper() {
        this.objectMapper = new ObjectMapper();
    }

    public String getCodeAsJson(CodeableConcept codeableConcept) {
        List<Code> codes = codeableConcept.getCoding().stream()
                .map(coding -> new Code(coding.getSystem(), coding.getCode(), coding.getDisplay()))
                .collect(Collectors.toList());

        List<Type> categories = Collections.singletonList(new Type(codeableConcept.getText(), codes));

        try {
            return objectMapper.writeValueAsString(categories);
        } catch (JsonProcessingException exception) {
            return exception.getLocalizedMessage();
        }
    }

    public String getCategoryAsJson(List<CodeableConcept> codeableConcepts) {
        List<Category> categories = new LinkedList<>();
        codeableConcepts.forEach(category -> {
            List<Code> codes = category.getCoding().stream()
                    .map(coding -> new Code(coding.getSystem(), coding.getCode(), coding.getDisplay()))
                    .collect(Collectors.toList());
            categories.add(new Category(codes));
        });

        try {
            return objectMapper.writeValueAsString(categories);
        } catch (JsonProcessingException exception) {
            return exception.getLocalizedMessage();
        }
    }

    public String getCategoryDisplay(List<CodeableConcept> codeableConcepts) {
        List<String> values = new LinkedList<>();
        codeableConcepts.forEach(codeableConcept -> {
            codeableConcept.getCoding().stream()
                    .map(coding -> coding.getDisplay())
                    .forEach(values::add);
        });

        return values.stream().collect(Collectors.joining(", "));
    }

}
