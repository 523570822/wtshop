package com.wtshop.api.common.result;

import java.io.Serializable;

/**
 * Created by sq on 2017/11/1.
 */
public class ImageResult implements Serializable{

    private String originalFileName;

    private String urlName;

    public ImageResult(String originalFileName, String urlName) {
        this.originalFileName = originalFileName;
        this.urlName = urlName;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getUrlName() {
        return urlName;
    }

    public void setUrlName(String urlName) {
        this.urlName = urlName;
    }
}
