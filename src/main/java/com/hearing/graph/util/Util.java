package com.hearing.graph.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

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

    public static boolean contains(String[] sources, String s) {
        for (String source : sources) {
            if (isEquals(source, s)) {
                return true;
            }
        }
        return false;
    }

    public static TableInfo getTableInfo(String path) {
        File file = new File(path);
        if (file.exists()) {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));
                String line;
                int row = 0;
                int column = 0;
                while ((line = reader.readLine()) != null) {
                    String[] items = line.trim().split(",");
                    if (column < items.length) {
                        column = items.length;
                    }
                    row++;
                }
                System.out.println("Table info of " + path + ": " + row + " " + column);
                return new TableInfo(row, column);
            } catch (Exception e) {
            }
        }
        return null;
    }

    public static boolean isLegalTable(TableInfo info) {
        return info != null && info.column > 0 && info.row > 0;
    }

    public static class TableInfo {
        public int row;
        public int column;

        public TableInfo(int row, int column) {
            this.row = row;
            this.column = column;
        }
    }
}
