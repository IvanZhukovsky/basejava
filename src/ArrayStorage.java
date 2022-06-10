import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    Resume[] storage = new Resume[10000];
    int cursor = 0;

    void clear() {
        for (int i = 0; i < cursor; i++) {
            storage[i] = null;
        }
        cursor = 0;
    }

    void save(Resume r) {
        storage[cursor] = r;
        cursor++;
    }

    Resume get(String uuid) {
        for (int i = 0; i < cursor; i++) {
            if (storage[i].uuid.equals(uuid)) {
                return storage[i];
            }
        }
        return null;
    }

    void delete(String uuid) {
        for (int i = 0; i < cursor; i++) {
            if (storage[i].uuid.equals(uuid)) {
                storage[i] = null;
                System.arraycopy(storage, i + 1, storage, i, cursor - i - 1);
                storage[cursor - 1] = null;
                cursor--;
            }
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll() {
        return Arrays.copyOf(storage, cursor);
    }

    int size() {
        return cursor;
    }
}
