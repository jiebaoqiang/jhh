package com.jhh.dc.baika.entity.lakala.notify;

import java.io.InputStream;

import com.jhh.dc.baika.entity.lakala.webHook.SuperWebHookRequest;

public class ReconDownload extends SuperWebHookRequest {

    private InputStream in;

    private String fileName;

    private String privData;

    public InputStream getIn() {
        return in;
    }

    public void setIn(InputStream in) {
        this.in = in;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPrivData() {
        return privData;
    }

    public void setPrivData(String privData) {
        this.privData = privData;
    }
}
