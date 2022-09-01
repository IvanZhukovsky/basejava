package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.*;

public class MapUuidStorage extends AbstractStorage {

    Map<String, Resume> storage = new TreeMap<>();

    @Override
    protected boolean isExist(Object key) {
        return storage.containsKey((String) key);
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
    public List<Resume> getAllSorted() {
        List<Resume> list = new ArrayList<>(storage.values());
        Comparator<Resume> comparator = (a, b) -> a.getFullName().compareTo(b.getFullName());
        list.sort(comparator.thenComparing((a,b) -> a.getUuid().compareTo(b.getUuid())));
        return list;
    }

    @Override
    public int size() {
        return storage.size();
    }


}
