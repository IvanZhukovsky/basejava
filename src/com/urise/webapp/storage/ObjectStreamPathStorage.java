package com.urise.webapp.storage;

import com.urise.webapp.storage.IOStrategy.IOStrategy;

public class ObjectStreamPathStorage extends AbstractPathStorage {

    protected ObjectStreamPathStorage(String directory, IOStrategy ioStrategy) {
        super(directory);
        this.ioStrategy = ioStrategy;
    }
}


