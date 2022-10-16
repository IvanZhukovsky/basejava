package com.urise.webapp.storage;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;

import java.io.*;
import java.nio.file.Path;

public class ObjectStreamPathStorage extends AbstractPathStorage {

    protected ObjectStreamPathStorage(String directory) {
        super(directory);
        writer = new SerializeWriteMethod();
        reader = new SerializeReadMethod();
    }

    @Override
    protected void doWrite(Resume resume, OutputStream os) throws IOException {
        writer.write(resume, os);
    }

    @Override
    protected Resume doRead(InputStream is) throws IOException {
        return reader.read(is);
    }

}


