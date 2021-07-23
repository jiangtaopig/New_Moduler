/*
 * Copyright (c) 2015-2016 BiliBili Inc.
 */

package com.zjt.startmodepro.cpu_info;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.TreeMap;

public class BlinkCpuInfo {

    private static final String TAG = "CpuInfo";

    private static BlinkCpuInfo sInstance;
    public String mRawCpuInfo = "";
    public TreeMap<String, String> mRawInfoMap = new TreeMap<String, String>();

    public static BlinkCpuInfo parseCpuInfo() {
        if (sInstance != null)
            return sInstance;

        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream("/proc/cpuinfo");
        } catch (FileNotFoundException e) {
            Log.d(TAG, "parse cpu info:" + e.getMessage());
            return null;
        }

        InputStreamReader reader = new InputStreamReader(inputStream, Charset.defaultCharset());
        BufferedReader buffReader = new BufferedReader(reader);

        BlinkCpuInfo blinkCpuInfo = new BlinkCpuInfo();

        try {
            StringBuilder infoBuilder = new StringBuilder();
            String line;
            while ((line = buffReader.readLine()) != null) {
                blinkCpuInfo.addCpuInfo(line);

                infoBuilder.append(line);
                infoBuilder.append('\n');
            }
            blinkCpuInfo.mRawCpuInfo = infoBuilder.toString();
        } catch (IOException e) {
            Log.d(TAG, "parse cpu info:" + e.getMessage());
            blinkCpuInfo = null;
        } finally {
            try {
                buffReader.close();
                reader.close();
                inputStream.close();
            } catch (Exception e) {
                Log.d(TAG, "parse cpu info:" + e.getMessage());
            }
        }
        sInstance = blinkCpuInfo;
        return blinkCpuInfo;
    }

    public void addCpuInfo(String line) {
        String[] lineInfo = line.split(":", 2);
        if (lineInfo.length >= 2) {
            addCpuInfo(lineInfo[0], lineInfo[1]);
        }
    }

    @SuppressWarnings("StringSplitter")
    public void addCpuInfo(String key, String value) {
        key = key.toLowerCase(Locale.US).trim();
        value = value.trim();
        mRawInfoMap.put(key, value);
    }
}
