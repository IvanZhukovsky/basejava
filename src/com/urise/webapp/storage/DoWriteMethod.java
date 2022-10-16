package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.io.IOException;
import java.io.OutputStream;

public interface DoWriteMethod {
    public void write(Resume resume, OutputStream os) throws IOException;
}
