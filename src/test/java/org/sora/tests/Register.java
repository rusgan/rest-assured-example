package org.sora.tests;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import lombok.val;
import org.junit.jupiter.api.*;
import org.sora.dataproviders.User;
import org.sora.steps.AccountSteps;
import org.sora.steps.DidResolverSteps;
import org.sora.steps.InformationSteps;
import org.sora.steps.ProjectSteps;
import org.sora.utils.PropertiesUtil;

import static org.assertj.core.api.Assertions.assertThat;
import static org.sora.rest.SoraFilter.initFilter;
import static org.sora.utils.AssertUtil.assertPositiveStatus;
import static org.sora.utils.DidUtil.generateUserDDO;
import static org.sora.utils.UserUtil.checkCountry3;
import static org.sora.utils.UserUtil.initRandomUser;

@Epic("")
@Feature("")
@Story("")
class Register extends BaseTest {

    private AccountSteps accountSteps = new AccountSteps();
    private ProjectSteps projectSteps = new ProjectSteps();
    private DidResolverSteps didSteps = new DidResolverSteps();
    private InformationSteps informationSteps = new InformationSteps();
    private static User user;

    @Test()
    @Order(1)
    @Tags({@Tag("registration"), @Tag("positive")})
    void register() {

        //Register DDO
        val didResponse = didSteps.registerDid(generateUserDDO(user));
        assertPositiveStatus(didResponse.getStatus());

        //Select country
        val countriesResponse = informationSteps.getCountries();
        assertPositiveStatus(countriesResponse.getStatus());
        //checkCountry(user, countriesResponse.getCountries());
        checkCountry3(user, countriesResponse.getCountries());

        //Create user
        val createResponse = accountSteps.createUser(user);
        assertPositiveStatus(createResponse.getStatus());

        //Get SMS code
        val code = accountSteps.getVerificationCode(user.getUserId());

        //Verify phone
        val responseVerify = accountSteps.verify(code);
        assertPositiveStatus(responseVerify.getStatus());

        //Register user
        val registerUserResponse = accountSteps.registerUser(user);
        assertPositiveStatus(registerUserResponse.getStatus());

        val responseVotes =  projectSteps.getVotes();
        assertPositiveStatus(responseVotes.getStatus());
        assertThat(responseVotes.getVotes()).isEqualTo(1337);
    }

    @BeforeAll
    static void setUp() {

        //Init properties
        PropertiesUtil.initProperties();

        //Init user
        user = initRandomUser();

        //Init filter
        initFilter(user.getKeyPair());

        //Init connection
        AccountSteps.createAccountConnection();
    }


    @AfterAll
    static void tearDown() {

        //Close connection
        AccountSteps.closeAccountConnection();
    }
}
