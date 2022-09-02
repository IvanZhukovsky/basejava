package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractArrayStorage extends AbstractStorage {

    protected static final int STORAGE_LIMIT = 10000;
    protected int size = 0;
    protected Resume[] storage = new Resume[STORAGE_LIMIT];


    final public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    final public int size() {
        return size;
    }

    @Override
    protected Resume doGet(Object searchKey) {
        int index = (int) searchKey;
        return storage[index];
    }

    @Override
    protected void doSave(Object searchKey, Resume r) {
        if (size >= STORAGE_LIMIT) {
            throw new StorageException("Storage overflow", r.getUuid());
        } else {
            storage[getIndexForSave(r)] = r;
            size++;
        }
    }

    @Override
    protected void doUpdate(Object searchKey, Resume r) {
        int index = (int) searchKey;
        storage[index] = r;
    }

    @Override
    protected void doDelete(Object searchKey) {
        int index = (int) searchKey;
        fillAfterDelete(index);
        size--;
    }

    @Override
    protected List doList() {
        return Arrays.asList(Arrays.copyOf(storage, size));
    }

    @Override
    protected boolean isExist(Object key) {
        return (int) key >= 0;
    }

    protected abstract int getIndexForSave(Resume r);

    protected abstract void fillAfterDelete(int index);


}
