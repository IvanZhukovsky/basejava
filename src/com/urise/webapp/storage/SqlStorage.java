package com.urise.webapp.storage;

import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.*;
import com.urise.webapp.sql.SqlHelper;
import com.urise.webapp.util.JsonParser;

import java.sql.*;
import java.util.*;

public class SqlStorage implements Storage {
    private final SqlHelper sqlHelper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword)  {

        try {
            Class.forName ("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


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
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM sections  WHERE resume_uuid = ?")) {
                ps.setString(1, r.getUuid());
                ps.executeUpdate();
            }
            insertContacts(conn, r);
            insertSections(conn, r);
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
            insertSections(conn, r);
            return null;
        });
    }

    @Override
    public Resume get(String uuid) {

        return sqlHelper.transactionalExecute(conn -> {
            Resume r;
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM resume r " +
                    "LEFT JOIN contact c " +
                    "on r.uuid = c.resume_uuid WHERE r.uuid=?")) {
                ps.setString(1, uuid);
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    throw new NotExistStorageException(uuid);
                }
                r = new Resume(uuid, rs.getString("full_name"));
                do {
                    addContact(rs, r);
                } while (rs.next());
            }
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM resume r " +
                    "LEFT JOIN sections s " +
                    "on r.uuid = s.resume_uuid WHERE r.uuid=?")) {
                ps.setString(1, uuid);
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    throw new NotExistStorageException(uuid);
                }
                do {
                    addSection(rs, r);
                } while (rs.next());
                return r;
            }
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

//    @Override
//    public List<Resume> getAllSorted() {
//        return sqlHelper.transactionalExecute(conn -> {
//            List<Resume> resumesBase = new ArrayList<>();
//            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM resume ORDER BY (full_name, uuid)")) {
//                ResultSet rs = ps.executeQuery();
//                while (rs.next()) {
//                    resumesBase.add(new Resume(rs.getString("uuid"), rs.getString("full_name")));
//                }
//            }
//            for (Resume resume : resumesBase) {
//            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM contact WHERE resume_uuid = ?")) {
//                ps.setString(1, resume.getUuid());
//                ResultSet rs = ps.executeQuery();
//                while (rs.next()) {
//                    resume.addContact(ContactType.valueOf(rs.getString("type")), rs.getString("value"));
//                }
//            }
//            }
//
//            for (Resume resume : resumesBase) {
//                try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM sections WHERE resume_uuid = ?")) {
//                    ps.setString(1, resume.getUuid());
//                    ResultSet rs = ps.executeQuery();
//                    while (rs.next()) {
//                        addSection(rs, resume);
//                    }
//                }
//            }
//            return resumesBase;
//        });
//    }

    @Override
    public List<Resume> getAllSorted() {
        return sqlHelper.transactionalExecute(conn -> {
            Map<String, Resume> resumes = new LinkedHashMap<>();

            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM resume ORDER BY full_name, uuid")) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String uuid = rs.getString("uuid");
                    resumes.put(uuid, new Resume(uuid, rs.getString("full_name")));
                }
            }

            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM contact")) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Resume r = resumes.get(rs.getString("resume_uuid"));
                    addContact(rs, r);
                }
            }

            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM sections")) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Resume r = resumes.get(rs.getString("resume_uuid"));
                    addSection(rs, r);
                }
            }

            return new ArrayList<>(resumes.values());
        });
    }

    public void addContact(ResultSet rs, Resume resume) throws SQLException {
        if (rs.getString("type") != null) {
            resume.addContact(ContactType.valueOf(rs.getString("type")), rs.getString("value"));
        }
    }

    public void addSection(ResultSet rs, Resume resume) throws SQLException {

        String content = rs.getString ("content");
        if (rs.getString("type") != null) {
            SectionType type = SectionType.valueOf(rs.getString ("type"));
            resume.addSection(type, JsonParser.read(content, AbstractSection.class));
        }
    }

    @Override
    public int size() {
        return sqlHelper.commandForResult("SELECT count(*) FROM resume", ps -> {
            ResultSet rs = ps.executeQuery();
            int count = rs.next() ? rs.getInt(1) : 0;
            return count;
        });
    }

    private void insertContacts(Connection conn, Resume r) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO contact (resume_uuid, type, value) VALUES (?,?,?)")) {

            for (Map.Entry<ContactType, String> e : r.getContacts().entrySet()) {
                ps.setString(1, r.getUuid());
                ps.setString(2, e.getKey().name());
                ps.setString(3, e.getValue());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void insertSections(Connection conn, Resume r) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO sections (resume_uuid, type, content) VALUES (?,?,?)")) {
            for (Map.Entry<SectionType, AbstractSection> e : r.getSections().entrySet()) {
                ps.setString(1, r.getUuid());
                ps.setString(2, e.getKey().name());
                AbstractSection section = e.getValue();
                ps.setString(3, JsonParser.write(section, AbstractSection.class));
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }
}
