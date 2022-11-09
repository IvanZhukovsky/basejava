package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.Resume;
import com.urise.webapp.sql.SqlHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
        sqlHelper.commmand("UPDATE resume SET full_name = ? WHERE uuid = ?", ps -> {
            ps.setString(2, r.getUuid());
            ps.setString(1, r.getFullName());
            ps.executeUpdate();
        });
    }

    @Override
    public void save(Resume r) {
        sqlHelper.commmand("SELECT * FROM resume r WHERE r.uuid=?", ps -> {
            ps.setString(1, r.getUuid());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                throw new ExistStorageException(r.getUuid());
            }
        });

        sqlHelper.commmand("INSERT INTO resume (uuid, full_name) VALUES (?,?)", ps -> {
            ps.setString(1, r.getUuid());
            ps.setString(2, r.getFullName());
            ps.execute();
        });

    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.commandForResult("SELECT * FROM resume r WHERE r.uuid=?", ps -> {
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new NotExistStorageException(uuid);
            }
            return new Resume(uuid, rs.getString("full_name"));
        });
    }

    @Override
    public void delete(String uuid) {
        this.get(uuid);
        sqlHelper.commmand("DELETE FROM resume  WHERE uuid = ?", ps -> {
            ps.setString(1, uuid);
            ps.execute();
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        return sqlHelper.commandForResult("SELECT * FROM resume ORDER BY (uuid, full_name)", ps -> {
            List<Resume> resumes = new ArrayList<>();
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                resumes.add(new Resume(rs.getString("uuid").trim(), rs.getString("full_name")));
            }
            return resumes;
        });
    }

    @Override
    public int size() {
        return sqlHelper.commandForResult("SELECT count(*) FROM resume", ps -> {
            ResultSet rs = ps.executeQuery();
            int count = 0;
            count = rs.next() ? rs.getInt(1) : count;
            return count;
        });
    }
}
