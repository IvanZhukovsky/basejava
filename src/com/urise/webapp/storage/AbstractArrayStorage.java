package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;

public abstract class AbstractArrayStorage implements Storage {
    protected static final int STORAGE_LIMIT = 10000;

    protected Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int size = 0;

    final public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    final public void update(Resume resume) {
        int index = getIndex(resume.getUuid());
        if (index == -1) {
            System.out.printf("Резюме %s отсуствует в базе данных%n", resume.getUuid());
        } else {
            storage[index] = resume;
        }
    }

    final public void save(Resume r) {
        if (size >= STORAGE_LIMIT) {
            System.out.println("База резюме переполнена, запись невозможна");
        } else if (getIndex(r.getUuid()) >= 0) {
            System.out.printf("Резюме %s уже присутствует в базе данных%n", r.getUuid());
        } else {
            storage[getIndexForSave(r)] = r;
            size++;
        }
    }

    final public void delete(String uuid) {
        int index = getIndex(uuid);
        if (index == -1) {
            System.out.printf("Резюме %s отсуствует в базе данных%n", uuid);
        } else {
            fillAfterDelete(index);
            size--;
        }
    }

    final public Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    final public int size() {
        return size;
    }

    final public Resume get(String uuid) {
        int index = getIndex(uuid);
        if (index == -1) {
            System.out.println("Resume " + uuid + " not exist");
            return null;
        }
        return storage[index];
    }

    protected abstract int getIndex(String uuid);

    protected abstract int getIndexForSave(Resume r);

    protected abstract void fillAfterDelete(int index);
}
