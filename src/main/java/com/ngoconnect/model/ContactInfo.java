package com.ngoconnect.model;

import java.io.Serializable;

public class ContactInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String phone;
    private String email;
    private String website;
    private String address;

    public ContactInfo(String phone, String email, String website, String address) {
        this.phone = phone;
        this.email = email;
        this.website = website;
        this.address = address;
    }

    public String getPhone()   { return phone; }
    public String getEmail()   { return email; }
    public String getWebsite() { return website; }
    public String getAddress() { return address; }

    public void setPhone(String phone)     { this.phone = phone; }
    public void setEmail(String email)     { this.email = email; }
    public void setWebsite(String website) { this.website = website; }
    public void setAddress(String address) { this.address = address; }

    @Override
    public String toString() {
        return String.format("Phone: %s | Email: %s | Website: %s%nAddress: %s",
                phone, email, website, address);
    }
}
