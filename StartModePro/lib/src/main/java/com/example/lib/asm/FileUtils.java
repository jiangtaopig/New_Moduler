package com.example.lib.asm;

import java.io.File;

public class FileUtils {
    public static String getClassFilePath(Class clazz) {
        String buildDir = clazz.getProtectionDomain().getCodeSource().getLocation().getFile();
        String fileName = clazz.getSimpleName() + ".class";
        File file = new File(buildDir + clazz.getPackage().getName().replaceAll("[.]", "/") + "/", fileName);
        return file.getAbsolutePath();
    }
}
