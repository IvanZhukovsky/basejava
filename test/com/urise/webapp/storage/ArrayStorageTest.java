package com.urise.webapp.storage;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;
import org.junit.Assert;
import org.junit.Test;

public class ArrayStorageTest extends AbstractArrayStorageTest {
    public ArrayStorageTest() {
        super(new ArrayStorage());
    }

    @Test
    public void getIndexForSave() {
        storage.save(new Resume("uuid5","2"));
        Resume resume = new Resume("uuid4", "2");
        storage.save(resume);
        Assert.assertEquals(resume, storage.getAllSorted().get(3));
    }

}