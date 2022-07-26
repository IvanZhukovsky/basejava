package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;
import org.junit.Assert;
import org.junit.Test;

public class SortedArrayStorageTest extends AbstractArrayStorageTest {
    public SortedArrayStorageTest() {
        super(new SortedArrayStorage());
    }

    @Test
    public void getIndexForSave() {
        storage.save(new Resume("uuid5"));
        Resume resume = new Resume("uuid4");
        storage.save(resume);
        Assert.assertEquals(resume, storage.getAll()[3]);
    }

    @Test
    public void fillAfterDelete() {
        storage.delete("uuid1");
        Assert.assertEquals("uuid2", storage.getAll()[0].getUuid());
    }
}