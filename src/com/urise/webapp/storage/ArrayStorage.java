package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private static final int STORAGE_LIMIT = 10000;
    private final Resume[] storage = new Resume[STORAGE_LIMIT];
    private int size = 0;

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public void save(Resume r) {

        if (size == STORAGE_LIMIT) {
            System.out.println("База резюме переполнена, запись невозможна");
        } else {
            if (findIndex(r.getUuid()) == -1) {
                storage[size] = r;
                size++;
            } else {
                System.out.printf("Резюме %s уже присутствует в базе данных%n", r.getUuid());
            }
        }
    }

    public Resume get(String uuid) {
        int index = findIndex(uuid);
        if (index == -1) {
            System.out.printf("Резюме %s отсуствует в базе данных%n", uuid);
            return null;
        } else {
            return storage[index];
        }
    }

    public void delete(String uuid) {
        int index = findIndex(uuid);
        if (index == -1) {
            System.out.printf("Резюме %s отсуствует в базе данных%n", uuid);
        } else {
            storage[index] = storage[size - 1];
            storage[size - 1] = null;
            size--;
        }
    }

    public void update(Resume resume) {
        int index = findIndex(resume.getUuid());
        if (index == -1) {
            System.out.printf("Резюме %s отсуствует в базе данных%n", resume.getUuid());
        } else {
            storage[index] = resume;
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    public Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    public int size() {
        return size;
    }

    private int findIndex(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }
}
