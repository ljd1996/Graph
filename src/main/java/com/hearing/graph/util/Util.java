package com.hearing.graph.util;

import java.io.File;

public class Util {

    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    public static boolean isEquals(String s1, String s2) {
        if (isEmpty(s1) || isEmpty(s2)) {
            return false;
        }
        return s1.trim().equals(s2.trim());
    }

    public static void ensureDir(String path) {
        if (!isEmpty(path)) {
            File f = new File(path);
            if (f.exists()) {
                if (!f.isDirectory()) {
                    f.delete();
                    f.mkdirs();
                }
            } else {
                f.mkdirs();
            }
        }
    }

    public static String getPath(String parent, String name) {
        if (parent.endsWith(String.valueOf(File.separatorChar))) {
            return parent + name;
        } else {
            return parent + File.separatorChar + name;
        }
    }

    public static String getName(String fName) {
        if (!isEmpty(fName) && fName.contains(".")) {
            return fName.substring(0, fName.lastIndexOf("."));
        }
        return fName;
    }
}
