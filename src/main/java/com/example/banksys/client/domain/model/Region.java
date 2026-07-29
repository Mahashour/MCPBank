package com.example.banksys.client.domain.model;

/**
 * An Egyptian economic region.
 * Each constant provides both English and Arabic display.
 * Also accepts Foreign for non-Egyptians.
 */
public enum Region {
    GREATER_CAIRO("Greater Cairo", "القاهرة الكبرى"), ALEXANDRIA("Alexandria", "الإسكندرية"),
    SUEZ_CANAL("Suez Canal", "قناة السويس"), DELTA("Delta", "الدلتا"),
    UPPER_EGYPT_NORTH("Upper Egypt North", "شمال الصعيد"), UPPER_EGYPT_SOUTH("Upper Egypt South", "جنوب الصعيد"),
    FRONTIER("Frontier", "الحدود"), FOREIGN("Outside the Republic", "خارج الجمهورية");

    private final String englishName;
    private final String arabicName;

    Region(String englishName, String arabicName) {
        this.englishName = englishName;
        this.arabicName = arabicName;
    }

    public String getEnglishName() {
        return englishName;
    }

    public String getArabicName() {
        return arabicName;
    }
}
