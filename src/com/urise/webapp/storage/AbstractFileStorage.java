package com.urise.webapp.storage;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AbstractFileStorage extends AbstractStorage<File> {
    private final File directory;

    protected AbstractFileStorage(File directory) {
        Objects.requireNonNull(directory, "directory must not be null");
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + "is not directory");
        }
        if (!directory.canRead() || !directory.canWrite()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + "is readable/writable");
        }
        this.directory = directory;
    }

    @Override
    protected boolean isExist(File file) {
        return file.exists();
    }

    @Override
    protected File getSearchKey(String uuid) {
        return new File(directory, uuid);
    }

    @Override
    protected Resume doGet(File file) {
        try {
            return doRead(file);
        } catch (IOException e) {
            throw new StorageException("IO error", file.getName(), e);
        }
    }

    @Override
    protected void doSave(File file, Resume r) {
        try {
            file.createNewFile();
            doWrite(r, file);
        } catch (IOException e) {
            throw new StorageException("IO error", file.getName(), e);
        }
    }

    @Override
    protected void doUpdate(File file, Resume r) {
        try {
            doWrite(r, file);
        } catch (IOException e) {
            throw new StorageException("IO error", file.getName(), e);
        }
    }

    @Override
    protected void doDelete(File file) {
        if (!file.delete()) {
            throw new StorageException("IO error", file.getName());
        }
    }

    @Override
    protected List<Resume> doCopyAll() {
        ArrayList<Resume> resumes = new ArrayList<>();
        if (directory.listFiles() != null) {
            throw new StorageException("listFiles is null", directory.getName());
        }
        for (File file : directory.listFiles()) {
            try {
                resumes.add(doRead(file));
            } catch (IOException e) {
                throw new StorageException("IO error", file.getName(), e);
            }
        }
        return resumes;
    }

    @Override
    public void clear() {
        if (directory.listFiles() != null) {
            throw new StorageException("listFiles is null", directory.getName());
        }
        for (File file : directory.listFiles()) {
            doDelete(file);
        }
    }

    @Override
    public int size() {
        if (directory.listFiles() != null) {
            throw new StorageException("listFiles is null", directory.getName());
        }
        return directory.listFiles().length;
    }

    protected abstract Resume doRead(File file) throws IOException;

    protected abstract void doWrite(Resume resume, File file) throws IOException;
}
