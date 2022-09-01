package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage extends AbstractArrayStorage {

    @Override
    protected int getIndexForSave(Resume r) {
        return size;
    }

    @Override
    protected void fillAfterDelete(int index) {
        storage[index] = storage[size - 1];
        storage[size - 1] = null;
    }

    @Override
    protected Object getSearchKey(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public List<Resume> getAllSorted() {
        List<Resume> list = Arrays.asList(Arrays.copyOf(storage, size));
        Comparator<Resume> comparator = (a, b) -> a.getFullName().compareTo(b.getFullName());
        list.sort(comparator.thenComparing((a,b) -> a.getUuid().compareTo(b.getUuid())));
        return list;
    }
}
