package com.cgi.eet.playground.domain.taxpayer;

import com.cgi.eet.playground.domain.aggregates.TaxPayer;
import com.cgi.eet.playground.domain.commands.taxpayer.*;
import com.cgi.eet.playground.domain.entities.Workplace;
import com.cgi.eet.playground.domain.events.portalaccount.PortalAccountCreatedEvent;
import com.cgi.eet.playground.domain.events.taxpayer.*;
import junit.framework.Assert;
import junit.framework.AssertionFailedError;
import org.axonframework.test.FixtureConfiguration;
import org.axonframework.test.Fixtures;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

/**
 * @author stepanm
 */
public class CreationTest {

    private FixtureConfiguration fixtureConfiguration;
    private final String dic = "CZ12346578";
    private final String loginName = "stepan";
    private final String devId = "1";

    @Before
    public void setUp() throws Exception {
        fixtureConfiguration = Fixtures.newGivenWhenThenFixture(TaxPayer.class);
    }

    @Test
    public void testCreateTaxpayer() throws Exception {

        fixtureConfiguration.given()
                .when(new CreateUnactivatedTaxPayerCommand(dic, devId, loginName))
                .expectEvents(new UnactivatedTaxPayerCreatedEvent(dic, devId),
                        new AssignPortalAccountToTaxPayerEvent(dic, loginName));
    }

    @Test
    public void testIssueCertificate() throws Exception {

        byte[] cert = "Hello".getBytes();

        fixtureConfiguration
                .given(new UnactivatedTaxPayerCreatedEvent(dic, devId))
                .when(new TaxPayerIssueCertificateCommand(dic, cert))
                .expectEvents(new TaxPayerCertificateIssuedEvent(dic, cert));
    }

    @Test
    public void testActivateTaxPayer() throws Exception {

        String workplaceId = "12";
        String workplaceType = "normal";
        String workplaceIdentification = "blue";
        String workplaceState = "cr";
        String openHours = "0-0";
        String operationType = "furt";
        Date dateWorkplaceCreation = new Date();
        String workplaceCreationType = "functional";

        Workplace workplace = new Workplace(workplaceId, workplaceType, workplaceIdentification, workplaceState, openHours, operationType, workplaceCreationType, dateWorkplaceCreation);

        fixtureConfiguration.given(
                new UnactivatedTaxPayerCreatedEvent(dic, devId),
                new AssignPortalAccountToTaxPayerEvent(dic, loginName),
                new WorkplaceAddedToTaxPayerEvent(dic, workplace),
                new TaxPayerCertificateIssuedEvent(dic, workplace.toString().getBytes()))
                .when(new ActivateTaxPayerCommand(dic))
                .expectEvents(new TaxPayerActivatedEvent(dic));

    }

}
