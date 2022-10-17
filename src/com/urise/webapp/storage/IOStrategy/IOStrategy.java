package com.urise.webapp.storage.IOStrategy;

import com.urise.webapp.model.Resume;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface IOStrategy {
    public Resume doRead(InputStream is) throws IOException;
    public void doWrite(Resume resume, OutputStream os) throws IOException;
}
