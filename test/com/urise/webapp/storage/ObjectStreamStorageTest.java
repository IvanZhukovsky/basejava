package com.urise.webapp.storage;

import com.urise.webapp.storage.IOStrategy.SerializeStrategy;

public class ObjectStreamStorageTest extends AbstractStorageTest{
    public ObjectStreamStorageTest() {
        super(new ObjectStreamStorage(STORAGE_DIR, new SerializeStrategy()));
    }
}
