package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.Resume;
import com.urise.webapp.storage.IOStrategy.StreamSerializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Comparator;
import java.util.List;

public abstract class AbstractStorage<SK> implements Storage {

    private static final Comparator<Resume> comparator = Comparator.comparing(Resume::getFullName)
            .thenComparing(Resume::getUuid);

    private SK getExistingSearchKey(String uuid) {
        SK searchKey = getSearchKey(uuid);
        if (!isExist(searchKey)) {
            throw new NotExistStorageException(uuid);
        }
        return searchKey;
    }

    private SK getNotExistingSearchKey(String uuid) {
        SK searchKey = getSearchKey(uuid);
        if (isExist(searchKey)) {
            throw new ExistStorageException(uuid);
        }
        return searchKey;
    }

    @Override
    final public Resume get(String uuid) {
        SK searchKey = getExistingSearchKey(uuid);
        return doGet(searchKey);
    }

    @Override
    final public void save(Resume r) {
        SK searchKey = getNotExistingSearchKey(r.getUuid());
        doSave(searchKey, r);
    }

    @Override
    final public void update(Resume r) {
        SK searchKey = getExistingSearchKey(r.getUuid());
        doUpdate(searchKey, r);
    }

    @Override
    final public void delete(String uuid) {
        SK searchKey = getExistingSearchKey(uuid);
        doDelete(searchKey);
    }

    @Override
    final public List<Resume> getAllSorted() {
        List<Resume> list = doCopyAll();
//        Comparator<Resume> comparator = Comparator.comparing(Resume::getFullName)
//                .thenComparing(Resume::getUuid);
        list.sort(comparator);
        return list;
    }

    protected abstract boolean isExist(SK key);

    protected abstract SK getSearchKey(String uuid);

    protected abstract Resume doGet(SK searchKey);

    protected abstract void doSave(SK searchKey, Resume r);

    protected abstract void doUpdate(SK searchKey, Resume r);

    protected abstract void doDelete(SK searchKey);

    protected abstract List<Resume> doCopyAll();

}
