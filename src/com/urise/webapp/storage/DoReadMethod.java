package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.io.IOException;
import java.io.InputStream;

public interface DoReadMethod {
    public Resume read(InputStream is) throws IOException;
}
