package org.example.util;

import java.io.*;
import java.lang.reflect.*;

public class DynamicClassLoader extends ClassLoader {
    public Class<?> loadClassFromFile(String path, String className) throws IOException {
        File file = new File(path);
        byte[] bytes = loadBytesFromFile(file);
        return defineClass(className, bytes, 0, bytes.length);
    }

    private byte[] loadBytesFromFile(File file) throws IOException {
        try (InputStream inputStream = new FileInputStream(file)) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }
            return byteArrayOutputStream.toByteArray();
        }
    }
}
