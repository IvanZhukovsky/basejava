package com.urise.webapp;

import java.io.*;
import java.util.Properties;

public class Config {

    protected static final File PROPS = new File("/Users/zhukovsky/Desktop/basejava/config/resumes.properties");
    private static final Config INSTANCE = new Config();
    private Properties props = new Properties();
    private File storageDir;
    private String dbUrl;
    private String dbUser;
    private String dbPassword;


    public static Config get() {return INSTANCE;}

    private Config() {
        try (InputStream inputStream = new FileInputStream(PROPS)) {
            props.load(inputStream);
            storageDir = new File(props.getProperty("storage.dir"));
            dbUrl = props.getProperty("db.url");
            dbUser = props.getProperty("db.user");
            dbPassword = props.getProperty("db.password");
        } catch (IOException e) {
            new IllegalStateException("Invalid Cofig File" + PROPS.getAbsolutePath() );
        }
    }

    public File getStorageDir() {
        return storageDir;
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public String getDbUser() {
        return dbUser;
    }

    public String getDbPassword() {
        return dbPassword;
    }
}
