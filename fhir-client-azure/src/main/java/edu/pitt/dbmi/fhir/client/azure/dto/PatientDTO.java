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
package edu.pitt.dbmi.fhir.client.azure.dto;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Patient;

/**
 *
 * May 25, 2022 11:35:24 AM
 *
 * @author Kevin V. Bui (kvb2univpitt@gmail.com)
 */
public class PatientDTO {

    private final Patient patient;

    public PatientDTO(Patient patient) {
        this.patient = patient;
    }

    public String getOfficialName() {
        if (patient == null) {
            return "No patient found.";
        }

        return patient.getName().stream()
                .filter(name -> name.getUse() == HumanName.NameUse.OFFICIAL)
                .map(name -> String.format("%s %s", name.getGivenAsSingleString(), name.getFamily()))
                .collect(Collectors.joining(", "))
                .trim();
    }

    public boolean hasPatient() {
        return (patient != null);
    }

    public List<Name> getNames() {
        if (patient == null) {
            return Collections.EMPTY_LIST;
        }

        patient.getAddress().forEach(address -> {
            address.getCity();
            address.getCountry();
            address.getState();
            address.getPostalCode();
            address.getText();
            address.getUse().getDisplay();
        });

        return patient.getName().stream()
                .map(name -> new Name(name.getUse().getDisplay(), name.getGivenAsSingleString(), name.getFamily()))
                .collect(Collectors.toList());
    }

    public class Identifier {

    }

    public class Name {

        public String type;
        public String first;
        public String last;

        public Name(String type, String first, String last) {
            this.type = type;
            this.first = first;
            this.last = last;
        }

    }

}
