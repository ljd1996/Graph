package com.hearing.graph.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.FileInputStream;

import com.hearing.graph.bean.Node;
import com.hearing.graph.util.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class GraphHelper {

    private static final int MAX = 128;
    public static final int TYPE_MULTI = 0;
    public static final int TYPE_SIMPLE = 1;

    private static final int TYPE_QUERY = 2;
    private static final int TYPE_MATCH = 3;

    public static List<Node> match(String path, String source) {
        return handleWord(path, source, TYPE_MATCH);
    }

    public static List<Node> query(String path, String source) {
        return handleWord(path, source, TYPE_QUERY);
    }

    public static List<Node> handleWord(String path, String source, int type) {
        if (Util.isEmpty(path)) {
            return null;
        }
        if (Util.isEmpty(source)) {
            return doGraph(path, TYPE_MULTI);
        }

        File dir = new File(path);
        File[] files = dir.listFiles();
        if (files == null || files.length <= 0) {
            System.out.println(path + "has no file");
            return null;
        }
        List<Node> nodes = new ArrayList<>();
        try {
            for (File f : files) {
                if (f.getName().toLowerCase().contains(".csv")) {
                    System.out.println("parse file " + f.getAbsolutePath());

                    List<Node> single = null;
                    switch (type) {
                        case TYPE_QUERY:
                            single = doQuery(f.getAbsolutePath(), source);
                            break;
                        case TYPE_MATCH:
                            single = doMatch(f.getAbsolutePath(), source);
                            break;
                    }
                    if (single != null) {
                        nodes.addAll(single);
                    }
                }
            }
        } catch (Exception e) {
            return null;
        }
        return nodes;
    }

    public static List<Node> doGraph(String path, int type) {
        if (Util.isEmpty(path)) {
            return null;
        }
        File dir = new File(path);
        File[] files = dir.listFiles();
        if (files == null || files.length <= 0) {
            System.out.println(path + "has no file");
            return null;
        }
        List<Node> nodes = new ArrayList<>();
        try {
            for (File f : files) {
                if (f.getName().toLowerCase().contains(".csv")) {
                    System.out.println("parse file " + f.getAbsolutePath());
                    switch (type) {
                        case TYPE_MULTI:
                            nodes.addAll(getMulti(f.getAbsolutePath()));
                            break;
                        case TYPE_SIMPLE:
                            nodes.addAll(getSimple(f.getAbsolutePath()));
                            break;
                    }
                }
            }
        } catch (Exception e) {
            return null;
        }
        return nodes;
    }

    private static List<Node> doMatch(String path, String source) throws Exception {
        if (Util.isEmpty(path) || Util.isEmpty(source)) {
            return null;
        }
        Util.TableInfo tableInfo = Util.getTableInfo(path);
        if (!Util.isLegalTable(tableInfo)) {
            return null;
        }
        String[][] tree = new String[tableInfo.row][tableInfo.column];
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));
        String line;
        int currentRow = 0;
        while ((line = reader.readLine()) != null) {
            String[] item = line.split(",");
            tree[currentRow] = item;
            currentRow++;
        }
        reader.close();

        String[] title = tree[0];
        List<Node> nodes = new ArrayList<>();
        String[] sources = source.split(" ");
        for (int row = 1; row < tableInfo.row; row++)
            for (int i = 0; i < tableInfo.column; i++)
                for (int j = 0; j < tableInfo.column; j++) {
                    if (i == j || Util.isEmpty(tree[row][i]) || Util.isEmpty(tree[row][j])
                            || title.length <= j || Util.isEmpty(title[j]) || !inMatch(tree, row, i, j, sources)) {
                        continue;
                    }
                    Node node = new Node(tree[row][i], tree[row][j], title[j]);
                    System.out.println("Add node: " + node);
                    nodes.add(node);
                }

        return nodes;
    }

    private static boolean inMatch(String[][] tree, int row, int i, int j, String[] sources) {
        return isMatch(tree, row, i, sources) && isMatch(tree, row, j, sources);
    }

    private static boolean isMatch(String[][] tree, int row, int column, String[] sources) {
        if (columnInSrc(tree, column, sources)) {
            return Util.contains(sources, tree[row][column]);
        }
        return true;
    }

    private static boolean columnInSrc(String[][] tree, int column, String[] sources) {
        for (String[] strings : tree) {
            if (Util.contains(sources, strings[column])) {
                return true;
            }
        }
        return false;
    }

    private static List<Node> doQuery(String path, String source) throws Exception {
        if (Util.isEmpty(path) || Util.isEmpty(source)) {
            return null;
        }
        source = source.trim();
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));
        String[] title = reader.readLine().split(",");
        String line = null;
        List<Node> nodes = new ArrayList<>();
        String[] sources = source.split(" ");

        while ((line = reader.readLine()) != null) {
            String[] item = line.split(",");
            if (item == null || item.length <= 1) continue;
            for (int i = 0; i < item.length; i++)
                for (int j = 0; j < item.length; j++) {
                    if (i == j || Util.isEmpty(item[i]) || Util.isEmpty(item[j]) || title.length <= j
                            || Util.isEmpty(title[j]))
                        continue;
                    boolean flag = false;
                    for (String s : sources) {
                        if (Util.isEquals(s, item[i])) {
                            flag = true;
                        }
                    }
                    if (flag) {
                        nodes.add(new Node(item[i], item[j], title[j]));
                    }
                }
        }
        reader.close();
        return nodes;
    }

    private static List<Node> getMulti(String path) throws Exception {
        if (Util.isEmpty(path)) {
            return null;
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));
        String[] title = reader.readLine().split(",");
        String line = null;
        List<Node> nodes = new ArrayList<>();
        while ((line = reader.readLine()) != null) {
            String[] item = line.split(",");
            if (item == null || item.length <= 1) continue;
            for (int i = 0; i < item.length; i++)
                for (int j = 0; j < item.length; j++) {
                    if (i == j || Util.isEmpty(item[i]) || Util.isEmpty(item[j])
                            || title.length <= j || Util.isEmpty(title[j]))
                        continue;
                    nodes.add(new Node(item[i], item[j], title[j]));
                }
        }
        reader.close();
        return nodes;
    }

    private static List<Node> getSimple(String path) throws Exception {
        if (Util.isEmpty(path)) {
            return null;
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));
        String[] title = reader.readLine().split(",");
        String line = null;
        List<Node> nodes = new ArrayList<>();
        while ((line = reader.readLine()) != null) {
            String[] item = line.split(",");
            if (item == null || item.length <= 1) continue;
            for (int i = 1; i < item.length; i++) {
                if (Util.isEmpty(item[i]) || title.length <= i || Util.isEmpty(title[i]))
                    continue;
                nodes.add(new Node(item[0], item[i], title[i]));
            }
        }
        reader.close();
        return nodes;
    }
}