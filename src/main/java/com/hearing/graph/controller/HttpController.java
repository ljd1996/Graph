package com.hearing.graph.controller;

import com.hearing.graph.util.TableHelper;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HttpController {

    @GetMapping("generate")
    public String generate(@RequestParam(value = "class_path", required = true) String classPath,
        @RequestParam(value = "person_path", required = true) String personPath) {
            TableHelper tableHelper = new TableHelper();
            if (tableHelper.init(classPath, personPath)) {
                try {
                    tableHelper.doWork();
                    return tableHelper.getGeneratePath();
                } catch (Exception e) {
                    e.printStackTrace();
                    return "";
                }
            }
            return "";
    }
}
