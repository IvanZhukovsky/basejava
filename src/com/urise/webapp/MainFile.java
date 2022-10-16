package com.urise.webapp;

import java.io.File;

public class MainFile {
    public static void main(String[] args) {
        File sourcePath = new File("/Users/zhukovsky/IdeaProjects/basejava/src/com/urise/webapp");
        printFiles(sourcePath, " ");
    }

    private static void printFiles(File directory, String step) {
        for (File file : directory.listFiles()) {
            if (file.isFile()) {
                System.out.println(step + "  file: " + file.getName());
            }
            if (file.isDirectory()) {
                System.out.println(step + "dir: " + file.getName());
                printFiles(file, step + " ");
            }
        }
    }
}
