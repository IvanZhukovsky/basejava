package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;
import com.urise.webapp.storage.IOStrategy.StreamSerializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class PathStorage extends AbstractPathStorage {

    protected StreamSerializer streamSerializer;

    protected PathStorage(String directory, StreamSerializer streamSerializer) {
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


