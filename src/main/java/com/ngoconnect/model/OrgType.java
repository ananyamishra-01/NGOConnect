package com.ngoconnect.model;

public enum OrgType {
    NGO("Non-Governmental Organisation"),
    SHG("Self-Help Group"),
    TRUST("Charitable Trust"),
    SOCIETY("Registered Society"),
    FOUNDATION("Foundation");

    private final String label;

    OrgType(String label) { this.label = label; }

    public String getLabel() { return label; }

    @Override
    public String toString() { return label; }
}
