package com.oaspi.system.domain.vo;

import java.util.List;

public class CheckSysChunkVO {

    private boolean skipUpload;
    private List<Long>  uploads;

    public boolean getSkipUpload() {
        return skipUpload;
    }

    public void setSkipUpload(boolean skipUpload) {
        this.skipUpload = skipUpload;
    }

    public List<Long> getUploads() {
        return uploads;
    }

    public void setUploads(List<Long> uploads) {
        this.uploads = uploads;
    }
}
