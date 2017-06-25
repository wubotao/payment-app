package io.taotao.util;

import com.paypal.base.rest.APIContext;
import io.taotao.bean.PaypalClient;

import java.io.*;
import java.util.Properties;

/**
 * Author   : Tony Wu
 * Email    : mail@taotao.io
 */

public class PaypalUtil {
    private static Properties properties;

    static {
        String configFilename = "config.properties";
        File config = new File(configFilename);
        properties = new Properties();

        try (InputStream inputStream = new FileInputStream(config)) {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static PaypalClient getPaypalConfig(String currency) {
        PaypalClient client = new PaypalClient();
        if ("USD".equals(currency)) {
            client.setId(properties.getProperty("PP_USD_CLIENT_ID"));
            client.setSecret(properties.getProperty("PP_USD_CLIENT_SECRET"));
            return client;
        }

        if ("EUR".equals(currency)) {
            client.setId(properties.getProperty("PP_EUR_CLIENT_ID"));
            client.setSecret(properties.getProperty("PP_EUR_CLIENT_SECRET"));
            return client;
        }

        if ("AUD".equals(currency)) {
            client.setId(properties.getProperty("PP_AUD_CLIENT_ID"));
            client.setSecret(properties.getProperty("PP_AUD_CLIENT_SECRET"));
            return client;
        }

        if ("HKD".equals(currency)) {
            client.setId(properties.getProperty("PP_HKD_CLIENT_ID"));
            client.setSecret(properties.getProperty("PP_HKD_CLIENT_SECRET"));
            return client;
        }

        throw new RuntimeException("The currency " + currency + " is not supported");
    }

    public static APIContext getPaypalContext(String currency) {
        PaypalClient client = PaypalUtil.getPaypalConfig(currency);
        return new APIContext(client.getId(), client.getSecret(), "sandbox");
    }

}
