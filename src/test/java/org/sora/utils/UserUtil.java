package org.sora.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import lombok.val;
import org.sora.dataproviders.User;
import org.sora.rest.information.dto.CountryDTO;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;

import static org.sora.utils.CryptoUtil.getSecureRandom;
import static org.sora.utils.DidUtil.generateUserDDO;
import static org.sora.utils.MnemonicUtil.*;

public class UserUtil {

    //TODO
    public static User initRandomUser(){

        Faker faker = new Faker();
        User user = new User();

        user.setFirstName(faker.name().firstName())
                .setLastName(faker.name().lastName())
                .setPhone(faker.phoneNumber().cellPhone())
                .setCountry(new Locale("", faker.address().countryCode()).getISO3Country())
                .setPassphrase(generateMnemonic(getSecureRandom(20)));
        generateUserDDO(user);
        return user;
    }

    public static User initUser(String passphrase){

        User user = null;
        if(checkMnemonic(splitToArray(passphrase))){

            user = new User();
            Faker faker = new Faker();
            user.setFirstName(faker.name().firstName())
                    .setLastName(faker.name().lastName())
                    .setPhone(faker.phoneNumber().cellPhone())
                    .setCountry(faker.address().country())
                    .setPassphrase(passphrase);
            generateUserDDO(user);
        }
        return user;
    }

    //TODO move to db
    public static User retrieveUser(String path){

        ObjectMapper mapper = new ObjectMapper();
        User user = null;

        try {

            user = mapper.readValue(new FileInputStream(path), User.class);
        } catch (IOException ex){

            ex.printStackTrace();
        }
        generateUserDDO(user);
        return user;
    }

    public static void checkCountry3(User user, Map<String, CountryDTO> countries){

        Faker faker = new Faker();
        val country = countries.get(user.getCountry());
        if (country == null){

            CountryDTO def = countries.get("RUS");
            user.setCountry(def.getName());
            user.setPhone(def.getDial_code() + "000" + faker.number().digits(8));
        }
        else {

            user.setPhone(country.getDial_code() + faker.number().digits(8));
        }
    }
}