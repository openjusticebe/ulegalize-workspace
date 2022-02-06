package com.ulegalize.lawfirm.utils;

import java.util.Locale;
import java.util.Optional;
import java.util.Set;

public class Utils {

    public static String countryIso2Code(String inputCountryCode, String language) {
        Set<String> countryCodes = Locale.getISOCountries(Locale.IsoCountryCode.PART1_ALPHA3);

        Optional<String> codeOptional = inputCountryCode != null ? countryCodes.stream().filter(country -> {
            Locale obj = new Locale(language, country);

            return obj.getCountry().equalsIgnoreCase(inputCountryCode);

        }).findFirst() : Optional.empty();

        return codeOptional.orElse("BE");

    }

    public static String generateHashkey() {
        // remove 0.
        // 2 power 31 => 10 digit : 2147483648
        String random = String.valueOf((Math.random())).substring(2);
        // to be sure that the number is not greather than 9 (10 - 1 security digit)
        int maxLength = Math.min(random.length(), 9);
        random = random.substring(0, maxLength);
        return Integer.toString(Integer.parseInt(random), 36);
    }
}