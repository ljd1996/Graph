package com.hearing.graph.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class TableHelper {

    private static final String GENERATE_DIR_NAME = "generate";

    private String mClassPath;
    private String mPersonPath;
    private String mGeneratePath;

    public String getGeneratePath() {
        return mGeneratePath;
    }

    public boolean init(String classPath, String personPath) {
        if (Util.isEmpty(classPath) || Util.isEmpty(personPath)) {
            return false;
        }
        mClassPath = classPath;
        mPersonPath = personPath;
        mGeneratePath = Util.getPath(mPersonPath, GENERATE_DIR_NAME);
        Util.ensureDir(mClassPath);
        Util.ensureDir(mPersonPath);
        Util.ensureDir(mGeneratePath);
        return true;
    }

    public void doWork() throws Exception {
        System.out.println("begin to generate");

        File[] classFiles = new File(mClassPath).listFiles();
        if (classFiles == null) {
            return;
        }
        File[] personFiles = new File(mPersonPath).listFiles();
        if (personFiles == null) {
            return;
        }
        
        Map<String, List<String>> classMap = new TreeMap<>();
        for (File classFile: classFiles) {
            if (classFile.getName().endsWith(".csv")) {
                classMap.put(Util.getName(classFile.getName()), parseClassFile(classFile));
            }
        }

        for (File personFile: personFiles) {
            if (!personFile.getName().endsWith(".csv")) {
                continue;
            }

            System.out.println("generate " + personFile.getAbsolutePath());

            String name =  GENERATE_DIR_NAME + "_" + personFile.getName();
            File file = new File(Util.getPath(mGeneratePath, name));
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(file, true), "UTF-8"));
            String line = ",";
            for (String key: classMap.keySet()) {
                line = line + key + ",";
            }
            bw.append(line + "\n");
            
            List<String> people = parsePersonFile(personFile);
            String holder = "";
            for (Map.Entry<String, List<String>> entry: classMap.entrySet()) {
                System.out.println("current class " + entry.getKey());

                for (String person: people) {
                    if (entry.getValue().contains(person)) {
                        bw.append(Util.getName(personFile.getName())+ "," + holder + person + "\n");
                    }
                }
                holder += ",";
            }
            bw.close();
        }
        
    }

    private List<String> parseClassFile(File classFile) throws Exception {
        List<String> list = new ArrayList<>();
        if (classFile != null && classFile.exists()) {
            BufferedReader classReader = new BufferedReader(new InputStreamReader(
                new FileInputStream(classFile), "UTF-8"));
            
            classReader.readLine();
            String line = null;

            while ((line = classReader.readLine()) != null) {
                String[] items = line.split(",");
                if (items != null) {
                    for (String s: items) {
                        if (!Util.isEmpty(s)) {
                            list.add(s);
                        }
                    }
                }
            }
            classReader.close();
        }
        return list;
    }

    private List<String> parsePersonFile(File personFile) throws Exception {
        List<String> list = new ArrayList<>();
        if (personFile != null && personFile.exists()) {
            BufferedReader personReader = new BufferedReader(new InputStreamReader(
                new FileInputStream(personFile), "UTF-8"));
            
            personReader.readLine();
            String line = null;

            while ((line = personReader.readLine()) != null) {
                String[] items = line.split(",");
                if (items != null && items.length >= 2) {
                    list.add(items[1].trim());
                }
            }
            personReader.close();
        }
        return list;
    }
}
