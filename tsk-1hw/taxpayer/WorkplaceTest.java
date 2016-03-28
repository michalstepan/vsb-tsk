package com.cgi.eet.playground.domain.taxpayer;

import com.cgi.eet.playground.domain.aggregates.TaxPayer;
import com.cgi.eet.playground.domain.commands.taxpayer.AddWorkplaceToTaxPayerCommand;
import com.cgi.eet.playground.domain.commands.taxpayer.IsTaxPayerWorkPlaceOpenNowCommand;
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
public class WorkplaceTest {

    private FixtureConfiguration fixtureConfiguration;
    private final String dic = "CZ12346578";
    private final String loginName = "stepan";
    private final String devId = "1";

    @Before
    public void setUp() throws Exception {
        fixtureConfiguration = Fixtures.newGivenWhenThenFixture(TaxPayer.class);
    }

    @Test
    public void testAddWorkplace() throws Exception {

        String workplaceId = "12";
        String workplaceType = "normal";
        String workplaceIdentification = "blue";
        String workplaceState = "cr";
        String openHours = "0-0";
        String operationType = "furt";
        Date dateWorkplaceCreation = new Date();
        String workplaceCreationType = "functional";

        Workplace workplace = new Workplace(workplaceId, workplaceType, workplaceIdentification, workplaceState, openHours, operationType, workplaceCreationType, dateWorkplaceCreation);

        fixtureConfiguration.given(new UnactivatedTaxPayerCreatedEvent(dic, devId),
                new AssignPortalAccountToTaxPayerEvent(dic, loginName))
                .when(new AddWorkplaceToTaxPayerCommand(dic, workplace))
                .expectEvents(new WorkplaceAddedToTaxPayerEvent(dic, workplace));

    }

    @Test
    public void testIsWorkPlaceOpened() throws Exception {

        String workplaceId = "12";
        String workplaceType = "normal";
        String workplaceIdentification = "blue";
        String workplaceState = "activated";
        String openHours = "1-23";
        String operationType = "furt";
        Date dateWorkplaceCreation = new Date();
        String workplaceCreationType = "functional";

        Workplace workplace = new Workplace(workplaceId, workplaceType, workplaceIdentification, workplaceState, openHours, operationType, workplaceCreationType, dateWorkplaceCreation);


        fixtureConfiguration.given(new UnactivatedTaxPayerCreatedEvent(dic, devId),
                new AssignPortalAccountToTaxPayerEvent(dic, loginName),
                new WorkplaceAddedToTaxPayerEvent(dic, workplace))
                .when(new IsTaxPayerWorkPlaceOpenNowCommand(dic, workplaceId))
                .expectEvents(new TaxPayerWorkPlaceIsOpenedNowEvent(dic));
    }

    @Test
    public void testIsWorkPlaceOpenedButClosed() throws Exception {

        String workplaceId = "12";
        String workplaceType = "normal";
        String workplaceIdentification = "blue";
        String workplaceState = "activated";
        String openHours = "22-23";
        String operationType = "furt";
        Date dateWorkplaceCreation = new Date();
        String workplaceCreationType = "functional";

        Workplace workplace = new Workplace(workplaceId, workplaceType, workplaceIdentification, workplaceState, openHours, operationType, workplaceCreationType, dateWorkplaceCreation);


        fixtureConfiguration.given(new UnactivatedTaxPayerCreatedEvent(dic, devId),
                new AssignPortalAccountToTaxPayerEvent(dic, loginName),
                new WorkplaceAddedToTaxPayerEvent(dic, workplace))
                .when(new IsTaxPayerWorkPlaceOpenNowCommand(dic, workplaceId))
                .expectEvents(new TaxPayerWorkPlaceIsClosedNowEvent(dic));
    }

    @Test
    public void testIsWorkPlaceOpenedButNotFilledOpenHours() throws Exception {

        String workplaceId = "12";
        String workplaceType = "normal";
        String workplaceIdentification = "blue";
        String workplaceState = "cr";
        String openHours = null;
        String operationType = "furt";
        Date dateWorkplaceCreation = new Date();
        String workplaceCreationType = "functional";

        Workplace workplace = new Workplace(workplaceId, workplaceType, workplaceIdentification, workplaceState, openHours, operationType, workplaceCreationType, dateWorkplaceCreation);


        fixtureConfiguration.given(new UnactivatedTaxPayerCreatedEvent(dic, devId),
                new AssignPortalAccountToTaxPayerEvent(dic, loginName),
                new WorkplaceAddedToTaxPayerEvent(dic, workplace))
                .when(new IsTaxPayerWorkPlaceOpenNowCommand(dic, workplaceId))
                .expectException(IllegalArgumentException.class);
    }

    @Test
    public void testIsWorkPlaceOpenedButWorkplaceIsntInTaxPayer() throws Exception {

        String workplaceId = "12";
        String workplaceType = "normal";
        String workplaceIdentification = "blue";
        String workplaceState = "cr";
        String openHours = null;
        String operationType = "furt";
        Date dateWorkplaceCreation = new Date();
        String workplaceCreationType = "functional";

        Workplace workplace = new Workplace(workplaceId, workplaceType, workplaceIdentification, workplaceState, openHours, operationType, workplaceCreationType, dateWorkplaceCreation);

        String workplaceIdNotInTaxpayer = "1231321";

        fixtureConfiguration.given(new UnactivatedTaxPayerCreatedEvent(dic, devId),
                new AssignPortalAccountToTaxPayerEvent(dic, loginName),
                new WorkplaceAddedToTaxPayerEvent(dic, workplace))
                .when(new IsTaxPayerWorkPlaceOpenNowCommand(dic, workplaceIdNotInTaxpayer))
                .expectException(IllegalArgumentException.class);
    }

    @Test
    public void testIsWorkPlaceOpenedButWorkplaceNotExists() throws Exception {

        String workplaceId = "12";
        String workplaceType = "normal";
        String workplaceIdentification = "blue";
        String workplaceState = "activated";
        String openHours = "22-23";
        String operationType = "furt";
        Date dateWorkplaceCreation = new Date();
        String workplaceCreationType = "functional";

        Workplace workplace = new Workplace(workplaceId, workplaceType, workplaceIdentification, workplaceState, openHours, operationType, workplaceCreationType, dateWorkplaceCreation);


        fixtureConfiguration.given(new UnactivatedTaxPayerCreatedEvent(dic, devId),
                new AssignPortalAccountToTaxPayerEvent(dic, loginName))
                .when(new IsTaxPayerWorkPlaceOpenNowCommand(dic, workplaceId))
                .expectEvents(new TaxPayerHasPortalAccountEvent(dic),
                        new TaxPayerHasNotCertificateEvent(dic));
    }

    @Test
    public void testIsWorkPlaceOpenedButWorkplaceNotExistsAndAccountDetto() throws Exception {

        String workplaceId = "12";
        String workplaceType = "normal";
        String workplaceIdentification = "blue";
        String workplaceState = "cr";
        String openHours = "22-23";
        String operationType = "furt";
        Date dateWorkplaceCreation = new Date();
        String workplaceCreationType = "functional";

        Workplace workplace = new Workplace(workplaceId, workplaceType, workplaceIdentification, workplaceState, openHours, operationType, workplaceCreationType, dateWorkplaceCreation);


        fixtureConfiguration.given(new UnactivatedTaxPayerCreatedEvent(dic, devId))
                .when(new IsTaxPayerWorkPlaceOpenNowCommand(dic, workplaceId))
                .expectEvents(new TaxPayerHasNotPortalAccountEvent(dic),
                        new TaxPayerHasNotCertificateEvent(dic));
    }

    @Test(expected = AssertionError.class)
    public void testIsWorkPlaceOpenedOnWrongDic() throws Exception {

        String workplaceId = "12";
        String workplaceType = "normal";
        String workplaceIdentification = "blue";
        String workplaceState = "cr";
        String openHours = "22-23";
        String operationType = "furt";
        Date dateWorkplaceCreation = new Date();
        String workplaceCreationType = "functional";

        String wrongDic = "123";

        Workplace workplace = new Workplace(workplaceId, workplaceType, workplaceIdentification, workplaceState, openHours, operationType, workplaceCreationType, dateWorkplaceCreation);

        fixtureConfiguration.given(new UnactivatedTaxPayerCreatedEvent(dic, devId))
                .when(new IsTaxPayerWorkPlaceOpenNowCommand(wrongDic, workplaceId))
                .expectEvents();
    }

    @Test
    public void testIsWorkPlaceOpenedButWorkplaceNotExistsAndAccountDoesButNoCertificate() throws Exception {

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
                new AssignPortalAccountToTaxPayerEvent(dic, loginName))
                .when(new IsTaxPayerWorkPlaceOpenNowCommand(dic, workplaceId))
                .expectEvents(new TaxPayerHasPortalAccountEvent(dic),
                        new TaxPayerHasNotCertificateEvent(dic));
    }

    @Test
    public void testIsWorkPlaceOpenedButWorkplaceNotExistsAndAccountDoes() throws Exception {

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
                new TaxPayerCertificateIssuedEvent(dic, certificate))
                .when(new IsTaxPayerWorkPlaceOpenNowCommand(dic, workplaceId))
                .expectEvents(new TaxPayerHasPortalAccountEvent(dic),
                        new TaxPayerHasCertificateEvent(dic));
    }
}
