package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage{

    //protected Resume[] sortedArray = Arrays.copyOf(storage, STORAGE_LIMIT);

    @Override
    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    @Override
    public void update(Resume r) {
        int index = getIndex(r.getUuid());
        if (index == -1) {
            System.out.printf("Резюме %s отсуствует в базе данных%n", r.getUuid());
        } else {
            storage[index] = r;
        }
    }

    @Override
    public void save(Resume r) {
        if (size >= STORAGE_LIMIT) {
            System.out.println("База резюме переполнена, запись невозможна");
        } else if (getIndex(r.getUuid()) >= 0) {
            System.out.printf("Резюме %s уже присутствует в базе данных%n", r.getUuid());
        } else {
            storage[size] = r;
            size++;
        }
    }

    @Override
    public void delete(String uuid) {
        int index = getIndex(uuid);
        if (index == -1) {
            System.out.printf("Резюме %s отсуствует в базе данных%n", uuid);
        } else {
            storage[index] = storage[size - 1];
            storage[size - 1] = null;
            size--;
        }
    }

    @Override
    public Resume[] getAll() {
        sortArray();
        return Arrays.copyOf(storage, size);
    }

    @Override
    protected int getIndex(String uuid) {
        Resume searchKey = new Resume();
        searchKey.setUuid(uuid);
        sortArray();
        return Arrays.binarySearch(storage, 0, size, searchKey);
    }

    private void sortArray() {
        for (int i = 1; i < size; i++) {
            Resume buffer = storage[i];
            int index = Arrays.binarySearch(storage, 0, i, buffer) * (-1) - 1;
            System.arraycopy(storage, index, storage, index + 1, i - index);
            storage[index] = buffer;
        }
    }
}
