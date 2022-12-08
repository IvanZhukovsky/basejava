package com.urise.webapp.storage;

import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.*;
import com.urise.webapp.sql.SqlHelper;

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
        Resume resume =  sqlHelper.commandForResult("SELECT * FROM resume r " +
                "LEFT JOIN contact c " +
                "on r.uuid = c.resume_uuid WHERE r.uuid=?", ps -> {
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new NotExistStorageException(uuid);
            }
            Resume r = new Resume(uuid, rs.getString("full_name"));
            do {
                addContact(rs, r);
            } while (rs.next());
            return r;
        });

        return sqlHelper.commandForResult("SELECT * FROM resume r " +
                "LEFT JOIN sections s " +
                "on r.uuid = s.resume_uuid WHERE r.uuid=?", ps -> {
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new NotExistStorageException(uuid);
            }
                do {
                    addSection(rs, resume);
                } while (rs.next());
            return resume;
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
                while (rs.next()) {
                    resume.addContact(ContactType.valueOf(rs.getString("type")), rs.getString("value"));
                }
            });
        }

        for (Resume resume : resumesBase) {
            sqlHelper.commmand("SELECT * FROM sections WHERE resume_uuid = ?", ps -> {
                ps.setString(1, resume.getUuid());
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    addSectionByType(SectionType.valueOf(rs.getString("type")), resume, rs.getString("content"));
                }
            });
        }

        return resumesBase;
    }

    public void addContact(ResultSet rs, Resume resume) throws SQLException {
        if (rs.getString("type") != null) {
            resume.addContact(ContactType.valueOf(rs.getString("type")), rs.getString("value"));
        }
    }

    public void addSection(ResultSet rs, Resume resume) throws SQLException {
        if (rs.getString("type") != null) {
            addSectionByType(SectionType.valueOf(rs.getString("type")), resume, rs.getString("content"));
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
                String content = "";
                switch (e.getKey()) {
                    case OBJECTIVE:
                    case PERSONAL:
                        TextSection textSection = (TextSection) e.getValue();
                        content = textSection.getContent();
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        ListSection listSection = (ListSection) e.getValue();
                        for (String note : listSection.getContent()) {
                            content = content + note + "\n";
                        }
                        break;
                }
                ps.setString(1, r.getUuid());
                ps.setString(2, e.getKey().name());
                ps.setString(3, content);
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void addSectionByType(SectionType sectionType, Resume resume, String sectionContent) {
        switch (sectionType) {
            case OBJECTIVE:
            case PERSONAL:
                resume.addSection(sectionType, new TextSection(sectionContent));
                break;
            case ACHIEVEMENT:
            case QUALIFICATIONS:
                List<String> content = new ArrayList<>();
                Scanner scanner = new Scanner(sectionContent);
                while (scanner.hasNextLine()) {
                    content.add(scanner.nextLine());
                }
                resume.addSection(sectionType, new ListSection(content));
                break;
        }
    }
}
