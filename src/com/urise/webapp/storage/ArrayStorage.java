package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    Resume[] storage = new Resume[10000];
    int size = 0;

    public void clear() {
        Arrays.fill(storage, 0, size - 1, null);
        size = 0;
    }

    public void save(Resume r) {
        if (size < 10000) {
            if (checkPresent(r.uuid) == -1) {
                storage[size] = r;
                size++;
            } else {
                System.out.printf("Резюме %s уже присутствует в базе данных%n", r.uuid);
            }
        } else {
            System.out.println("База резюме переполнена, запись невозможна");
        }
    }

    public Resume get(String uuid) {
        int index = checkPresentAndPrint(uuid);
        if (index != -1) {
            return storage[index];
        }
        return null;
    }

    public void delete(String uuid) {
        int index = checkPresentAndPrint(uuid);
        if (index != -1) {
            System.arraycopy(storage, index + 1, storage, index, size - index - 1);
            storage[size - 1] = null;
            size--;
        }
    }

    public void update(Resume resume) {
        int index = checkPresentAndPrint(resume.uuid);
        if (index != -1) {
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

    private int checkPresent(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].uuid.equals(uuid)) {
                return i;
            }
        }
        return -1;
    }
    private int checkPresentAndPrint(String uuid) {
        int result = checkPresent(uuid);
        if (result == -1) {
            System.out.printf("Резюме %s отсуствует в базе данных%n", uuid);
            return result;
        }
        else {
            return result;
        }
    }
}
