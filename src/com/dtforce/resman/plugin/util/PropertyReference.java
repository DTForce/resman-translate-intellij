package com.dtforce.resman.plugin.util;

public class PropertyReference {
    private String className;
    private String key;

    public PropertyReference(String className, String key) {
        this.className = className;
        this.key = key;
    }

    public String getClassName() {
        return className;
    }

    public String getKey() {
        return key;
    }
}
