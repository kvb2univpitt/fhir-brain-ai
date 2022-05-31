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
package edu.pitt.dbmi.fhir.client.azure.conf;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import edu.pitt.dbmi.fhir.client.azure.utils.fhir.EncounterHelper;
import edu.pitt.dbmi.fhir.client.azure.utils.fhir.ObservationHelper;
import edu.pitt.dbmi.fhir.client.azure.utils.fhir.PatientHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * May 10, 2022 8:36:55 PM
 *
 * @author Kevin V. Bui (kvb2univpitt@gmail.com)
 */
@Configuration
public class AppConfig {

    @Bean
    public FhirContext fhirContext() {
        return FhirContext.forR4();
    }

    @Bean
    public IParser jsonParser(FhirContext fhirContext) {
        return fhirContext.newJsonParser();
    }

    @Bean
    public PatientHelper patientHelper() {
        return new PatientHelper();
    }

    @Bean
    public EncounterHelper encounterHelper() {
        return new EncounterHelper();
    }

    @Bean
    public ObservationHelper observationHelper() {
        return new ObservationHelper();
    }

}
