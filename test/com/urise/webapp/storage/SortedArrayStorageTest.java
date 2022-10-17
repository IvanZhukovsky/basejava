package com.urise.webapp.storage;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;
import org.junit.Assert;
import org.junit.Test;

public class SortedArrayStorageTest extends AbstractArrayStorageTest {
    public SortedArrayStorageTest() {
        super(new SortedArrayStorage());
    }

    @Test
    public void getIndexForSave() {
        storage.save(new Resume("uuid5", "Name5"));
        Resume resume = new Resume("uuid4", "Name4");
        storage.save(resume);
        Assert.assertEquals(resume, storage.getAllSorted().get(3));
    }

}