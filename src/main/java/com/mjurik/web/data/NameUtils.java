package com.mjurik.web.data;

import com.mjurik.web.crawler.db.entity.NominalValue;
import com.mjurik.web.crawler.db.entity.Variant;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Marian Jurik on 20.11.2015.
 */
public class NameUtils {

    private static final Map<String, NominalValue> SUBSTR_TO_NOMINAL = new HashMap<>();
    private static final Map<String, Variant> SUBSTR_TO_VARIANT = new HashMap<>();

    static {
        SUBSTR_TO_NOMINAL.put("10 EUR".toLowerCase(), NominalValue.E_10);
        SUBSTR_TO_NOMINAL.put("10 €".toLowerCase(), NominalValue.E_10);
        SUBSTR_TO_NOMINAL.put("100 EUR".toLowerCase(), NominalValue.E_100);
        SUBSTR_TO_NOMINAL.put("100 €".toLowerCase(), NominalValue.E_100);
        SUBSTR_TO_NOMINAL.put("200 EUR".toLowerCase(), NominalValue.E_200);
        SUBSTR_TO_NOMINAL.put("200 €".toLowerCase(), NominalValue.E_200);

        SUBSTR_TO_NOMINAL.put("100 SK".toLowerCase(), NominalValue.SK_100);
        SUBSTR_TO_NOMINAL.put("200 SK".toLowerCase(), NominalValue.SK_200);
        SUBSTR_TO_NOMINAL.put("500 SK".toLowerCase(), NominalValue.SK_500);
        SUBSTR_TO_NOMINAL.put("1000 SK".toLowerCase(), NominalValue.SK_1000);
        SUBSTR_TO_NOMINAL.put("5000 SK".toLowerCase(), NominalValue.SK_5000);
        SUBSTR_TO_NOMINAL.put("10000 SK".toLowerCase(), NominalValue.SK_10000);

        SUBSTR_TO_NOMINAL.put("Sada minc".toLowerCase(), NominalValue.COLLECTION);

        SUBSTR_TO_VARIANT.put("PROOF".toLowerCase(), Variant.PROOF);
        SUBSTR_TO_VARIANT.put("BK".toLowerCase(), Variant.BK);
    }

    public static NominalValue parseNominal(String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        String nameLower = name.toLowerCase();
        for (Map.Entry<String, NominalValue> valueEntry : SUBSTR_TO_NOMINAL.entrySet()) {
            if (nameLower.contains(valueEntry.getKey())) {
                return valueEntry.getValue();
            }
        }
        return null;
    }

    public static Variant parseVariant(String name) {
        if (StringUtils.isBlank(name)) {
            return Variant.UNKNOWN;
        }
        String nameLower = name.toLowerCase();
        for (Map.Entry<String, Variant> variantEntry : SUBSTR_TO_VARIANT.entrySet()) {
            Pattern regex = Pattern.compile("\\b" + variantEntry.getKey() + "\\b");
            Matcher regexMatcher = regex.matcher(nameLower);
            if (regexMatcher.find()) {
                return variantEntry.getValue();
            }
        }
        return Variant.UNKNOWN;
    }
}
