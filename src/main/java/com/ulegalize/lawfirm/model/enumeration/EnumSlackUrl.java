package com.ulegalize.lawfirm.model.enumeration;

public enum EnumSlackUrl {
    REMINDER_EMAIL_SUPPORT("Email support (reminder)"),
    NEW_ARRIVAL("New arrival for lawfirm"),
    INFO("Info"),
    CALENDAR_SCHEDULER("Calendar scheduler"),
    SENSITIVE("Issue within ulegalize-lawfirm");

    private final String description;

    EnumSlackUrl(String description) {
        this.description = description;
    }


    public static EnumSlackUrl fromName(String name) {
        for (EnumSlackUrl enumSlackUrl : values()) {
            if (enumSlackUrl.name().equalsIgnoreCase(name))
                return enumSlackUrl;
        }
        return null;
    }

    public String getDescription() {
        return description;
    }
}
