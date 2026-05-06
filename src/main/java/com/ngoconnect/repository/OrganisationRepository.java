package com.ngoconnect.repository;

import com.ngoconnect.exception.OrganisationNotFoundException;
import com.ngoconnect.model.Category;
import com.ngoconnect.model.Organisation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class OrganisationRepository {

    private List<Organisation> store;

    public OrganisationRepository() {
        this.store = FileRepository.loadOrganisations();
    }

    public void add(Organisation org) {
        store.add(org);
        persist();
    }

    public void update(Organisation org) {
        persist();
    }

    public void delete(String id) {
        boolean removed = store.removeIf(o -> o.getId().equals(id));
        if (!removed) throw new OrganisationNotFoundException(id);
        persist();
    }

    public Optional<Organisation> findById(String id) {
        return store.stream().filter(o -> o.getId().equalsIgnoreCase(id)).findFirst();
    }

    public Organisation getById(String id) {
        return findById(id).orElseThrow(() -> new OrganisationNotFoundException(id));
    }

    public List<Organisation> findAll() {
        return new ArrayList<>(store);
    }

    public List<Organisation> findByCategory(Category category) {
        return store.stream()
                .filter(o -> o.getCategories().contains(category))
                .collect(Collectors.toList());
    }

    public List<Organisation> search(String keyword) {
        String kw = keyword.toLowerCase().trim();
        return store.stream().filter(o ->
                o.getName().toLowerCase().contains(kw) ||
                o.getDescription().toLowerCase().contains(kw) ||
                o.getAreaOfWork().toLowerCase().contains(kw) ||
                o.getLocation().toLowerCase().contains(kw) ||
                o.getState().toLowerCase().contains(kw) ||
                o.getCategories().stream().anyMatch(c -> c.getDisplayName().toLowerCase().contains(kw))
        ).collect(Collectors.toList());
    }

    public List<Organisation> findByLocation(String location) {
        String loc = location.toLowerCase().trim();
        return store.stream().filter(o ->
                o.getLocation().toLowerCase().contains(loc) ||
                o.getState().toLowerCase().contains(loc)
        ).collect(Collectors.toList());
    }

    public List<Organisation> findVerified() {
        return store.stream().filter(Organisation::isVerified).collect(Collectors.toList());
    }

    public List<Organisation> findByCategoryAndLocation(Category category, String location) {
        String loc = location.toLowerCase().trim();
        return store.stream().filter(o ->
                o.getCategories().contains(category) &&
                (o.getLocation().toLowerCase().contains(loc) || o.getState().toLowerCase().contains(loc))
        ).collect(Collectors.toList());
    }

    public List<Organisation> getTopRated(int n) {
        return store.stream()
                .filter(o -> !o.getReviews().isEmpty())
                .sorted((a, b) -> Double.compare(b.getAverageRating(), a.getAverageRating()))
                .limit(n)
                .collect(Collectors.toList());
    }

    public List<Organisation> recommend(List<Category> interests) {
        return store.stream()
                .filter(o -> o.getCategories().stream().anyMatch(interests::contains))
                .sorted((a, b) -> Double.compare(b.getAverageRating(), a.getAverageRating()))
                .collect(Collectors.toList());
    }

    public int count() { return store.size(); }

    private void persist() {
        FileRepository.saveOrganisations(store);
    }
}
