package com.github.phillima.asniffer.model;

public enum PackageType {
    UNNAMED("unnamed");

    private final String packageTypeText;    

    private PackageType(final String text) {
        this.packageTypeText = text;
    }
    
    @Override
    public String toString() {
        return packageTypeText;
    }
}