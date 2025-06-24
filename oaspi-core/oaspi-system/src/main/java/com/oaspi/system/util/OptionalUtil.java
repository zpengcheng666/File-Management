package com.oaspi.system.util;

/**
 * @Description TODO
 * @Author hong yang
 * @Date 2024/11/18 20:26
 */
public class OptionalUtil {

    /**
     * Returns the value if it's not null, otherwise returns the default value.
     *
     * @param value        the value to check
     * @param defaultValue the default value to return if the value is null
     * @param <T>          the type of the value
     * @return the value or the default value if the value is null
     */
    public static <T> T getValueOrDef(T value, T defaultValue) {
        return value != null ? value : defaultValue;
    }

    /**
     * Converts a blank or null string to the specified default value.
     *
     * @param value        the string to check
     * @param defaultValue the default value to return if the string is blank or null
     * @return the original string if it's not blank, otherwise the default value
     */
    public static String convertBlankToDef(String value, String defaultValue) {
        return (value == null || value.trim().isEmpty()) ? defaultValue : value;
    }
}
