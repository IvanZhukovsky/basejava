package com.urise.webapp.storage;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AbstractFileStorage extends AbstractStorage<File> {
    private final File directory;

    File[] files;

    protected AbstractFileStorage(File directory) {
        Objects.requireNonNull(directory, "directory must not be null");
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + "is not directory");
        }
        if (!directory.canRead() || !directory.canWrite()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + "is readable/writable");
        }
        this.directory = directory;
        files = directory.listFiles();
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
            return doRead(new BufferedInputStream(new FileInputStream(file)));
        } catch (IOException e) {
            throw new StorageException("IO error", file.getName(), e);
        }
    }

    @Override
    protected void doSave(File file, Resume r) {
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new StorageException("IO error", file.getName(), e);
        }
            doUpdate(file, r);
    }

    @Override
    protected void doUpdate(File file, Resume r) {
        try {
            doWrite(r, new BufferedOutputStream(new FileOutputStream(file)));
        } catch (IOException e) {
            throw new StorageException("IO error", file.getName(), e);
        }
    }

    @Override
    protected void doDelete(File file) {

        file.delete();
//        if (!file.delete()) {
//            throw new StorageException("file delete error", file.getName());
//        }
    }

    @Override
    protected List<Resume> doCopyAll() {
        ArrayList<Resume> resumes = new ArrayList<>();
        if (files == null) {
            throw new StorageException("listFiles is null", directory.getName());
        }
        for (File file : files) {
                resumes.add(doGet(file));
        }
        return resumes;
    }

    @Override
    public void clear() {
        if (files == null) {
            throw new StorageException("listFiles is null", directory.getName());
        }
        for (File file : files) {
            doDelete(file);
        }
    }

    @Override
    public int size() {
        String[] list = directory.list();
        if (list == null) {
            throw new StorageException("Directory read error", null);
        }
        return list.length;
    }

    protected abstract Resume doRead(InputStream file) throws IOException;

    protected abstract void doWrite(Resume resume, OutputStream file) throws IOException;
}
