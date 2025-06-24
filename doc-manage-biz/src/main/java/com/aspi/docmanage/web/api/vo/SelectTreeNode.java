package com.aspi.docmanage.web.api.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SelectTreeNode {
    private Long id;
    private Long pid;
    private String name;
    private List<SelectTreeNode> children = new ArrayList<>();
    public void addChild(SelectTreeNode child){
        if (this.children == null){
            this.children = new ArrayList<>();
        }
        this.children.add(child);
    }
}
