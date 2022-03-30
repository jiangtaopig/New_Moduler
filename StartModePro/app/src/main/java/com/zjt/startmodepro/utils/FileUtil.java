package com.zjt.startmodepro.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Pair;

import com.zjt.startmodepro.MyApplication;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;

public class FileUtil {

    private static final String TAG = "FileUtil";
    private static String pathDiv = "/";
    private static File cacheDir = !isExternalStorageWritable() ? getInnerCacheDir() : getExternalCacheDir();
    public static final String ZUIFULI_DIR_PATH = Environment.getExternalStorageDirectory()
            + File.separator + "zhujt" + File.separator;

    private FileUtil() {
    }


    /**
     * 检查磁盘空间是否大于10mb
     *
     * @return true 大于
     */
    public static boolean isDiskAvailable() {
        long size = getDiskAvailableSize();
        return size > 10 * 1024 * 1024; // > 10bm
    }

    /**
     * 获取磁盘可用空间
     *
     * @return byte 单位 kb
     */
    public static long getDiskAvailableSize() {
        if (!existsSdcard()) return 0;
        File path = Environment.getExternalStorageDirectory(); // 取得sdcard文件路径
        StatFs stat = new StatFs(path.getAbsolutePath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize;
        // (availableBlocks * blockSize)/1024 KIB 单位
        // (availableBlocks * blockSize)/1024 /1024 MIB单位
    }

    public static Boolean existsSdcard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static long getFileOrDirSize(File file) {
        if (!file.exists()) return 0;
        if (!file.isDirectory()) return file.length();

        long length = 0;
        File[] list = file.listFiles();
        if (list != null) { // 文件夹被删除时, 子文件正在被写入, 文件属性异常返回null.
            for (File item : list) {
                length += getFileOrDirSize(item);
            }
        }

        return length;
    }


    public static String assetFile2Str(String assetUrl) {
        InputStream in = null;
        try {
            in = MyApplication.getContext().getAssets().open(assetUrl);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String line = null;
            StringBuilder sb = new StringBuilder();
            do {
                line = bufferedReader.readLine();
                if (line != null && !line.matches("^\\s*\\/\\/.*")) {
                    sb.append(line);
                }
            } while (line != null);

            bufferedReader.close();
            in.close();

            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
        return null;
    }

    // 获取指定文件夹内所有文件大小的和
    public static long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (File aFileList : fileList) {
                if (aFileList.isDirectory()) {
                    size = size + getFolderSize(aFileList);
                } else {
                    size = size + aFileList.length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    // 格式化单位
    public static Pair<String, String> getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return new Pair<>(String.valueOf(size), "Byte");
        }
        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return new Pair<>(result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString(), "KB");
        }
        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return new Pair<>(result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString(), "MB");
        }
        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return new Pair<>(result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString(), "GB");
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return new Pair<>(result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString(), "TB");
    }

    public static void deleteFile(File directory) {
        if (!directory.exists()) {
            return;
        }
        if (directory.isFile()) {
            directory.delete();
            return;
        }
        if (directory.isDirectory()) {
            File[] childFile = directory.listFiles();
            if (childFile == null || childFile.length == 0) {
                directory.delete();
                return;
            }
            for (File f : childFile) {
                deleteFile(f);
            }
            directory.delete();
        }
    }

    /**
     * 解析Famtter拿到的文件大小单位
     *
     * @param formatSize
     * @return
     */
    public static String getFormatUnit(String formatSize) {
        String unit = "";
        if (TextUtils.isEmpty(formatSize)) {
            return unit;
        }
        for (int i = 0; i < formatSize.length(); i++) {
            char c = formatSize.charAt(i);
            if ((c < '0' || c > '9') && c != '.') {
                unit = formatSize.substring(i);
                break;
            }
        }
        return unit;
    }

    public static void deleteDirectory(String filepath) throws IOException {
        File f = new File(filepath);

        if (f.exists() && f.isDirectory()) {
            if (f.listFiles().length == 0) {
                f.delete();
            } else {
                File delFile[] = f.listFiles();
                int i = f.listFiles().length;
                for (int j = 0; j < i; j++) {
                    if (delFile[j].isDirectory()) {
                        deleteDirectory(delFile[j].getAbsolutePath());
                    }
                    delFile[j].delete();
                }
            }
        }
    }

    /**
     * Delete a directory and all its contents
     */
    public static void deleteDirectory(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteDirectory(child);

        fileOrDirectory.delete();
    }



    public static File getInnerCacheDir() {
        return MyApplication.getContext().getFilesDir();
    }

    public static File getExternalCacheDir() {
        return MyApplication.getContext().getExternalCacheDir();
    }

    /**
     * 创建临时文件
     *
     * @param type 文件类型
     */
    public static File getTempFile(FileType type) {
        try {
            File file = File.createTempFile(type.toString(), null, cacheDir);
            return file;
        } catch (IOException e) {
            return null;
        }
    }


    /**
     * 获取缓存文件地址
     */
    public static String getCacheFilePath(String fileName) {
//        return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + fileName;
        return cacheDir.getAbsolutePath() + pathDiv + fileName;
    }

    public static String getCachePath(){
        return cacheDir.getAbsolutePath();
    }

    /**
     * 判断缓存文件是否存在
     */
    public static boolean isCacheFileExist(String fileName) {
        File file = new File(getCacheFilePath(fileName));
        return file.exists();
    }


    /**
     * 将图片存储为文件
     *
     * @param bitmap 图片
     */
    public static String createFile(Bitmap bitmap, String filename) {
        File f = new File(cacheDir, filename);
        try {
            if (f.createNewFile()) {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
                byte[] bitmapdata = bos.toByteArray();
                FileOutputStream fos = new FileOutputStream(f);
                fos.write(bitmapdata);
                fos.flush();
                fos.close();
            }
        } catch (IOException e) {
            Log.e(TAG, "create bitmap file error" + e);
        }
        if (f.exists()) {
            return f.getAbsolutePath();
        }
        return null;
    }

    /**
     * 将图片存储为文件
     *
     * @param bitmap 图片
     */
    public static String saveBm2File(Bitmap bitmap, String absoluteFilename) {
        File f = new File(absoluteFilename);
        try {
            if (f.createNewFile()) {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
                byte[] bitmapdata = bos.toByteArray();
                FileOutputStream fos = new FileOutputStream(f);
                fos.write(bitmapdata);
                fos.flush();
                fos.close();
            }
        } catch (IOException e) {
            Log.e(TAG, "create bitmap file error" + e);
        }
        if (f.exists()) {
            return f.getAbsolutePath();
        }
        return null;
    }

    /**
     * 将数据存储为文件
     *
     * @param data 数据
     */
    public static void createFile(byte[] data, String filename) {
        File f = new File(cacheDir, filename);
        try {
            if (f.createNewFile()) {
                FileOutputStream fos = new FileOutputStream(f);
                fos.write(data);
                fos.flush();
                fos.close();
            }
        } catch (IOException e) {
            Log.e(TAG, "create file error" + e);
        }
    }


    public static String readFile(String dirName, String fileName) {
        String fileContent = "";
        try {
            File f = new File(cacheDir, dirName + pathDiv + fileName);
            if (f.isFile() && f.exists()) {
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(f), "gbk");
                BufferedReader reader = new BufferedReader(read);
                String line;
                while ((line = reader.readLine()) != null) {
                    fileContent += line + "\n";
                }
                read.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileContent;
    }

    public static void writeFile(String dirName, String fileName, String fileContent) {
        try {
//            File file = getInnerCacheDir().getParentFile(); // /data/user/0/com.zjt.startmodepro
            File dir = new File(cacheDir, dirName);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File f = new File(dir.getAbsoluteFile(), fileName);
            if (!f.exists()) {
                f.createNewFile();
            }
            OutputStreamWriter write = new OutputStreamWriter(
                    new FileOutputStream(f, true), "gbk");
            BufferedWriter writer = new BufferedWriter(write);
            writer.write(fileContent);

//            File tmpFile = new File(dir+"/bi/");
//            if (!tmpFile.exists()){
//                tmpFile.mkdirs();
//            }
            // 将文件移动到别的目录或者只是重命名，如果是移动到指定目录，那么必须保证该目录存在，如果不存在，类似上面先创建目录
//            f.renameTo(new File(tmpFile, "xxx.txt"));
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * 删除指定 目录下的制定文件
     * @param dirName 指定的目录
     * @param fileName 指定的文件
     * @return
     */
    public static synchronized boolean deleteFile(String dirName, String fileName) {
        File ff = new File(cacheDir, dirName + pathDiv + fileName);
        if (!ff.exists()) {
            return false;
        }
        if (ff.isFile()) {
            return ff.delete();
        }
        return false;
    }


    /**
     * 判断缓存文件是否存在
     */
    public static boolean isFileExist(String fileName, String type) {
        if (isExternalStorageWritable()) {
            File dir = MyApplication.getContext().getExternalFilesDir(type);
            if (dir != null) {
                File f = new File(dir, fileName);
                return f.exists();
            }
        }
        return false;
    }


    /**
     * 将数据存储为文件
     *
     * @param data     数据
     * @param fileName 文件名
     * @param type     文件类型
     */
    public static File createFile(String data, String fileName, String type) {
        if (isExternalStorageWritable()) {
            File dir = MyApplication.getContext().getExternalFilesDir(type);
            if (dir != null) {
                File f = new File(dir, fileName);
                try {
                    if (f.createNewFile()) {
                        FileOutputStream fos = new FileOutputStream(f);
                        fos.write(data.getBytes());
                        fos.flush();
                        fos.close();
                        return f;
                    }
                } catch (IOException e) {
                    Log.e(TAG, "create file error" + e);
                    return null;
                }
            }
        }
        return null;
    }


    /**
     * 从URI获取图片文件地址
     *
     * @param context 上下文
     * @param uri     文件uri
     */
//    public static String getImageFilePath(Context context, Uri uri) {
//        if (uri == null) {
//            return null;
//        }
//        String path = null;
//        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
//        if (isKitKat) {
//            if (!isMediaDocument(uri)) {
//                try {
//                    final String docId = DocumentsContract.getDocumentId(uri);
//                    final String[] split = docId.split(":");
//                    Uri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//                    final String selection = "_id=?";
//                    final String[] selectionArgs = new String[]{
//                            split[1]
//                    };
//                    path = getDataColumn(context, contentUri, selection, selectionArgs);
//                } catch (IllegalArgumentException e) {
//                    path = null;
//                }
//            }
//        }
//        if (path == null) {
//            String[] projection = {MediaStore.Images.Media.DATA};
//            Cursor cursor = ((Activity) context).managedQuery(uri, projection, null, null, null);
//            if (cursor != null) {
//                int column_index = cursor
//                        .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//                cursor.moveToFirst();
//                return cursor.getString(column_index);
//            }
//
//            path = null;
//        }
//        return path;
//    }


//    /**
//     * 从URI获取文件地址
//     *
//     * @param context 上下文
//     * @param uri     文件uri
//     */
//    public static String getFilePath(Context context, Uri uri) {
//        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
//        // DocumentProvider
//        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
//            // ExternalStorageProvider
//            if (isExternalStorageDocument(uri)) {
//                final String docId = DocumentsContract.getDocumentId(uri);
//                final String[] split = docId.split(":");
//                final String type = split[0];
//
//                if ("primary".equalsIgnoreCase(type)) {
//                    return Environment.getExternalStorageDirectory() + "/" + split[1];
//                }
//
//                // TODO handle non-primary volumes
//            }
//            // DownloadsProvider
//            else if (isDownloadsDocument(uri)) {
//
//                final String id = DocumentsContract.getDocumentId(uri);
//                final Uri contentUri = ContentUris.withAppendedId(
//                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
//
//                return getDataColumn(context, contentUri, null, null);
//            }
//            // MediaProvider
//            else if (isMediaDocument(uri)) {
//                final String docId = DocumentsContract.getDocumentId(uri);
//                final String[] split = docId.split(":");
//                final String type = split[0];
//
//                Uri contentUri = null;
//                if ("image".equals(type)) {
//                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//                } else if ("video".equals(type)) {
//                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
//                } else if ("audio".equals(type)) {
//                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//                }
//
//                final String selection = "_id=?";
//                final String[] selectionArgs = new String[]{
//                        split[1]
//                };
//
//                return getDataColumn(context, contentUri, selection, selectionArgs);
//            }
//        }
//        // MediaStore (and general)
//        else if ("content".equalsIgnoreCase(uri.getScheme())) {
//            return getDataColumn(context, uri, null, null);
//        }
//        // File
//        else if ("file".equalsIgnoreCase(uri.getScheme())) {
//            return uri.getPath();
//        }
//
//        return null;
//    }


    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    private static String getDataColumn(Context context, Uri uri, String selection,
                                        String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * 判断外部存储是否可用
     */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        Log.e(TAG, "ExternalStorage not mounted");
        return false;
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public enum FileType {
        IMG,
        AUDIO,
        VIDEO,
        FILE,
    }

    /**
     * 将图片转换成Base64编码的字符串
     */
    public static String imageToBase64(File file) {
        if (file == null || !file.exists() || !file.isFile()) {
            return null;
        }
        InputStream is = null;
        byte[] data = null;
        String result = null;
        try {
            is = new FileInputStream(file);
            //创建一个字符流大小的数组。
            data = new byte[is.available()];
            //写入数组
            is.read(data);
            //用默认的编码格式进行编码
            result = Base64.encodeToString(data, Base64.NO_CLOSE);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return result;
    }
}
