package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class SerializeWriteMethod implements DoWriteMethod{
    @Override
    public void write(Resume resume, OutputStream os) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(os)) {
            oos.writeObject(resume);
        }
    }
}
