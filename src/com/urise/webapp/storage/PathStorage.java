package com.urise.webapp.storage;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;
import com.urise.webapp.storage.serializer.StreamSerializer;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PathStorage extends AbstractStorage<Path> {

    private final Path directory;
    private StreamSerializer streamSerializer;

    protected PathStorage(String dir, StreamSerializer streamSerializer) {
        directory = Paths.get(dir);
        Objects.requireNonNull(directory, "directory must not be null");
        if (!Files.isDirectory(directory) || !Files.isWritable(directory)) {
            throw new IllegalArgumentException(dir + "is not directory");
        }
        this.streamSerializer = streamSerializer;
    }

    @Override
    protected boolean isExist(Path path) {
        return Files.isRegularFile(path);
    }

    @Override
    protected Path getSearchKey(String uuid) {
        return directory.resolve(uuid);
    }

    @Override
    protected Resume doGet(Path path) {
        try {
            return streamSerializer.doRead(new BufferedInputStream(Files.newInputStream(path)));
        } catch (IOException e) {
            throw new StorageException("read file error", getFileName(path), e);
        }
    }

    @Override
    protected void doSave(Path path, Resume r) {
        try {
            Files.createFile(path);
        } catch (IOException e) {
            throw new StorageException("Coulnd't create path" + path, getFileName(path), e);
        }
            doUpdate(path, r);
    }

    @Override
    protected void doUpdate(Path path, Resume r) {
        try {
            streamSerializer.doWrite(r, new BufferedOutputStream(Files.newOutputStream(path)));
        } catch (IOException e) {
            throw new StorageException("update file error", r.getUuid(), e);
        }
    }

    @Override
    protected void doDelete(Path path) {
        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new StorageException("delete file error", getFileName(path), e);
        }
    }

    @Override
    protected List<Resume> doCopyAll() {
//        List<Resume> resumes = new ArrayList<>();
//        for (Path file : getFilesList(directory).toList()) {
//                resumes.add(doGet(file));
//        }
//        return resumes;
        return getFilesList(directory).map(this::doGet).collect(Collectors.toList());
    }
    //Исправлено
    @Override
    public void clear() {
            getFilesList(directory).forEach(this::doDelete);
    }

    @Override
    public int size() {
        return getFilesList(directory).toList().size();
    }

    private Stream<Path> getFilesList(Path directory){
        try {
            Stream<Path> paths =  Files.list(directory);
            if (paths == null) {
                throw new StorageException("listFiles is null", directory.getFileName().toString());
            }
            return Files.list(directory);
        } catch (IOException e) {
            throw new StorageException("listFiles error", e);
        }
    }

    private String getFileName(Path path) {
        return path.getFileName().toString();
    }
}
