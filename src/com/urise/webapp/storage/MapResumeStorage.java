package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.*;

public class MapResumeStorage extends AbstractStorage {

    Map<String, Resume> storage = new TreeMap<>();

    @Override
    protected boolean isExist(Object key) {
        return storage.containsValue(key);
    }

    @Override
    protected Object getSearchKey(String uuid) {
        return storage.get(uuid);
    }

    @Override
    protected Resume doGet(Object searchKey) {
        Resume resume = (Resume) searchKey;
        return storage.get(resume.getUuid());
    }

    @Override
    protected void doSave(Object searchKey, Resume r) {
        storage.put(r.getUuid(), r);
    }

    @Override
    protected void doUpdate(Object searchKey, Resume r) {
        Resume resume = (Resume) searchKey;
        storage.put(resume.getUuid(), r);
    }

    @Override
    protected void doDelete(Object searchKey) {
        Resume resume = (Resume) searchKey;
        storage.remove(resume.getUuid());
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
