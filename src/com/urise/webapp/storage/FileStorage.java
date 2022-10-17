package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;
import com.urise.webapp.storage.IOStrategy.StreamSerializer;

import java.io.*;

public class FileStorage extends AbstractFileStorage {

    protected StreamSerializer streamSerializer;

    protected FileStorage(File directory, StreamSerializer streamSerializer) {
        super(directory);
        this.streamSerializer = streamSerializer;
    }

    @Override
    protected Resume doRead(InputStream io) throws IOException {
        return streamSerializer.doRead(io);
    }

    @Override
    protected void doWrite(Resume resume, OutputStream os) throws IOException {
        streamSerializer.doWrite(resume, os);
    }
}


