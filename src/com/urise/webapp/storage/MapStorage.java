package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

public class MapStorage extends AbstractStorage {

    TreeMap<String, Resume> storage = new TreeMap<>();

    @Override
    protected boolean isExist(Object key) {
        if (key != null) {
            return true;
        }
        return false;
    }

    @Override
    protected Object getSearchKey(String uuid) {
        if (storage.containsKey(uuid)) {
            return uuid;
        }
        return null;
    }

    @Override
    protected Resume doGet(Object searchKey) {
        return storage.get(searchKey);
    }

    @Override
    protected void doSave(Object searchKey, Resume r) {
        storage.put(r.getUuid(), r);
    }

    @Override
    protected void doUpdate(Object searchKey, Resume r) {
        storage.put((String) searchKey, r);
    }

    @Override
    protected void doDelete(Object searchKey) {
        storage.remove(searchKey);
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public Resume[] getAll() {
        Resume[] resumes = new Resume[0];
       resumes = Arrays.copyOf(storage.values().toArray(resumes), size());
       for (Resume resume: resumes) System.out.println(resume.getUuid());
        return Arrays.copyOf(storage.values().toArray(new Resume[0]), size());
    }

    @Override
    public int size() {
        if (storage == null) {
            return 0;
        } else {
            return storage.size();
        }
    }
}
