package com.structura.steel.commons.utils;

import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

public class AppConstants {
    public static final String[] PERMITTED_HOSTS = {
            "http://localhost:3000"
    };
    public static final String DEFAULT_PAGE_NUMBER = "0";
    public static final String DEFAULT_PAGE_SIZE = "10";
    public static final String DEFAULT_SORT_BY = "id";
    public static final String DEFAULT_SORT_DIRECTION = "asc";
    public static final String DELETED = "false";
    public static final String GET_ALL = "false";

    public static final String DELIMITER_COMMA = ",";
    public static final String EMPTY_STRING = "";

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static final String DEV_PROFILE = "dev";
    public static final String PROD_PROFILE = "prod";

    public static final int MAX_EMAIL_ATTEMPTS = 5;
    public static final int MAX_OTP_ATTEMPTS = 3;
    public static final long LOCKOUT_DURATION_MINUTES = 5;
}
