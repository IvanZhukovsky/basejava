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
import java.util.stream.Stream;

public abstract class AbstractPathStorage extends AbstractStorage<Path> {

    private final Path directory;

    protected AbstractPathStorage(String dir) {
        directory = Paths.get(dir);
        Objects.requireNonNull(directory, "directory must not be null");
        if (!Files.isDirectory(directory) || !Files.isWritable(directory)) {
            throw new IllegalArgumentException(dir + "is not directory");
        }
    }

    @Override
    protected boolean isExist(Path path) {
        return Files.exists(path);
    }

    @Override
    protected Path getSearchKey(String uuid) {
        return directory.resolve(uuid);
    }

    @Override
    protected Resume doGet(Path file) {
        try {
            return doRead(new BufferedInputStream(new FileInputStream(file.toFile())));
        } catch (IOException e) {
            throw new StorageException("read file error", file.getFileName().toString(), e);
        }
    }

    @Override
    protected void doSave(Path file, Resume r) {
        try {
            Files.createFile(file);
        } catch (IOException e) {
            throw new StorageException("write file error", file.getFileName().toString(), e);
        }
            doUpdate(file, r);
    }

    @Override
    protected void doUpdate(Path file, Resume r) {
        try {
            doWrite(r, new BufferedOutputStream(new FileOutputStream(file.toFile())));
        } catch (IOException e) {
            throw new StorageException("update file error", file.getFileName().toString(), e);
        }
    }

    @Override
    protected void doDelete(Path file) {
        try {
            Files.delete(file);
        } catch (IOException e) {
            throw new StorageException("delete file error", directory.getFileName().toString());
        }
    }

    @Override
    protected List<Resume> doCopyAll() {
        List<Resume> resumes = new ArrayList<>();
        for (Path file : getListFiles(directory).toList()) {
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

    @Override
    public int size() {
        return getListFiles(directory).toList().size();
    }

    private Stream<Path> getListFiles(Path directory){
        try {
            Stream<Path> paths =  Files.list(directory);
            if (paths == null) {
                throw new StorageException("listFiles is null", directory.getFileName().toString());
            }
            return Files.list(directory);
        } catch (IOException e) {
            throw new StorageException("listFiles error", directory.getFileName().toString());
        }
    }
}
