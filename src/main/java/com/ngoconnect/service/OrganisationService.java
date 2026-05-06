package com.ngoconnect.service;

import com.ngoconnect.exception.NGOConnectException;
import com.ngoconnect.model.*;
import com.ngoconnect.repository.OrganisationRepository;
import com.ngoconnect.util.Validator;

import java.util.List;
import java.util.Optional;

public class OrganisationService {

    private final OrganisationRepository repo;

    public OrganisationService(OrganisationRepository repo) {
        this.repo = repo;
    }

    public Organisation register(String name, String description, OrgType type,
                                 String location, String state, ContactInfo contactInfo,
                                 int foundedYear, String areaOfWork) {
        Validator.requireNonEmpty(name, "Name");
        Validator.requireNonEmpty(description, "Description");
        Validator.requireNonEmpty(location, "Location");
        Validator.requireNonEmpty(state, "State");
        Validator.requireNonEmpty(areaOfWork, "Area of Work");
        Validator.requirePositiveYear(foundedYear, "Founded Year");
        Validator.requireValidEmail(contactInfo.getEmail());

        Organisation org = new Organisation(name, description, type, location, state,
                contactInfo, foundedYear, areaOfWork);
        repo.add(org);
        return org;
    }

    public List<Organisation> search(String keyword) {
        Validator.requireNonEmpty(keyword, "Search keyword");
        return repo.search(keyword);
    }

    public List<Organisation> filterByCategory(Category category) {
        if (category == null) throw new NGOConnectException("Category cannot be null.");
        return repo.findByCategory(category);
    }

    public List<Organisation> filterByCategoryAndLocation(Category category, String location) {
        Validator.requireNonEmpty(location, "Location");
        return repo.findByCategoryAndLocation(category, location);
    }

    public List<Organisation> getAll() {
        return repo.findAll();
    }

    public Optional<Organisation> findById(String id) {
        return repo.findById(id);
    }

    public Organisation getById(String id) {
        return repo.getById(id);
    }

    public void addReview(String orgId, String reviewerName, int rating, String comment) {
        Organisation org = repo.getById(orgId);
        Review review = new Review(reviewerName, rating, comment);
        org.addReview(review);
        repo.update(org);
    }

    public void expressInterest(String orgId, User user) {
        Organisation org = repo.getById(orgId);
        user.connectToOrg(orgId);
        System.out.printf("Interest expressed in '%s'. The organisation will be notified.%n", org.getName());
    }

    public List<Organisation> recommend(List<Category> interests) {
        if (interests == null || interests.isEmpty()) return repo.findAll();
        return repo.recommend(interests);
    }

    public List<Organisation> getTopRated(int n) {
        return repo.getTopRated(n);
    }

    public void verify(String orgId, boolean status) {
        Organisation org = repo.getById(orgId);
        org.setVerified(status);
        repo.update(org);
    }

    public void addEventToOrg(String orgId, Event event) {
        Organisation org = repo.getById(orgId);
        org.addEvent(event);
        repo.update(org);
    }

    public int count() { return repo.count(); }
}
