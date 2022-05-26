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
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.HumanName;

/**
 *
 * May 26, 2022 1:13:24 AM
 *
 * @author Kevin V. Bui (kvb2univpitt@gmail.com)
 */
public class PatientHelper {

    private final ObjectMapper objectMapper;

    public PatientHelper() {
        this.objectMapper = new ObjectMapper();
    }

    public String getCodingAsJson(List<Coding> codings) {
        List<Code> codes = codings.stream()
                .map(code -> new Code(code.getSystem(), code.getCode(), code.getDisplay()))
                .collect(Collectors.toList());

        try {
            return objectMapper.writeValueAsString(codes);
        } catch (JsonProcessingException exception) {
            return exception.getLocalizedMessage();
        }
    }

    public String getOfficialName(List<HumanName> humanNames) {
        return humanNames.stream()
                .filter(name -> name.getUse() == HumanName.NameUse.OFFICIAL)
                .map(name -> String.format("%s %s", name.getGivenAsSingleString(), name.getFamily()))
                .collect(Collectors.joining(", "))
                .trim();
    }

    public class Code implements Serializable {

        private static final long serialVersionUID = -8464234990587439755L;

        private String system;
        private String code;
        private String display;

        public Code() {
        }

        public Code(String system, String code, String display) {
            this.system = system;
            this.code = code;
            this.display = display;
        }

        public String getDisplay() {
            return display;
        }

        public void setDisplay(String display) {
            this.display = display;
        }

        public String getSystem() {
            return system;
        }

        public void setSystem(String system) {
            this.system = system;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

    }

}
