package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class ListStorage extends AbstractStorage {

    protected ArrayList<Resume> storage = new ArrayList<>();

    @Override
    protected boolean isExist(Object key) {
        return (int) key >= 0;
    }

    @Override
    protected Object getSearchKey(String uuid) {
        for (int i = 0; i < storage.size(); i++) {
            if (storage.get(i).getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    protected Resume doGet(Object searchKey) {
        int index = (int) searchKey;
        return storage.get(index);
    }

    @Override
    protected void doSave(Object searchKey, Resume r) {
        storage.add(r);
    }

    @Override
    protected void doUpdate(Object searchKey, Resume r) {
        int index = (int) searchKey;
        storage.add(index, r);
    }

    @Override
    protected void doDelete(Object searchKey) {
        int index = (int) searchKey;
        storage.remove(index);
    }

    @Override
    final public void clear() {
        storage.clear();
    }

    @Override
    final public List<Resume> getAllSorted() {
        List<Resume> list = storage;
        Comparator<Resume> comparator = (a,b) -> a.getFullName().compareTo(b.getFullName());
        list.sort(comparator.thenComparing((a,b) -> a.getUuid().compareTo(b.getUuid())));
        //list.sort(comparator);
        return list;
    }

    @Override
    final public int size() {
        if (storage == null) {
            return 0;
        } else {
            return storage.size();
        }
    }

    class FullNameComparator implements Comparator<Resume>{

        public int compare(Resume a, Resume b){

            return a.getFullName().compareTo(b.getFullName());
        }
    }
}
