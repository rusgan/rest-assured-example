package org.sora.utils;

import org.sora.properties.Properties;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class PropertiesUtil {

    public static void  initProperties() {

        try {
            java.util.Properties config = new java.util.Properties();
            InputStream input = new FileInputStream("");
            config.load(new InputStreamReader(input, StandardCharsets.UTF_8));
            Properties.ACCOUNT_URL = config.getProperty("account.url");
            Properties.DID_URL = config.getProperty("did.url");
            Properties.AUTH_URL = config.getProperty("auth.url");
            Properties.PROJECT_URL = config.getProperty("project.url");
            Properties.WALLET_URL = config.getProperty("wallet.url");
            Properties.ACTIVITY_URL = config.getProperty("activity.url");
            Properties.INFORMATION_URL = config.getProperty("information.url");
            Properties.DB_URL = config.getProperty("db.url");
            Properties.DB_PORT = config.getProperty("db.port");
            Properties.ACCOUNT_DB_USER = config.getProperty("account.user");
            Properties.ACCOUNT_DB_PASS = config.getProperty("account.pass");
            Properties.PROJECT_NAME = config.getProperty("project.name");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
