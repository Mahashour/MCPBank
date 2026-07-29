package com.example.banksys.client.domain.model;

import org.jspecify.annotations.NonNull;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * An Egyptian governorate (place of birth in national ID).
 * <p>
 * Lookups available by national ID code ({@link #fromNationalGovernorateCode(String)})
 * and by ISO 3166-2 subdivision code ({@link #fromIsoSubdivisionCode(String)}).
 * Validators by these two codes also available.
 * <p>
 * Also accepts Foreign Born for non-Egyptians.
 *
 * @see Region
 */
public enum Governorate {
    // Greater Cairo
    CAIRO("01", "C", "Cairo", "القاهرة", Region.GREATER_CAIRO),
    QALYUBIA("14", "KB", "Qalyubia", "القليوبية", Region.GREATER_CAIRO),
    GIZA("21", "GZ", "Giza", "الجيزة", Region.GREATER_CAIRO),

    // Alexandria
    ALEXANDRIA("02", "ALX", "Alexandria", "الإسكندرية", Region.ALEXANDRIA),
    BEHEIRA("18", "BH", "Beheira", "البحيرة", Region.ALEXANDRIA),

    // Suez Canal
    PORT_SAID("03", "PTS", "Port Said", "بورسعيد", Region.SUEZ_CANAL),
    SUEZ("04", "SUZ", "Suez", "السويس", Region.SUEZ_CANAL),
    ISMAILIA("19", "IS", "Ismailia", "الإسماعيلية", Region.SUEZ_CANAL),

    // Delta
    DAMIETTA("11", "DT", "Damietta", "دمياط", Region.DELTA), DAKAHLIA("12", "DK", "Dakahlia", "الدقهلية", Region.DELTA),
    SHARKIA("13", "SHR", "Sharkia", "الشرقية", Region.DELTA),
    KAFR_EL_SHEIKH("15", "KFS", "Kafr El Sheikh", "كفر الشيخ", Region.DELTA),
    GHARBIA("16", "GH", "Gharbia", "الغربية", Region.DELTA),
    MENOUFIA("17", "MNF", "Menoufia", "المنوفية", Region.DELTA),

    // Upper Egypt North
    BENI_SUEF("22", "BNS", "Beni Suef", "بني سويف", Region.UPPER_EGYPT_NORTH),
    FAYOUM("23", "FYM", "Fayoum", "الفيوم", Region.UPPER_EGYPT_NORTH),
    MINYA("24", "MN", "Minya", "المنيا", Region.UPPER_EGYPT_NORTH),

    // Upper Egypt South
    ASSIUT("25", "AST", "Assiut", "أسيوط", Region.UPPER_EGYPT_SOUTH),
    SOHAG("26", "SHG", "Sohag", "سوهاج", Region.UPPER_EGYPT_SOUTH),
    QENA("27", "KN", "Qena", "قنا", Region.UPPER_EGYPT_SOUTH),
    ASWAN("28", "ASN", "Aswan", "أسوان", Region.UPPER_EGYPT_SOUTH),
    LUXOR("29", "LX", "Luxor", "الأقصر", Region.UPPER_EGYPT_SOUTH),

    // Frontier
    RED_SEA("31", "BA", "Red Sea", "البحر الأحمر", Region.FRONTIER),
    NEW_VALLEY("32", "WAD", "New Valley", "الوادي الجديد", Region.FRONTIER),
    MATROUH("33", "MT", "Matrouh", "مطروح", Region.FRONTIER),
    NORTH_SINAI("34", "SIN", "North Sinai", "شمال سيناء", Region.FRONTIER),
    SOUTH_SINAI("35", "JS", "South Sinai", "جنوب سيناء", Region.FRONTIER),

    // Special
    FOREIGN_BORN("88", "FOREIGN", "Foreign Born", "مولود بالخارج", Region.FOREIGN);

    private final String nationalGovernorateCode;
    private final String isoSubdivisionCode;
    private final String englishName;
    private final String arabicName;
    private final Region region;

    Governorate(String nationalGovernorateCode, String isoSubdivisionCode, String englishName,
                String arabicName, Region region) {
        this.nationalGovernorateCode = nationalGovernorateCode;
        this.isoSubdivisionCode = isoSubdivisionCode;
        this.englishName = englishName;
        this.arabicName = arabicName;
        this.region = region;
    }

    // getters
    public String getNationalGovernorateCode() {
        return nationalGovernorateCode;
    }

    public String getIsoSubdivisionCode() {
        return isoSubdivisionCode;
    }

    public String getEnglishName() {
        return englishName;
    }

    public String getArabicName() {
        return arabicName;
    }

    public Region getRegion() {
        return region;
    }

    public String getFullIsoCode() {
        return "EG-" + isoSubdivisionCode;
    }

    // static lookup maps
    private static final Map<String, Governorate> BY_NATIONAL_GOVERNORATE_CODE = Arrays.stream(values())
            .collect(Collectors.toMap(Governorate::getNationalGovernorateCode, Function.identity()));

    private static final Map<String, Governorate> BY_ISO_SUBDIVISION_CODE = Arrays.stream(values())
            .collect(Collectors.toMap(Governorate::getIsoSubdivisionCode, Function.identity()));

    // factory methods
    public static @NonNull Governorate fromNationalGovernorateCode(String code) {
        Objects.requireNonNull(code, "code must not be null");
        Governorate governorate = BY_NATIONAL_GOVERNORATE_CODE.get(code);
        if (governorate == null)
            throw new IllegalArgumentException("Unknown national governate code: " + code);
        return governorate;
    }

    public static @NonNull Governorate fromIsoSubdivisionCode(String code) {
        Objects.requireNonNull(code, "code must not be null");
        Governorate governorate = BY_ISO_SUBDIVISION_CODE.get(code);
        if (governorate == null)
            throw new IllegalArgumentException("Unknown ISO subdivision code: " + code);
        return governorate;
    }

    public static boolean isValidNationalGovernorateCode(String code) {
        return BY_NATIONAL_GOVERNORATE_CODE.containsKey(code);
    }

    public static boolean isValidIsoSubdivisionCode(String code) {
        return BY_ISO_SUBDIVISION_CODE.containsKey(code);
    }
}
