package com.urise.webapp.storage;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AbstractPathStorage extends AbstractStorage<Path> {

    private final Path directory;

    protected AbstractPathStorage(String dir) {
        directory = Paths.get(dir);
        Objects.requireNonNull(directory, "directory must not be null");
        if (!Files.isDirectory(directory) || !Files.isWritable(directory)) {
            throw new IllegalArgumentException(dir + "is not directory");
        }
    }
    //Исправлено
    @Override
    protected boolean isExist(Path path) {
        return Files.exists(path);
    }
    //Исправлено
    @Override
    protected Path getSearchKey(String uuid) {
        return Paths.get(directory.toString(), uuid);
    }
    //Исправлено
    @Override
    protected Resume doGet(Path file) {
        try {
            return doRead(new BufferedInputStream(new FileInputStream(file.toFile())));
        } catch (IOException e) {
            throw new StorageException("IO error", file.getFileName().toString(), e);
        }
    }

    @Override
    protected void doSave(Path file, Resume r) {
        try {
            Files.createFile(file);
        } catch (IOException e) {
            throw new StorageException("IO error", file.getFileName().toString(), e);
        }
            doUpdate(file, r);
    }
    //Исправлено
    @Override
    protected void doUpdate(Path file, Resume r) {
        try {
            doWrite(r, new BufferedOutputStream(new FileOutputStream(file.toFile())));
        } catch (IOException e) {
            throw new StorageException("IO error", file.getFileName().toString(), e);
        }
    }
    //Исправлено
    @Override
    protected void doDelete(Path file) {
        try {
            Files.delete(file);
        } catch (IOException e) {
            throw new StorageException("file delete error", directory.getFileName().toString());
        }
    }

    @Override
    protected List<Resume> doCopyAll() {
        List<Resume> resumes = new ArrayList<>();
        List<Path> files;
        try {
            files = Files.list(directory).toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (files == null) {
            throw new StorageException("listFiles is null", directory.getFileName().toString());
        }
        for (Path file : files) {
                resumes.add(doGet(file));
        }
        return resumes;
    }
    //Исправлено
    @Override
    public void clear() {
        try {
            Files.list(directory).forEach(this::doDelete);
        } catch (IOException e) {
            throw new StorageException("Path delete error", null);
        }

    }
    //Исправлено
    @Override
    public int size() {
        int size;
        try {
            size =  Files.list(directory).toList().size();
        } catch (IOException e) {
            throw new StorageException("listFiles error", directory.getFileName().toString());
        }
        return size;
    }

    protected abstract Resume doRead(InputStream file) throws IOException;

    protected abstract void doWrite(Resume resume, OutputStream file) throws IOException;
}
