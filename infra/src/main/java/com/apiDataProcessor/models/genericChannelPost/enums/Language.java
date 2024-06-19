package com.apiDataProcessor.models.genericChannelPost.enums;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.Getter;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

@Getter
public enum Language {
    ENGLISH(Sets.newHashSet("en")),
    SPANISH(Sets.newHashSet("es")),
    FRENCH(Sets.newHashSet("fr")),
    GERMAN(Sets.newHashSet("de")),
    ITALIAN(Sets.newHashSet("it")),
    PORTUGUESE(Sets.newHashSet("pt")),
    DUTCH(Sets.newHashSet("nl")),
    RUSSIAN(Sets.newHashSet("ru")),
    JAPANESE(Sets.newHashSet("ja")),
    CHINESE(Sets.newHashSet("zh")),
    KOREAN(Sets.newHashSet("ko")),
    ARABIC(Sets.newHashSet("ar")),
    HINDI(Sets.newHashSet("hi")),
    BENGALI(Sets.newHashSet("bn")),
    PUNJABI(Sets.newHashSet("pa")),
    URDU(Sets.newHashSet("ur")),
    TAMIL(Sets.newHashSet("ta")),
    TELUGU(Sets.newHashSet("te")),
    MARATHI(Sets.newHashSet("mr")),
    GUJARATI(Sets.newHashSet("gu")),
    KANNADA(Sets.newHashSet("kn")),
    MALAYALAM(Sets.newHashSet("ml")),
    ORIYA(Sets.newHashSet("or")),
    ASSAMESE(Sets.newHashSet("as")),
    MAITHILI(Sets.newHashSet("mai")),
    SANTALI(Sets.newHashSet("sat")),
    KASHMIRI(Sets.newHashSet("ks")),
    NEPALI(Sets.newHashSet("ne")),
    SINDHI(Sets.newHashSet("sd")),
    SANSKRIT(Sets.newHashSet("sa")),
    ENGLISH_BRITISH(Sets.newHashSet("en-GB")),
    ENGLISH_AMERICAN(Sets.newHashSet("en-US")),
    ENGLISH_AUSTRALIAN(Sets.newHashSet("en-AU")),
    ENGLISH_CANADIAN(Sets.newHashSet("en-CA")),
    ENGLISH_INDIAN(Sets.newHashSet("en-IN")),
    ENGLISH_IRISH(Sets.newHashSet("", "en-IE")),
    ENGLISH_SOUTH_AFRICAN(Sets.newHashSet("en-ZA")),
    ENGLISH_SCOTTISH(Sets.newHashSet("en-GB-SCO")),
    ENGLISH_WELSH(Sets.newHashSet("en-GB-WLS")),
    ENGLISH_NEW_ZEALAND(Sets.newHashSet("en-NZ")),
    ENGLISH_SINGAPORE(Sets.newHashSet("en-SG")),
    ENGLISH_PHILIPPINES(Sets.newHashSet("en-PH")),
    ENGLISH_MALAYSIA(Sets.newHashSet("en-MY")),
    ENGLISH_HONG_KONG(Sets.newHashSet("en-HK")),
    ENGLISH_PAKISTAN(Sets.newHashSet("en-PK")),
    ENGLISH_BANGLADESH(Sets.newHashSet("en-BD")),
    ENGLISH_SRI_LANKA(Sets.newHashSet("en-LK")),
    ENGLISH_NIGERIA(Sets.newHashSet("en-NG")),
    ENGLISH_KENYA(Sets.newHashSet("en-KE")),
    ENGLISH_UGANDA(Sets.newHashSet("en-UG")),
    ENGLISH_TANZANIA(Sets.newHashSet("en-TZ")),
    ENGLISH_GHANA(Sets.newHashSet("en-GH")),
    ENGLISH_ZAMBIA(Sets.newHashSet("en-ZM")),
    ENGLISH_ZIMBABWE(Sets.newHashSet("en-ZW")),
    ENGLISH_CAMEROON(Sets.newHashSet("en-CM")),
    ;

    private final Set<String> languageCodes;
    private static final Map<String, Language> LANGUAGE_MAP;

    Language(Set<String> languageCodes) {
        this.languageCodes = languageCodes;
    }

    static {
        Map<String, Language> languageMap = Maps.newHashMap();
        for (Language language : Language.values()) {
            for (String languageCode : language.getLanguageCodes()) {
                languageMap.put(languageCode, language);
            }
        }
        LANGUAGE_MAP = Collections.unmodifiableMap(languageMap);
    }

    public static Language getLanguage(String languageCode) {
        return LANGUAGE_MAP.getOrDefault(languageCode, null);
    }
}
