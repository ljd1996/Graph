package com.hearing.graph.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.hearing.graph.util.GraphHelper;
import com.hearing.graph.util.TableHelper;
import com.hearing.graph.bean.Node;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

@Controller
public class GraphController {

    @GetMapping(value = {"index", "/"})
    public String index() {
        return "index";
    }

    @GetMapping("query")
    public String query(Map<String, Object> map,
        @RequestParam(value = "path", required = true) String path,
        @RequestParam(value = "key", required = false) String key) {
        System.out.println("path = " + path + ", key = " + key);
        List<Node> nodes = GraphHelper.query(path, key);
        map.put("data", nodes == null ? new ArrayList<>() : nodes);
        return "graph";
    }

    @GetMapping("graph")
    public String graph(Map<String, Object> map,
    @RequestParam(value = "path", required = true) String path,
    @RequestParam(value = "type", required = false) Integer type) {
        System.out.println("path = " + path + ", type = " + type);
        if (type == null) {
            type = GraphHelper.TYPE_MULTI;
        }
        List<Node> nodes = GraphHelper.doGraph(path, type);
        map.put("data", nodes == null ? new ArrayList<>() : nodes);
        return "graph";
    }
}
