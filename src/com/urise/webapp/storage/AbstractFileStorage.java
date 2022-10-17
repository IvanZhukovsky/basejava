package com.urise.webapp.storage;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;

import java.io.*;
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
            return doRead(new BufferedInputStream(new FileInputStream(file)));
        } catch (IOException e) {
            throw new StorageException("read file error", file.getName(), e);
        }
    }

    @Override
    protected void doSave(File file, Resume r) {
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new StorageException("write file error", file.getName(), e);
        }
            doUpdate(file, r);
    }

    @Override
    protected void doUpdate(File file, Resume r) {
        try {
            doWrite(r, new BufferedOutputStream(new FileOutputStream(file)));
        } catch (IOException e) {
            throw new StorageException("update file error", file.getName(), e);
        }
    }

    @Override
    protected void doDelete(File file) {
        if (!file.delete()) {
            throw new StorageException("delete file error", file.getName());
        }
    }

    @Override
    protected List<Resume> doCopyAll() {
        ArrayList<Resume> resumes = new ArrayList<>();
        for (File file : getListFiles(directory)) {
                resumes.add(doGet(file));
        }
        return resumes;
    }

    @Override
    public void clear() {
        for (File file : getListFiles(directory)) {
            doDelete(file);
        }
    }

    @Override
    public int size() {
        return getListFiles(directory).length;
    }

    private File[] getListFiles(File directory) {
        if (directory.listFiles() == null) {
            throw new StorageException("ListFiles is null", null);
        }
        return directory.listFiles();
    }

    protected abstract Resume doRead(InputStream io) throws IOException;
    protected abstract void doWrite(Resume resume, OutputStream os) throws IOException;
}
