package com.oaspi.system.util.minio;

import lombok.Data;

@Data
public class PartFileResp {
    private int partNum;
    private String url;

    public PartFileResp(int partNum, String url) {
        this.partNum = partNum;
        this.url = url;
    }

}
