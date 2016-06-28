package com.wso2telco.dep.tpservice.util;

/**
 * Validator class
 * @author Chinthana
 * @since 28/06/2016
 */
public class Validator {

    /**
     * Checks whether the string is null or empty.
     * @param str  the string to check.
     * @return true if the string is null or empty, false otherwise.
     */
    public static boolean isInvalidString(final String str) {
        if (str == null) {
            return true;
        }
        String as = str.trim();
        if ((as.equals("null")) || (as.equals(""))) {
            return true;
        }
        return false;
    }
}
