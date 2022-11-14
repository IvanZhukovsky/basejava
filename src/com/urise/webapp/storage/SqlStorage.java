package com.urise.webapp.storage;

import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.ContactType;
import com.urise.webapp.model.Resume;
import com.urise.webapp.sql.SqlHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SqlStorage implements Storage {
    private final SqlHelper sqlHelper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        sqlHelper = new SqlHelper(dbUrl, dbUser, dbPassword);
    }

    @Override
    public void clear() {
        sqlHelper.commmand("DELETE FROM resume", ps -> ps.execute());
    }

    @Override
    public void update(Resume r) {
        this.get(r.getUuid());
        sqlHelper.transactionalExecute(conn -> {
            try (PreparedStatement ps = conn.prepareStatement("UPDATE resume SET full_name = ? WHERE uuid = ?")) {
                ps.setString(2, r.getUuid());
                ps.setString(1, r.getFullName());
                ps.executeUpdate();
            }
            return null;
        });

        sqlHelper.transactionalExecute(conn -> {
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM contact  WHERE resume_uuid = ?")) {
                ps.setString(1, r.getUuid());
                ps.executeUpdate();

            }
            insertContacts(conn, r);
            return null;
        });
    }

    @Override
    public void save(Resume r) {
        sqlHelper.transactionalExecute(conn -> {
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO resume (uuid, full_name) VALUES (?,?)")) {
                ps.setString(1, r.getUuid());
                ps.setString(2, r.getFullName());
                ps.execute();

            }
            insertContacts(conn, r);
            return null;
        });
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.commandForResult("SELECT * FROM resume r " +
                "LEFT JOIN contact c " +
                "on r.uuid = c.resume_uuid WHERE r.uuid=?", ps -> {
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new NotExistStorageException(uuid);
            }
            Resume r = new Resume(uuid, rs.getString("full_name"));
            if (rs.getString("type") != null) {
             do {
                String value = rs.getString("value");
                ContactType type = ContactType.valueOf(rs.getString("type"));
                r.addContact(type, value);
            } while (rs.next());}
            return r;
        });
    }

    @Override
    public void delete(String uuid) {
        sqlHelper.commmand("DELETE FROM resume  WHERE uuid = ?", ps -> {
            ps.setString(1, uuid);
            if (ps.executeUpdate() == 0) {
                throw new NotExistStorageException(uuid);
            }
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        List<Resume> resumesBase = new ArrayList<>();
        sqlHelper.commmand("SELECT * FROM resume ORDER BY (full_name, uuid)", ps -> {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                resumesBase.add(new Resume(rs.getString("uuid"), rs.getString("full_name")));
            }
        });

        for (Resume resume : resumesBase) {
            sqlHelper.commmand("SELECT * FROM contact WHERE resume_uuid = ?", ps -> {
                ps.setString(1, resume.getUuid());
                ResultSet rs = ps.executeQuery();
//                if (!rs.next()) {
//                    throw new NotExistStorageException(resume.getUuid());
//                }
                while (rs.next()) {
                    resume.addContact(ContactType.valueOf(rs.getString("type")), rs.getString("value"));
                }
            });
        }
        return resumesBase;
    }

    @Override
    public int size() {
        return sqlHelper.commandForResult("SELECT count(*) FROM resume", ps -> {
            ResultSet rs = ps.executeQuery();
            int count = rs.next() ? rs.getInt(1) : 0;
            return count;
        });
    }

    public void insertContacts(Connection conn, Resume r) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO contact (resume_uuid, type, value) VALUES (?,?,?)")){

            for (Map.Entry<ContactType, String> e : r.getContacts().entrySet()) {
                ps.setString(1, r.getUuid());
                ps.setString(2, e.getKey().name());
                ps.setString(3, e.getValue());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

}
