package com.mars.foo;

import java.io.File;

public class Foo {
    private static final String CACHE_DIR = "cache";
    public static File getCacheDir(String dir){
        String path = System.getProperty("user.dir")+(File.separator + CACHE_DIR + File.separator + dir);
        File file = new File(path);
        if(!file.exists()){
            if(!file.mkdirs()){
                throw new RuntimeException("Create path error:" + path);
            }
        }
        return file;
    }
}
