package com.oaspi.system.util.minio;

import lombok.Data;

@Data
public class MultiPartCompleteResp {
    private String uploadId;
    private boolean succeed;
}
