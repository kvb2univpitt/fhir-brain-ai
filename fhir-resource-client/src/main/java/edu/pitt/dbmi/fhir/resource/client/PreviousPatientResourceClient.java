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
package edu.pitt.dbmi.fhir.resource.client;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.CacheControlDirective;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.BearerTokenAuthInterceptor;
import ca.uhn.fhir.rest.gclient.IDelete;
import edu.pitt.dbmi.fhir.resource.mapper.r4.brainai.PatientResourceMapper;
import edu.pitt.dbmi.fhir.resource.mapper.util.Delimiters;
import edu.pitt.dbmi.fhir.resource.mapper.util.JsonResourceConverterR4;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Patient;

/**
 *
 * May 17, 2022 11:02:53 AM
 *
 * @author Kevin V. Bui (kvb2univpitt@gmail.com)
 */
public class PreviousPatientResourceClient {

    public static final FhirContext FHIR_CONTEXT = FhirContext.forR4();
    private static final IParser PARSER = FHIR_CONTEXT.newJsonParser();

    public static final String FHIR_URL = "https://brainai-init.fhir.azurehealthcareapis.com";
    public static final String ACCESS_TOKEN = "";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        IGenericClient client = FHIR_CONTEXT.newRestfulGenericClient(FHIR_URL);
        client.registerInterceptor(new BearerTokenAuthInterceptor(ACCESS_TOKEN));

//        showPatients(client);
//        addPatients(client);
//        deletePatients(client, "010cbf98-d5f4-11ec-9d64-0242ac120002");
//        showPatientIdentifiers(client);
//        addSyntheaPatient(client);
        addSMultipleyntheaPatients(client);
    }

    private static void addSMultipleyntheaPatients(IGenericClient client) {
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("Add Multiple Synthea Patients");
        System.out.println("--------------------------------------------------------------------------------");

        Path dir = Paths.get("/home", "kvb2", "Downloads", "synthea", "fhir");
        try {
            List<Path> sortedFiles = listFiles(dir).stream().sorted().collect(Collectors.toList());
            sortedFiles.forEach(file -> addSyntheaPatient(client, file));
        } catch (IOException exception) {
            exception.printStackTrace(System.err);
        }

        System.out.println();
    }

    private static void addSyntheaPatient(IGenericClient client, Path file) {
        System.out.println(file);
        try ( BufferedReader reader = Files.newBufferedReader(file, Charset.defaultCharset())) {
            Bundle bundle = (Bundle) PARSER.parseResource(reader);
            Bundle patientBundle = client.transaction().withBundle(bundle).execute();
        } catch (IOException exception) {
            exception.printStackTrace(System.err);
        }
    }

    private static List<Path> listFiles(Path dir) throws IOException {
        try ( Stream<Path> stream = Files.walk(dir)) {
            return stream.map(Path::normalize)
                    .filter(Files::isRegularFile)
                    .collect(Collectors.toList());

        }
    }

    private static void addSyntheaPatient(IGenericClient client) {
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("Add Synthea Patient");
        System.out.println("--------------------------------------------------------------------------------");

        Path file = Paths.get(PatientResourceClient.class.getResource("/data/synthea/fhir_r4/patient_1.json").getFile());
        try ( BufferedReader reader = Files.newBufferedReader(file, Charset.defaultCharset())) {
            Bundle bundle = (Bundle) PARSER.parseResource(reader);
            Bundle patientBundle = client.transaction().withBundle(bundle).execute();
            System.out.println(JsonResourceConverterR4.resourceToJson(patientBundle, true));
        } catch (IOException exception) {
            exception.printStackTrace(System.err);
        }

        System.out.println();
    }

    private static void deletePatients(IGenericClient client, String patientId) {
        System.out.println("--------------------------------------------------------------------------------");
        System.out.printf("Delete Patient: %s%n", patientId);
        System.out.println("--------------------------------------------------------------------------------");

        Bundle patientBundle = getPatients(client);
        patientBundle.getEntry().stream()
                .map(e -> (Patient) e.getResource())
                .forEach(patient -> {
                    Identifier id = patient.getIdentifierFirstRep();
                    if (id.getValue() != null) {
                        if (patientId.compareTo(id.getValue()) == 0) {
                            try {
                                IDelete delete = client.delete();
                                MethodOutcome outcome = delete.resource(patient).execute();
                                System.out.printf("Patient (%s) has been removed.", outcome.getId().getValue());
                            } catch (Exception exception) {
                                exception.printStackTrace(System.err);
                            }
                        }
                    }
                });

        System.out.println();
    }

    private static void showPatientIdentifiers(IGenericClient client) {
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("Patient's Identifiers");
        System.out.println("--------------------------------------------------------------------------------");

        Bundle bundle = getPatients(client);
        bundle.getEntry().stream()
                .map(e -> (Patient) e.getResource())
                .forEach(patient -> {
                    System.out.println(patient.getIdentifierFirstRep().getValue());
                });
        System.out.println();
    }

    private static void showPatients(IGenericClient client) {
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("Patients");
        System.out.println("--------------------------------------------------------------------------------");

        Bundle bundle = getPatients(client);
        Patient[] patients = bundle.getEntry().stream()
                .map(e -> (Patient) e.getResource())
                .collect(Collectors.toList()).toArray(new Patient[0]);
        for (int i = 0; i < patients.length; i++) {
            System.out.printf("%n---------   Patient %d   --------%n", i + 1);
            System.out.println(JsonResourceConverterR4.resourceToJson(patients[i], true));
        }

        System.out.println();
    }

    private static void addPatients(IGenericClient client) {
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("Add Patients");
        System.out.println("--------------------------------------------------------------------------------");

        Bundle bundle = new Bundle();
        bundle.setType(Bundle.BundleType.TRANSACTION);

        Path file = Paths.get(PatientResourceClient.class.getResource("/data/brainai/persons.tsv").getFile());
        Map<String, Patient> patients = PatientResourceMapper.getPatients(file, Delimiters.TAB_DELIM);
        patients.values().forEach(patient -> {
            bundle.addEntry()
                    .setFullUrl(patient.getIdElement().getValue())
                    .setResource(patient)
                    .getRequest()
                    .setUrl("Patient")
                    .setMethod(Bundle.HTTPVerb.POST);
        });

        try {
            Bundle patientBundle = client.transaction().withBundle(bundle).execute();
            System.out.println(JsonResourceConverterR4.resourceToJson(patientBundle, true));
        } catch (Exception exception) {
            exception.printStackTrace(System.err);
        }
        System.out.println();
    }

    private static Bundle getPatients(IGenericClient client) {
        return client
                .search()
                .forResource(Patient.class)
                .returnBundle(Bundle.class)
                .cacheControl(new CacheControlDirective().setNoCache(true))
                .execute();
    }

}
