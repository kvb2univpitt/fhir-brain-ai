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

import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.CacheControlDirective;
import ca.uhn.fhir.rest.api.DeleteCascadeModeEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import edu.pitt.dbmi.fhir.resource.mapper.r4.brainai.EncounterResourceMapper;
import edu.pitt.dbmi.fhir.resource.mapper.r4.brainai.PatientResourceMapper;
import edu.pitt.dbmi.fhir.resource.mapper.util.Delimiters;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;

/**
 *
 * May 17, 2022 11:02:53 AM
 *
 * @author Kevin V. Bui (kvb2univpitt@gmail.com)
 */
public class PatientResourceClient {

    private final IGenericClient client;

    public PatientResourceClient(IGenericClient client) {
        this.client = client;
    }

    public void deleteAll(Path jsonFile, IParser jsonParser) {
        try ( BufferedReader reader = Files.newBufferedReader(jsonFile, Charset.defaultCharset())) {
            Bundle bundle = (Bundle) jsonParser.parseResource(reader);
            bundle.getEntry().forEach(e -> {
                Bundle searchBundle = client
                        .search()
                        .forResource(e.getResource().getClass())
                        .returnBundle(Bundle.class)
                        .cacheControl(new CacheControlDirective().setNoCache(true))
                        .execute();

                Bundle deleteBundle = new Bundle();
                deleteBundle.setType(Bundle.BundleType.TRANSACTION);

                searchBundle.getEntry()
                        .forEach(resource -> deleteBundle.addEntry().getRequest().setUrl(resource.getFullUrl()).setMethod(Bundle.HTTPVerb.DELETE));
                while (searchBundle.getLink(IBaseBundle.LINK_NEXT) != null) {
                    searchBundle = client.loadPage().next(searchBundle).execute();

                    searchBundle.getEntry()
                            .forEach(resource -> deleteBundle.addEntry().getRequest().setUrl(resource.getFullUrl()).setMethod(Bundle.HTTPVerb.DELETE));
                }

                client.transaction().withBundle(deleteBundle).execute();
            });
        } catch (IOException exception) {
            exception.printStackTrace(System.err);
        }
    }

    public Bundle addResourceFromJsonBundleFile(Path jsonFile, IParser jsonParser) {
        try ( BufferedReader reader = Files.newBufferedReader(jsonFile, Charset.defaultCharset())) {
            Bundle bundle = (Bundle) jsonParser.parseResource(reader);

            return client.transaction().withBundle(bundle).execute();
        } catch (IOException exception) {
            exception.printStackTrace(System.err);

            return null;
        }
    }

    public Bundle addEncountersFromTsvFile(Path patientTsvFile, Path encounterTsvFile) {
        Bundle bundle = new Bundle();
        bundle.setType(Bundle.BundleType.TRANSACTION);

        Map<String, Patient> patients = PatientResourceMapper.getPatients(patientTsvFile, Delimiters.TAB_DELIM);
        Map<String, Encounter> encounters = EncounterResourceMapper.getEncountersFromFile(encounterTsvFile, Delimiters.TAB_DELIM, patients);
        encounters.values().forEach(encounter -> {
            bundle.addEntry()
                    .setFullUrl(encounter.getIdElement().getValue())
                    .setResource(encounter)
                    .getRequest()
                    .setUrl("Encounter")
                    .setMethod(Bundle.HTTPVerb.POST);
        });

        return client.transaction().withBundle(bundle).execute();
    }

    public Bundle addPatientsFromTsvFile(Path tsvFile) {
        Bundle bundle = new Bundle();
        bundle.setType(Bundle.BundleType.TRANSACTION);

        Map<String, Patient> patients = PatientResourceMapper.getPatients(tsvFile, Delimiters.TAB_DELIM);
        patients.values().forEach(patient -> {
            bundle.addEntry()
                    .setFullUrl(patient.getIdElement().getValue())
                    .setResource(patient)
                    .getRequest()
                    .setUrl("Patient")
                    .setMethod(Bundle.HTTPVerb.POST);
        });

        return client.transaction().withBundle(bundle).execute();
    }

    public void deleteAllPatients() {
        Bundle searchBundle = client
                .search()
                .forResource(Patient.class)
                .returnBundle(Bundle.class)
                .cacheControl(new CacheControlDirective().setNoCache(true))
                .execute();

        Bundle deleteBundle = new Bundle();
        deleteBundle.setType(Bundle.BundleType.TRANSACTION);

        searchBundle.getEntry()
                .forEach(e -> deleteBundle
                .addEntry()
                .getRequest().setUrl(e.getFullUrl())
                .setMethod(Bundle.HTTPVerb.DELETE));

        while (searchBundle.getLink(IBaseBundle.LINK_NEXT) != null) {
            searchBundle = client
                    .loadPage()
                    .next(searchBundle)
                    .execute();

            searchBundle.getEntry().stream()
                    .forEach(e -> deleteBundle
                    .addEntry()
                    .getRequest().setUrl(e.getFullUrl())
                    .setMethod(Bundle.HTTPVerb.DELETE));
        }

        client.transaction().withBundle(deleteBundle).execute();
    }

    public void deleteEncounters(String patientId) {
        Bundle searchBundle = client.search()
                .forResource(Encounter.class)
                .where(Encounter.PATIENT.hasId(new IdType("Patient", patientId)))
                .returnBundle(Bundle.class)
                .cacheControl(new CacheControlDirective().setNoCache(true))
                .execute();

        Bundle deleteBundle = new Bundle();
        deleteBundle.setType(Bundle.BundleType.TRANSACTION);

        searchBundle.getEntry().stream()
                .map(e -> (Encounter) e.getResource())
                .forEach(encounter -> {
                    deleteBundle.addEntry()
                            .getRequest()
                            .setUrl("Encounter/" + encounter.getIdElement().getIdPart())
                            .setMethod(Bundle.HTTPVerb.DELETE);
                });

        while (searchBundle.getLink(IBaseBundle.LINK_NEXT) != null) {
            searchBundle = client
                    .loadPage()
                    .next(searchBundle)
                    .execute();

            searchBundle.getEntry().stream()
                    .map(e -> (Encounter) e.getResource())
                    .forEach(encounter -> {
                        deleteBundle.addEntry()
                                .getRequest()
                                .setUrl("Encounter/" + encounter.getIdElement().getIdPart())
                                .setMethod(Bundle.HTTPVerb.DELETE);
                    });
        }

        client.transaction().withBundle(deleteBundle).execute();
    }

    public void deleteObservations(String patientId) {
        Bundle searchBundle = client.search()
                .forResource(Observation.class)
                .where(Observation.PATIENT.hasId(new IdType("Patient", patientId)))
                .returnBundle(Bundle.class)
                .cacheControl(new CacheControlDirective().setNoCache(true))
                .execute();

        Bundle deleteBundle = new Bundle();
        deleteBundle.setType(Bundle.BundleType.TRANSACTION);

        searchBundle.getEntry().stream()
                .map(e -> (Observation) e.getResource())
                .forEach(observation -> {
                    deleteBundle.addEntry()
                            .getRequest()
                            .setUrl("Observation/" + observation.getIdElement().getIdPart())
                            .setMethod(Bundle.HTTPVerb.DELETE);
                });

        while (searchBundle.getLink(IBaseBundle.LINK_NEXT) != null) {
            searchBundle = client
                    .loadPage()
                    .next(searchBundle)
                    .execute();

            searchBundle.getEntry().stream()
                    .map(e -> (Observation) e.getResource())
                    .forEach(observation -> {
                        deleteBundle.addEntry()
                                .getRequest()
                                .setUrl("Observation/" + observation.getIdElement().getIdPart())
                                .setMethod(Bundle.HTTPVerb.DELETE);
                    });
        }

        client.transaction().withBundle(deleteBundle).execute();
    }

    /**
     * Not supported by Azure.
     *
     * @param patient
     * @return
     */
    public MethodOutcome deletePatientCascade(Patient patient) {
        return client.delete()
                .resource(patient)
                .cascade(DeleteCascadeModeEnum.DELETE)
                .execute();
    }

    public MethodOutcome deletePatient(Patient patient) {
        return client.delete().resource(patient).execute();
    }

    public Bundle searchPatient(String resourceId) {
        return client
                .search()
                .forResource(Patient.class)
                .where(Patient.RES_ID.exactly().identifier(resourceId))
                .returnBundle(Bundle.class)
                .cacheControl(new CacheControlDirective().setNoCache(true))
                .execute();
    }

    public Patient getPatient(String resourceId) {
        return client.read()
                .resource(Patient.class)
                .withId(resourceId)
                .execute();
    }

    public Bundle getPatients() {
        return client
                .search()
                .forResource(Patient.class)
                .returnBundle(Bundle.class)
                .cacheControl(new CacheControlDirective().setNoCache(true))
                .execute();
    }

}
