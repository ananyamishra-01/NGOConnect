package com.ngoconnect.model;

public enum Category {
    OLD_AGE_SUPPORT("Old Age Support", "SDG 3"),
    WOMEN_EMPOWERMENT("Women Empowerment", "SDG 5, SDG 8"),
    CHILD_WELFARE("Child Welfare", "SDG 4, SDG 3"),
    ANIMAL_WELFARE("Animal Welfare", "SDG 15"),
    ENVIRONMENTAL_CONSERVATION("Environmental Conservation", "SDG 13, SDG 15"),
    SPECIALLY_ABLED("Support for Specially-Abled Individuals", "SDG 10, SDG 11"),
    EDUCATION("Education & Literacy", "SDG 4"),
    HEALTH("Health & Well-being", "SDG 3"),
    CLEAN_WATER("Clean Water & Sanitation", "SDG 6"),
    LIVELIHOOD("Livelihood & Economic Empowerment", "SDG 8"),
    DISASTER_RELIEF("Disaster Relief", "SDG 11"),
    RURAL_DEVELOPMENT("Rural Development", "SDG 11, SDG 9");

    private final String displayName;
    private final String sdgAlignment;

    Category(String displayName, String sdgAlignment) {
        this.displayName = displayName;
        this.sdgAlignment = sdgAlignment;
    }

    public String getDisplayName() { return displayName; }
    public String getSdgAlignment() { return sdgAlignment; }

    @Override
    public String toString() { return displayName; }
}
