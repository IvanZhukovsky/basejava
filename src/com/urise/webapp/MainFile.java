package com.urise.webapp;

import java.io.File;
import java.util.ArrayList;

public class MainFile {
    public static void main(String[] args) {

        File sourcePath = new File("/Users/zhukovsky/IdeaProjects/basejava/src/com/urise/webapp");

        long start = System.currentTimeMillis();
        printFiles(sourcePath);
        System.out.println("Рекурсивный метод справился с задачей за - " + (System.currentTimeMillis() - start) + "мс");

        start = System.currentTimeMillis();
        ArrayList<File> generalList = new ArrayList<>();
        generalList.add(sourcePath);
        for (int i  = 0; i < generalList.size(); i++) {
            File[] list = generalList.get(i).listFiles();
            for (File file : list) {
                if (file.isFile()) {
                    System.out.println(file.getName());
                }
                if (file.isDirectory()) {
                    generalList.add(file);
                }
            }
        }
        System.out.println("Не рекурсивный метод справился с задачей за - " + (System.currentTimeMillis() - start) + "мс");

    }

    private static void printFiles(File directory) {
        for (File file : directory.listFiles()) {
            if (file.isFile()) {
                System.out.println(file.getName());
            }
            if (file.isDirectory()) {
                printFiles(file);
            }
        }
    }

}
