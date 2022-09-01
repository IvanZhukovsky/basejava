package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ListStorageTest  extends AbstractArrayStorageTest {
    public ListStorageTest() {
        super(new ListStorage());
    }


}