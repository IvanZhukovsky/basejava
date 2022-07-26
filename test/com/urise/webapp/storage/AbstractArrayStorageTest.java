package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public abstract class AbstractArrayStorageTest {
    private static final String UUID_1 = "uuid1";
    private static final String UUID_2 = "uuid2";
    private static final String UUID_3 = "uuid3";
    protected Storage storage;

    public AbstractArrayStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Before
    public void setUp() throws Exception {
        storage.clear();
        storage.save(new Resume(UUID_1));
        storage.save(new Resume(UUID_2));
        storage.save(new Resume(UUID_3));
    }

    @Test
    public void clear() {
        //Проверяем произошло ли обнуление счетчика заполненых элементов
        int size = 0;
        storage.clear();
        Assert.assertEquals(size, storage.size());
    }

    @Test
    public void update() {
        //Проверка поиска резюме не будет пройдена, если резюме присуствующее в хранилище не будет найдено
        Resume resume = new Resume(UUID_1);
        try {
            storage.update(resume);
        } catch (NotExistStorageException e) {
            Assert.fail("Ошибка поиска резюме!");
        }
    }

    @Test(expected = NotExistStorageException.class)
    public void NotExistStorageExceptionTest() {
        storage.update(new Resume());
    }

    @Test
    public void save() {
        //Проверяем произошло ли увеличение счетчика при сохранении нового резюме
        int size = 4;
        Resume resume = new Resume("uuid4");
        storage.save(resume);
        Assert.assertEquals(size, storage.size());
        //Проверяем было ли добавлено резюме
        Assert.assertEquals(resume, storage.getAll()[size - 1]);
    }

    @Test
    public void StorageOverflowExceptionTest() {
        try {
            for (int i = 3; i < 10000; i++) {
                storage.save(new Resume());
            }
        } catch (StorageException e) {
            Assert.fail("Переполнение произошло раньше времени");
        }
        try {
            Resume resume1 = new Resume();
            storage.save(resume1);
            Assert.fail("Исключение при переполнении не было выброшено!");
        } catch (StorageException e) {
        }
    }

    @Test(expected = ExistStorageException.class)
    public void ExistStorageExceptionTest() {
        storage.save(new Resume(UUID_1));
    }

    @Test
    public void delete() {
        //Проверяем произошло ли уменьшение счетчика при удалении резюме
        int size = storage.size();
        storage.delete(UUID_1);
        Assert.assertEquals(size - 1, storage.size());
    }

    @Test(expected = NotExistStorageException.class)
    public void NotExistStorageExceptionTest1() {
        storage.delete(new Resume().getUuid());
    }


    @Test
    public void getAll() {
        Resume[] resumes = new Resume[3];
        for (int i = 0; i < resumes.length; i++) {
            resumes[i] = storage.getAll()[i];
        }
        Assert.assertEquals(resumes, storage.getAll());
    }

    @Test
    public void size() throws Exception {
        int size = 3;
        Assert.assertEquals(size, storage.size());
    }

    @Test
    public void get() {
        Resume resume = new Resume("uuid1");
        Assert.assertEquals(resume, storage.get(UUID_1));
    }

    @Test(expected = NotExistStorageException.class)
    public void NotExistStorageExceptionTest2() {
        storage.get(new Resume().getUuid());
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExist() throws Exception {
        storage.get("dummy");
    }
}