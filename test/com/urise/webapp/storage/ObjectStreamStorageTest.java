package com.urise.webapp.storage;

import com.urise.webapp.storage.IOStrategy.ObjectStreamSerializer;

public class ObjectStreamStorageTest extends AbstractStorageTest{
    public ObjectStreamStorageTest() {
        super(new FileStorage(STORAGE_DIR, new ObjectStreamSerializer()));
    }
}
