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
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.BearerTokenAuthInterceptor;
import edu.pitt.dbmi.fhir.resource.mapper.util.JsonResourceConverterR4;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;

/**
 *
 * May 17, 2022 1:09:59 PM
 *
 * @author Kevin V. Bui (kvb2univpitt@gmail.com)
 */
public class PatientResourceClientTest {

    private final FhirContext fhirContext = FhirContext.forR4();
    private final IParser jsonParser = fhirContext.newJsonParser();

    private final String fhirUrl = "https://brainai-init.fhir.azurehealthcareapis.com";
    private final String accessToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6ImpTMVhvMU9XRGpfNTJ2YndHTmd2UU8yVnpNYyIsImtpZCI6ImpTMVhvMU9XRGpfNTJ2YndHTmd2UU8yVnpNYyJ9.eyJhdWQiOiJodHRwczovL2JyYWluYWktaW5pdC5maGlyLmF6dXJlaGVhbHRoY2FyZWFwaXMuY29tIiwiaXNzIjoiaHR0cHM6Ly9zdHMud2luZG93cy5uZXQvOWVmOWY0ODktZTBhMC00ZWViLTg3Y2MtM2E1MjYxMTJmZDBkLyIsImlhdCI6MTY1NDcxMTA3NywibmJmIjoxNjU0NzExMDc3LCJleHAiOjE2NTQ3MTYyMDcsImFjciI6IjEiLCJhaW8iOiJFMlpnWU9BdmNYaTI5OUhqL2gzK2F0UEtINmF1VWlxVmxQQTlVbTg1Zzlrb2E0OEFWeEVBIiwiYXBwaWQiOiJiYTM4ZjliMi01MTViLTRjNjAtOTVjNS05ZmI4ODM2ZTJkM2YiLCJhcHBpZGFjciI6IjEiLCJmYW1pbHlfbmFtZSI6IkJ1aSIsImdpdmVuX25hbWUiOiJLZXZpbiIsImlwYWRkciI6Ijc2LjEyNS4xODguNjciLCJuYW1lIjoiQnVpLCBLZXZpbiBWYW4iLCJvaWQiOiIyMWExZjc3ZC05ZDljLTQwNjItOGNiZi1mYmM1YzVjZjRiMTEiLCJvbnByZW1fc2lkIjoiUy0xLTUtMjEtMjM2MTk4NDU5Ny0yMDM5NTQ5NzgyLTMxODAyMDQxMTgtNDQ1NzAiLCJwdWlkIjoiMTAwM0JGRkQ4MjMzNTk1QiIsInJoIjoiMC5BVmdBaWZUNW5xRGc2MDZIekRwU1lSTDlEZGg0WjBfdld0eERvZi13YzNKTGxKVllBQVkuIiwic2NwIjoidXNlcl9pbXBlcnNvbmF0aW9uIiwic3ViIjoiY19Cd1E1MV81TmdsdC1LTzYxN0pyQm1FMHB5aVdVZVdpQ19QUU83UEU3TSIsInRpZCI6IjllZjlmNDg5LWUwYTAtNGVlYi04N2NjLTNhNTI2MTEyZmQwZCIsInVuaXF1ZV9uYW1lIjoiS1ZCMkBwaXR0LmVkdSIsInVwbiI6IktWQjJAcGl0dC5lZHUiLCJ1dGkiOiJfOWZTaUlaUWdrV0JjOHpZU05vbUFBIiwidmVyIjoiMS4wIn0.rdd_V7Bg0DB2hFV0iYyXRz5Z6TyT2lVNuKsFMdsnZoqBWVod9UcGi6N44m6IDSclBcZE2rpnkQUbe0On8-LTNO6MQG-4o-Ir9VNycMEyRWUOVn_MIE3iCFZ0NwkGSkE36AcwYDcH4WlHcdRn55UIlhOhTutVNm-g08I0II1LcO7dcK1Fm-q4KdCAzAuK8HeEK7w2g9Br--_gW0NeK_lm976EH4htwD5Mnub9PPUZQ-vDhaPBCmhOicqRi5fwayT8YkTKaquaqjl_BCcy7nbvZfTIqYnrVj3aGtL9oUtiTm0uKiJSMDtRqVElEn1tYzSENSPWzi81n763XK3cjc7aNQ";

    /**
     * Test of main method, of class PatientResourceClient.
     */
    @Test
    public void testMain() {
        PatientResourceClient client = new PatientResourceClient(getClient());

        try {
//            printAddBundleFromJsonFile(client);
//            printAddPatientsFromTsvFile(client);
//            printDeleteBrainAIPatient(client);
//            printSearchPatientByResourceId(client);
//            printGetPatientById(client);
//            printEachPatient(client);
        } catch (Exception exception) {
            exception.printStackTrace(System.err);
        }

    }

    private void printAddBundleFromJsonFile(PatientResourceClient client) {
        Path file = Paths.get(PatientResourceClientTest.class.getResource("/data/synthea/Aaron697_Brekke496_2fa15bc7-8866-461a-9000-f739e425860a.json").getFile());
        System.out.println("--------------------------------------------------------------------------------");
        System.out.printf("Add Resource From JSON Bundle File: %s%n", file.toString());
        System.out.println("--------------------------------------------------------------------------------");

        Bundle bundle = client.addResourceFromJsonBundleFile(file, jsonParser);
        if (bundle != null) {
            jsonParser.setPrettyPrint(true);
            System.out.println(jsonParser.encodeResourceToString(bundle));
        }

        System.out.println();

    }

    private void printAddPatientsFromTsvFile(PatientResourceClient client) {
        Path file = Paths.get(PatientResourceClientTest.class.getResource("/data/brainai/persons.tsv").getFile());
        System.out.println("--------------------------------------------------------------------------------");
        System.out.printf("Add Patients From TSV File: %s%n", file.toString());
        System.out.println("--------------------------------------------------------------------------------");

        Bundle bundle = client.addPatientsFromTsvFile(file);
        if (bundle != null) {
            jsonParser.setPrettyPrint(true);
            System.out.println(jsonParser.encodeResourceToString(bundle));
        }

        System.out.println();
    }

    private void printDeleteBrainAIPatient(PatientResourceClient client) {
        Patient patient = client.getPatient("f0d175f3-02ec-45bb-a1d4-47df16db0f7d");

        System.out.println("--------------------------------------------------------------------------------");
        System.out.printf("Delete Patient: %s (%s)%n",
                patient.getNameFirstRep().getNameAsSingleString(),
                patient.getIdentifierFirstRep().getValue());
        System.out.println("--------------------------------------------------------------------------------");

        client.deletePatient(patient);
        System.out.println();
    }

    private void printSearchPatientByResourceId(PatientResourceClient client) {
        Bundle bundle = client.searchPatient("f0d175f3-02ec-45bb-a1d4-47df16db0f7d");

        Patient[] patients = bundle.getEntry().stream()
                .map(e -> (Patient) e.getResource())
                .collect(Collectors.toList()).toArray(Patient[]::new);

        for (int i = 0; i < patients.length; i++) {
            Patient patient = patients[i];
            System.out.printf("%n---------   Patient %d (%s)   --------%n", i + 1, patient.getNameFirstRep().getNameAsSingleString());
            System.out.println(JsonResourceConverterR4.resourceToJson(patient, true));
        }
        System.out.println();
    }

    private void printGetPatientById(PatientResourceClient client) {
        Patient patient = client.getPatient("f0d175f3-02ec-45bb-a1d4-47df16db0f7d");
        if (patient != null) {
            System.out.printf("%n---------   Patient (%s)   --------%n", patient.getNameFirstRep().getNameAsSingleString());
            System.out.println(JsonResourceConverterR4.resourceToJson(patient, true));
        }
        System.out.println();
    }

    private void printEachPatient(PatientResourceClient client) {
        Bundle bundle = client.getPatients();

        Patient[] patients = bundle.getEntry().stream()
                .map(e -> (Patient) e.getResource())
                .collect(Collectors.toList()).toArray(Patient[]::new);

        for (int i = 0; i < patients.length; i++) {
            Patient patient = patients[i];
            System.out.printf("%n---------   Patient %d (%s)   --------%n", i + 1, patient.getNameFirstRep().getNameAsSingleString());
            System.out.println(JsonResourceConverterR4.resourceToJson(patient, true));
        }
        System.out.println();
    }

    private void printBundle(Bundle bundle, String title) {
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println(title);
        System.out.println("--------------------------------------------------------------------------------");

        jsonParser.setPrettyPrint(true);
        System.out.println(jsonParser.encodeResourceToString(bundle));

        System.out.println("--------------------------------------------------------------------------------");
    }

    private IGenericClient getClient() {
        IGenericClient client = fhirContext.newRestfulGenericClient(fhirUrl);
        client.registerInterceptor(new BearerTokenAuthInterceptor(accessToken));

        return client;
    }

}
