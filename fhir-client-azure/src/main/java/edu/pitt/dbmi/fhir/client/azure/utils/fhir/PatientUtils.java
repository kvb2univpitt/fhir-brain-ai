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

import edu.pitt.dbmi.fhir.client.azure.dto.BasicPatientDTO;
import java.util.List;
import java.util.stream.Collectors;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Patient;

/**
 *
 * May 25, 2022 11:31:06 AM
 *
 * @author Kevin V. Bui (kvb2univpitt@gmail.com)
 */
public final class PatientUtils {

    private PatientUtils() {
    }

    public static List<Patient> getPatients(Bundle bundle) {
        return bundle.getEntry().stream()
                .map(e -> (Patient) e.getResource())
                .collect(Collectors.toList());
    }

    public static List<BasicPatientDTO> getBasicPatientInfo(Bundle bundle) {
        return bundle.getEntry().stream()
                .map(e -> {
                    Patient patient = (Patient) e.getResource();

                    BasicPatientDTO patientDTO = new BasicPatientDTO();
                    patientDTO.setId(patient.getIdElement().getIdPart());
                    patientDTO.setGender(patient.getGender().getDisplay());

                    patient.getName().stream()
                            .filter(name -> name.getUse() == HumanName.NameUse.OFFICIAL)
                            .forEach(name -> {
                                patientDTO.setLastName(name.getFamily());
                                patientDTO.setFirstName(name.getGivenAsSingleString());
                            });

                    return patientDTO;
                })
                .collect(Collectors.toList());
    }

}
