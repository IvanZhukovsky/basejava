package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage{

    @Override
    protected Object getSearchKey(String uuid) {
        Resume searchKey = new Resume(uuid);
        return Arrays.binarySearch(storage, 0, size, searchKey);
    }

    @Override
    protected int getIndexForSave(Resume r) {
        int index = Arrays.binarySearch(storage, 0, size, r) * (-1) - 1;
        System.arraycopy(storage, index, storage, index + 1, size - index);
        return index;
    }

    @Override
    protected void fillAfterDelete(int index) {
        System.arraycopy(storage, index + 1, storage, index, size - index);
    }
}
