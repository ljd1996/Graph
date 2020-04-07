package com.hearing.graph.bean;

public class Node {
    public String source = "";
    public String target = "";
    public String rela = "";
    public String type = "resolved";

    public Node(String source, String target, String rela) {
        this.source = source;
        this.target = target;
        this.rela = rela;
    }
}
