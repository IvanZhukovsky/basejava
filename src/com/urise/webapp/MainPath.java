package com.urise.webapp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class MainPath {
    public static void main(String[] args) {
        Path directory = Paths.get("/Users/zhukovsky/IdeaProjects/basejava/storage");
        System.out.println(Paths.get(directory.toString(), "ttt.doc"));
//        try {
//            Files.createFile(Paths.get(directory.toString(), "ttt.doc"));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

        List<Path> files;

        try {
             files = Files.list(directory).toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (Path path : files) {
            System.out.println(path);
        }
    }
}
