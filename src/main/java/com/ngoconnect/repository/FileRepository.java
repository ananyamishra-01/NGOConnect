package com.ngoconnect.repository;

import com.ngoconnect.exception.NGOConnectException;
import com.ngoconnect.model.Organisation;
import com.ngoconnect.model.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles data persistence using Java Serialization (file-based).
 */
public class FileRepository {

    private static final String DATA_DIR  = "data/";
    private static final String ORG_FILE  = DATA_DIR + "organisations.dat";
    private static final String USER_FILE = DATA_DIR + "users.dat";

    static {
        new File(DATA_DIR).mkdirs();
    }

    // ── Organisation Persistence ─────────────────────────────

    @SuppressWarnings("unchecked")
    public static List<Organisation> loadOrganisations() {
        File file = new File(ORG_FILE);
        if (!file.exists()) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Organisation>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new NGOConnectException("Failed to load organisation data.", e);
        }
    }

    public static void saveOrganisations(List<Organisation> list) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ORG_FILE))) {
            oos.writeObject(list);
        } catch (IOException e) {
            throw new NGOConnectException("Failed to save organisation data.", e);
        }
    }

    // ── User Persistence ──────────────────────────────────────

    @SuppressWarnings("unchecked")
    public static List<User> loadUsers() {
        File file = new File(USER_FILE);
        if (!file.exists()) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new NGOConnectException("Failed to load user data.", e);
        }
    }

    public static void saveUsers(List<User> list) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USER_FILE))) {
            oos.writeObject(list);
        } catch (IOException e) {
            throw new NGOConnectException("Failed to save user data.", e);
        }
    }
}
