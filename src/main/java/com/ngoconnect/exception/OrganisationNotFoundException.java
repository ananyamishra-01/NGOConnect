package com.ngoconnect.exception;

public class OrganisationNotFoundException extends NGOConnectException {
    public OrganisationNotFoundException(String id) {
        super("Organisation with ID '" + id + "' was not found.");
    }
}
