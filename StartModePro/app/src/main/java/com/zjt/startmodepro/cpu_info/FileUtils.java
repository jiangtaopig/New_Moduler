package com.zjt.startmodepro.cpu_info;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

public class FileUtils {

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    public static String readFileToString(File file) throws IOException {
        return readFileToString(file, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    public static String readFileToString(File file, String encoding) throws IOException {
        Charset charset = null;
        try {
            charset = encoding == null ? Charset.defaultCharset() : Charset.forName(encoding);
        } catch (Exception e) {
            throw new UnsupportedEncodingException("Unsupported encoding " + encoding);
        }

        byte[] buff = readFileToByteArray(file);
        return new String(buff, charset);
    }

    public static byte[] readFileToByteArray(File file) throws IOException {
        InputStream in = null;
        try {
            in = openInputStream(file);
            final long length = file.length();
            if (length > 0) {
                return IOUtils.toByteArray(in, length);
            } else {
                return IOUtils.toByteArray(in);
            }
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    public static FileInputStream openInputStream(File file) throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new IOException("File '" + file + "' exists but is a directory");
            }
            if (!file.canRead()) {
                throw new IOException("File '" + file + "' cannot be read");
            }
        } else {
            throw new FileNotFoundException("File '" + file + "' does not exist");
        }
        return new FileInputStream(file);
    }
}
