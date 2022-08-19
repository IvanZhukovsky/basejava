package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

public class MapStorage extends AbstractStorage {

    Map<String, Resume> storage = new TreeMap<>();

    @Override
    protected boolean isExist(Object key) {
        return storage.containsKey(key);
    }

    @Override
    protected Object getSearchKey(String uuid) {
//        if (storage.containsKey(uuid)) {
//            return uuid;
//        }
        return uuid;
    }

    @Override
    protected Resume doGet(Object searchKey) {
        return storage.get((String) searchKey);
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
        storage.remove((String) searchKey);
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public Resume[] getAll() {
        return Arrays.copyOf(storage.values().toArray(new Resume[0]), size());
    }

    @Override
    public int size() {
        return storage.size();
    }
}
