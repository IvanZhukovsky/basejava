package com.urise.webapp;

import com.urise.webapp.storage.SqlStorage;
import com.urise.webapp.storage.Storage;

import java.io.*;
import java.util.Properties;

public class Config {

    protected static final File PROPS = new File("/Users/zhukovsky/Desktop/basejava/config/resumes.properties");
    //protected static final File PROPS = new File("config/resumes.properties");
    private static final Config INSTANCE = new Config();
    private Properties props = new Properties();
    private File storageDir;
    private Storage sqlStorage;

    public static Config get() {return INSTANCE;}

    private Config() {
        try (InputStream inputStream = new FileInputStream(PROPS)) {
            props.load(inputStream);
            storageDir = new File(props.getProperty("storage.dir"));
        } catch (IOException e) {
            new IllegalStateException("Invalid Cofig File" + PROPS.getAbsolutePath() );
        }
        sqlStorage = new SqlStorage(props.getProperty("db.url"), props.getProperty("db.user"), props.getProperty("db.password"));
    }

    public File getStorageDir() {
        return storageDir;
    }

    public Storage getSqlStorage() {
        return sqlStorage;
    }
}
