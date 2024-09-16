package com.module.connect.util;

import android.os.Parcelable;

import com.tencent.mmkv.MMKV;

/**
 * 键值对存取工具类，进程安全
 */
public class KeyValueUtils {
    private static MMKV mmkv = MMKV.mmkvWithID("InterProcessKV", MMKV.MULTI_PROCESS_MODE);


    public static void setInt(String key, Integer value) {
        mmkv.encode(key, value);
    }

    public static int getInt(String key) {
        return getInt(key, 0);
    }

    public static int getInt(String key, int defaultValue) {
        return mmkv.decodeInt(key, defaultValue);
    }

    public static void setLong(String key, long value) {
        mmkv.encode(key, value);
    }

    public static long getLong(String key) {
        return getLong(key, 0);
    }

    public static long getLong(String key, long defaultValue) {
        return mmkv.decodeLong(key, defaultValue);
    }

    public static void setBoolean(String key, boolean value) {
        mmkv.encode(key, value);
    }

    public static boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        return mmkv.decodeBool(key, defaultValue);
    }

    public static void setDouble(String key, double value) {
        mmkv.encode(key, value);
    }

    public static double getDouble(String key) {
        return getDouble(key, 0.0);
    }

    public static double getDouble(String key, double defaultValue) {
        return mmkv.decodeDouble(key, defaultValue);
    }

    public static void setFloat(String key, float value) {
        mmkv.encode(key, value);
    }

    public static float getFloat(String key) {
        return getFloat(key, 0f);
    }

    public static float getFloat(String key, float defaultValue) {
        return mmkv.decodeFloat(key, defaultValue);
    }

    public static void setString(String key, String value) {
        mmkv.encode(key, value);
    }

    public static String getString(String key) {
        return getString(key, "");
    }

    public static String getString(String key, String defaultValue) {
        return mmkv.decodeString(key, defaultValue);

    }

    public static void setParcelable(String key, Parcelable value) {
        mmkv.encode(key, value);
    }

    public static <T extends Parcelable> T getParcelable(String key, Class<T> clazz) {
        return mmkv.decodeParcelable(key, clazz);
    }

    public static void remove(String key) {
        mmkv.remove(key);
    }

    public static void removeValueForKey(String key) {
        mmkv.removeValueForKey(key);
    }
    public static void removeValuesForKeys(String[] key) {
        mmkv.removeValuesForKeys(key);
    }
    public static void clear() {
        mmkv.clearAll();
    }
}
