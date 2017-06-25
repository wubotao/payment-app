package io.taotao.util;

/**
 * Author : Tony Wu <mail@taotao.io>
 * Date   : 2017-06-17
 * Copyright (c) 2017. All rights reserved.
 */
public class CardUtil {
    public static String getCreditCardType (String creditCardNumber) {

        String regVisa = "^4[0-9]{12}(?:[0-9]{3})?$";
        String regMaster = "^5[1-5][0-9]{14}$";
        String regExpress = "^3[47][0-9]{13}$";
        String regDiscover = "^6(?:011|5[0-9]{2})[0-9]{12}$";
        String regJCB= "^(?:2131|1800|35\\d{3})\\d{11}$";

        if(creditCardNumber.matches(regVisa))
            return "visa";
        if (creditCardNumber.matches(regMaster))
            return "mastercard";
        if (creditCardNumber.matches(regExpress))
            return "amex";
        if (creditCardNumber.matches(regDiscover))
            return "discover";
        if (creditCardNumber.matches(regJCB))
            return "jcb";
        throw new RuntimeException("Credit card type must be visa, mastercard, amex, jcb, or discover.");
    }
}
