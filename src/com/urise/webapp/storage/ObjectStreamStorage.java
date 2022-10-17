package com.urise.webapp.storage;

import com.urise.webapp.storage.IOStrategy.IOStrategy;

import java.io.*;

public class ObjectStreamStorage extends AbstractFileStorage {

    protected ObjectStreamStorage(File directory, IOStrategy ioStrategy) {
        super(directory);
        this.ioStrategy = ioStrategy;
    }
}


