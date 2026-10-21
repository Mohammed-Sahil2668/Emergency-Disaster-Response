package com.emergency.response.models;

public class EmergencyCategory {
    private String id;
    private String name;
    private int iconResId;
    private int accentColorResId;
    private int bgColorResId;
    private String defaultDescription;

    public EmergencyCategory(String id, String name, int iconResId, int accentColorResId, int bgColorResId, String defaultDescription) {
        this.id = id;
        this.name = name;
        this.iconResId = iconResId;
        this.accentColorResId = accentColorResId;
        this.bgColorResId = bgColorResId;
        this.defaultDescription = defaultDescription;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getIconResId() {
        return iconResId;
    }

    public int getAccentColorResId() {
        return accentColorResId;
    }

    public int getBgColorResId() {
        return bgColorResId;
    }

    public String getDefaultDescription() {
        return defaultDescription;
    }

    @Override
    public String toString() {
        return name;
    }
}
