package com.cgi.eet.playground.domain.taxpayer;

import com.cgi.eet.playground.domain.aggregates.TaxPayer;
import com.cgi.eet.playground.domain.commands.taxpayer.IsTaxPayerActiveableCommand;
import com.cgi.eet.playground.domain.entities.Workplace;
import com.cgi.eet.playground.domain.events.taxpayer.*;
import org.axonframework.test.FixtureConfiguration;
import org.axonframework.test.Fixtures;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

/**
 * Created by stepanm on 25.2.2016.
 */
public class ActivableTest {

    private FixtureConfiguration fixtureConfiguration;
    private final String dic = "CZ12346578";
    private final String loginName = "stepan";
    private final String devId = "1";

    @Before
    public void setUp() throws Exception {
        fixtureConfiguration = Fixtures.newGivenWhenThenFixture(TaxPayer.class);
    }

    @Test
    public void testIsActiveableWithAll() throws Exception {

        String workplaceId = "12";
        String workplaceType = "normal";
        String workplaceIdentification = "blue";
        String workplaceState = "cr";
        String openHours = "22-23";
        String operationType = "furt";
        Date dateWorkplaceCreation = new Date();
        String workplaceCreationType = "functional";
        Workplace workplace = new Workplace(workplaceId, workplaceType, workplaceIdentification, workplaceState, openHours, operationType, workplaceCreationType, dateWorkplaceCreation);

        byte[] certificate = "Hello".getBytes();

        fixtureConfiguration.given(new UnactivatedTaxPayerCreatedEvent(dic, devId),
                new AssignPortalAccountToTaxPayerEvent(dic, loginName),
                new TaxPayerCertificateIssuedEvent(dic, certificate),
                new WorkplaceAddedToTaxPayerEvent(dic, workplace))
                .when(new IsTaxPayerActiveableCommand(dic))
                .expectEvents(new TaxPayerHasCertificateEvent(dic),
                        new TaxPayerHasWorkplaceEvent(dic),
                        new TaxPayerHasPortalAccountEvent(dic),
                        new TaxPayerIsActiveableEvent(dic))
                .expectReturnValue(true);
    }

    @Test
    public void testIsActiveableWithoutCertificate() throws Exception {

        String workplaceId = "12";
        String workplaceType = "normal";
        String workplaceIdentification = "blue";
        String workplaceState = "cr";
        String openHours = "22-23";
        String operationType = "furt";
        Date dateWorkplaceCreation = new Date();
        String workplaceCreationType = "functional";
        Workplace workplace = new Workplace(workplaceId, workplaceType, workplaceIdentification, workplaceState, openHours, operationType, workplaceCreationType, dateWorkplaceCreation);


        fixtureConfiguration.given(new UnactivatedTaxPayerCreatedEvent(dic, devId),
                new AssignPortalAccountToTaxPayerEvent(dic, loginName),
                new WorkplaceAddedToTaxPayerEvent(dic, workplace))
                .when(new IsTaxPayerActiveableCommand(dic))
                .expectEvents(new TaxPayerHasNotCertificateEvent(dic),
                        new TaxPayerHasWorkplaceEvent(dic),
                        new TaxPayerHasPortalAccountEvent(dic))
                .expectReturnValue(false);
    }

    @Test
    public void testIsActiveableWithoutWorkplace() throws Exception {

        byte[] certificate = "Hello".getBytes();

        fixtureConfiguration.given(new UnactivatedTaxPayerCreatedEvent(dic, devId),
                new AssignPortalAccountToTaxPayerEvent(dic, loginName),
                new TaxPayerCertificateIssuedEvent(dic, certificate))
                .when(new IsTaxPayerActiveableCommand(dic))
                .expectEvents(new TaxPayerHasCertificateEvent(dic),
                        new TaxPayerHasNotWorkplaceEvent(dic),
                        new TaxPayerHasPortalAccountEvent(dic))
                .expectReturnValue(false);
    }


    @Test
    public void testIsActiveableWithoutPortalAccount() throws Exception {

        String workplaceId = "12";
        String workplaceType = "normal";
        String workplaceIdentification = "blue";
        String workplaceState = "cr";
        String openHours = "22-23";
        String operationType = "furt";
        Date dateWorkplaceCreation = new Date();
        String workplaceCreationType = "functional";
        Workplace workplace = new Workplace(workplaceId, workplaceType, workplaceIdentification, workplaceState, openHours, operationType, workplaceCreationType, dateWorkplaceCreation);

        byte[] certificate = "Hello".getBytes();

        fixtureConfiguration.given(new UnactivatedTaxPayerCreatedEvent(dic, devId),
                new TaxPayerCertificateIssuedEvent(dic, certificate),
                new WorkplaceAddedToTaxPayerEvent(dic, workplace))
                .when(new IsTaxPayerActiveableCommand(dic))
                .expectEvents(new TaxPayerHasCertificateEvent(dic),
                        new TaxPayerHasWorkplaceEvent(dic),
                        new TaxPayerHasNotPortalAccountEvent(dic))
                .expectReturnValue(false);
    }

    @Test
    public void testIsActiveableWithCertificateOnly() throws Exception {

        byte[] certificate = "Hello".getBytes();

        fixtureConfiguration.given(new UnactivatedTaxPayerCreatedEvent(dic, devId),
                new TaxPayerCertificateIssuedEvent(dic, certificate))
                .when(new IsTaxPayerActiveableCommand(dic))
                .expectEvents(new TaxPayerHasCertificateEvent(dic),
                        new TaxPayerHasNotWorkplaceEvent(dic),
                        new TaxPayerHasNotPortalAccountEvent(dic))
                .expectReturnValue(false);
    }

    @Test
    public void testIsActiveableWithWorkplaceOnly() throws Exception {

        String workplaceId = "12";
        String workplaceType = "normal";
        String workplaceIdentification = "blue";
        String workplaceState = "cr";
        String openHours = "22-23";
        String operationType = "furt";
        Date dateWorkplaceCreation = new Date();
        String workplaceCreationType = "functional";
        Workplace workplace = new Workplace(workplaceId, workplaceType, workplaceIdentification, workplaceState, openHours, operationType, workplaceCreationType, dateWorkplaceCreation);

        fixtureConfiguration.given(new UnactivatedTaxPayerCreatedEvent(dic, devId),
                new WorkplaceAddedToTaxPayerEvent(dic, workplace))
                .when(new IsTaxPayerActiveableCommand(dic))
                .expectEvents(new TaxPayerHasNotCertificateEvent(dic),
                        new TaxPayerHasWorkplaceEvent(dic),
                        new TaxPayerHasNotPortalAccountEvent(dic))
                .expectReturnValue(false);
    }

    @Test
    public void testIsActiveableWithPortalAccountOnly() throws Exception {

        fixtureConfiguration.given(new UnactivatedTaxPayerCreatedEvent(dic, devId),
                new AssignPortalAccountToTaxPayerEvent(dic, loginName))
                .when(new IsTaxPayerActiveableCommand(dic))
                .expectEvents(new TaxPayerHasNotCertificateEvent(dic),
                        new TaxPayerHasNotWorkplaceEvent(dic),
                        new TaxPayerHasPortalAccountEvent(dic))
                .expectReturnValue(false);
    }

    @Test
    public void testIsActiveableWithNothing() throws Exception {

        fixtureConfiguration.given(new UnactivatedTaxPayerCreatedEvent(dic, devId))
                .when(new IsTaxPayerActiveableCommand(dic))
                .expectEvents(new TaxPayerHasNotCertificateEvent(dic),
                        new TaxPayerHasNotWorkplaceEvent(dic),
                        new TaxPayerHasNotPortalAccountEvent(dic))
                .expectReturnValue(false);
    }
}
