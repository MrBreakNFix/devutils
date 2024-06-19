package com.mrbreaknfix.utils;

public class SkinPayload {
    private String variant;
    private String url;

    public SkinPayload(String variant, String url) {
        this.variant = variant;
        this.url = url;
    }

    public String getVariant() {
        return variant;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
