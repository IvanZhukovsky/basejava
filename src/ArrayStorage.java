import com.sun.source.util.SourcePositions;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    Resume[] storage = new Resume[10000];
    int size = 0;

    void clear() {
        Arrays.fill(storage, 0, size - 1, null);
        size = 0;
    }

    void save(Resume r) {
        if (size < 10000) {
            if (checkPresent(r.uuid) == -1) {
                storage[size] = r;
                size++;
            } else {
                System.out.printf("Резюме %s уже присутствует в базе данных%n", r.uuid);
            }
        } else {
            System.out.println("База резюме переполнена, запись невозможна");
        }
    }

    Resume get(String uuid) {
        int index = checkPresentAndPrint(uuid);
        if (index != -1) {
            return storage[index];
        }
        return null;
    }

    void delete(String uuid) {
        int index = checkPresentAndPrint(uuid);
        if (index != -1) {
            System.arraycopy(storage, index + 1, storage, index, size - index - 1);
            storage[size - 1] = null;
            size--;
        }
    }

    void update(Resume resume) {
        int index = checkPresentAndPrint(resume.uuid);
        if (index != -1) {
            storage[index] = resume;
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    int size() {
        return size;
    }

    int checkPresent(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].uuid.equals(uuid)) {
                return i;
            }
        }
        return -1;
    }
    int checkPresentAndPrint(String uuid) {
        int result = checkPresent(uuid);
        if (result == -1) {
            System.out.printf("Резюме %s отсуствует в базе данных%n", uuid);
            return result;
        }
        else {
            return result;
        }
    }
}
