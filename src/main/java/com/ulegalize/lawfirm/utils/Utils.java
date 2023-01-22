package com.ulegalize.lawfirm.utils;

import com.ulegalize.enumeration.EnumLanguage;

import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    public static final String PACKAGENAME = "com.ulegalize.lawfirm.international.Message";
    private static final String REGEXNOMENCLATUREPATTERN = "^[\\w\\-. ]+$";

    public static final Pattern VALID_NOMENCLATURE_REGEX =
            Pattern.compile(REGEXNOMENCLATUREPATTERN, Pattern.CASE_INSENSITIVE);

    public static String countryIso2Code(String inputCountryCode, String language) {
        Set<String> countryCodes = Locale.getISOCountries(Locale.IsoCountryCode.PART1_ALPHA3);

        Optional<String> codeOptional = inputCountryCode != null ? countryCodes.stream().filter(country -> {
            Locale obj = new Locale(language, country);

            return obj.getCountry().equalsIgnoreCase(inputCountryCode);

        }).findFirst() : Optional.empty();

        return codeOptional.orElse("BE");

    }

    public static boolean validNomenclature(String nomenclature) {
        Matcher matcher = VALID_NOMENCLATURE_REGEX.matcher(nomenclature);
        return matcher.find();
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

    public static String getLabel(EnumLanguage language, String labelFr, String labelEn, String labelNl, String labelDe) {
        if (language != null) {

            switch (language) {

                case FR:
                    return labelFr;
                case NL:
                    return labelNl;
                case EN:
                    return labelEn;
                case DE:
                    return labelDe;
                default:
                    return labelFr;
            }
        }

        return labelFr;
    }

}