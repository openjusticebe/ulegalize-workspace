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
}