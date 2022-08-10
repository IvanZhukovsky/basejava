package com.urise.webapp.storage;

import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.Resume;

import java.util.ArrayList;
import java.util.Arrays;

public class ListStorage extends AbstractStorage {

    protected ArrayList<Resume> storage = new ArrayList<>();

    @Override
    final public void clear() {
        storage.clear();
        size = 0;
    }

    @Override
    final public Resume get(String uuid) {
        int index = getIndex(uuid);
        if (index < 0) {
            throw new NotExistStorageException(uuid);
        }
        return storage.get(getIndex(uuid));
    }

    @Override
    final public Resume[] getAll() {
        Resume[] resumes = new Resume[10000];
        return Arrays.copyOf(storage.toArray(resumes), size());
    }

    @Override
    final public int size() {
        if (storage == null) {
            return 0;
        } else {
            return storage.size();
        }
    }

    @Override
    protected int getIndex(String uuid) {
        for (int i = 0; i < storage.size(); i++) {
            if (storage.get(i).getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    protected void updateElement(int index, Resume r) {
        storage.add(index, r);
    }

    @Override
    protected void addElement(Resume r) {
        storage.add(r);
    }

    @Override
    protected void deleteElement(int index) {
        storage.remove(index);
    }
}
