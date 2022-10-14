package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.Resume;
import com.urise.webapp.model.ResumeTestData;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public abstract class AbstractStorageTest {
    protected static final Resume RESUME_1 = ResumeTestData.createResume("uuid1", "1");
    protected static final Resume RESUME_2 = ResumeTestData.createResume("uuid2", "1");
    protected static final Resume RESUME_3 = ResumeTestData.createResume("uuid3", "1");
    protected static final Resume RESUME_4 = ResumeTestData.createResume("uuid4", "2");
    protected static final String UUID_NOT_EXIST = "uuid5";
    protected static final int STORAGE_LIMIT = 10000;
    protected final Storage storage;

    public AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Before
    public void setUp() {
        storage.clear();
        storage.save(RESUME_1);
        storage.save(RESUME_2);
        storage.save(RESUME_3);
    }

    @Test
    public void clear() {
        //Проверяем произошло ли обнуление счетчика заполненых элементов
        storage.clear();
        assertSize(0);
        Assert.assertFalse(!storage.getAllSorted().isEmpty());
    }

    @Test
    public void update() {
        storage.update(RESUME_2);
        Assert.assertSame(RESUME_2, storage.get(RESUME_2.getUuid()));
        assertGet(RESUME_2);
    }

    @Test(expected = NotExistStorageException.class)
    public void updateNotExist() {
        storage.update(new Resume());
    }

    @Test
    public void save() {
        storage.save(RESUME_4);
        assertGet(RESUME_4);
        assertSize(4);
    }

    @Test(expected = ExistStorageException.class)
    public void saveExist() {
        storage.save(RESUME_1);
    }

    @Test(expected = NotExistStorageException.class)
    public void delete() {
        //Проверяем произошло ли уменьшение счетчика при удалении резюме
        storage.delete(RESUME_1.getUuid());
        assertSize(2);
        assertGet(RESUME_1);
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNotExist() {
        storage.delete(UUID_NOT_EXIST);
    }

    @Test
    public void getAllSorted() {
        storage.clear();
        storage.save(RESUME_4);
        storage.save(RESUME_1);
        storage.save(RESUME_2);
        storage.save(RESUME_3);

        List<Resume> list = storage.getAllSorted();
        Assert.assertSame(RESUME_1, list.get(0));
        Assert.assertSame(RESUME_2, list.get(1));
        Assert.assertSame(RESUME_3, list.get(2));
        Assert.assertSame(RESUME_4, list.get(3));

    }

    @Test
    public void size() {
        assertSize(3);
    }

    @Test
    public void get() {
        assertGet(RESUME_1);
        assertGet(RESUME_2);
        assertGet(RESUME_3);
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExist() {
        storage.get(UUID_NOT_EXIST);
    }

    @Test
    public void fillAfterDelete() {
        storage.delete("uuid1");
        Assert.assertEquals("uuid2", storage.getAllSorted().get(0).getUuid());
    }

    private void assertSize(int expectedSize) {
        Assert.assertEquals(expectedSize, storage.size());
    }

    private void assertGet(Resume resume) {
        Assert.assertEquals(resume, storage.get(resume.getUuid()));
    }
}