package io.taotao.util;

import com.braintreegateway.BraintreeGateway;
import com.paypal.base.rest.APIContext;
import io.taotao.bean.PaypalClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Author   : Tony Wu
 * Email    : mail@taotao.io
 */

public class BraintreeUtil {
    private static Properties properties;
    public static BraintreeGateway gateway;

    static {
        String configFilename = "config.properties";
        File config = new File(configFilename);
        properties = new Properties();

        try (InputStream inputStream = new FileInputStream(config)) {
            properties.load(inputStream);
            gateway = BraintreeGatewayFactory.fromConfigFile(config);
        } catch (IOException e) {
            System.err.println("Could not load Braintree configuration from config file or system environment.");
            System.exit(1);
        }
    }

    public static String getAccountId(String currency) {
        if ("HKD".equals(currency)) {
            return properties.getProperty("BT_HKD_ACCOUNT");
        }

        if ("JPY".equals(currency)) {
            return properties.getProperty("BT_JPY_ACCOUNT");
        }

        return properties.getProperty("BT_USD_ACCOUNT");
    }





}
