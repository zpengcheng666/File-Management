package com.oaspi.system.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author：zpc
 * @Date：2025/1/4
 */
@Data
public class DeptTreeNodeVO implements Serializable {
    private Long id;
    private Long pid;
    private String name;
    private String deptCode;
    private List<DeptTreeNodeVO> children = new ArrayList<>();
    public void addChild(DeptTreeNodeVO child){
        if (this.children == null){
            this.children = new ArrayList<>();
        }
        this.children.add(child);
    }
}
