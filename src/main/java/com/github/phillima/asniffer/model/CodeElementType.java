package com.github.phillima.asniffer.model;

import com.google.gson.annotations.SerializedName;

public enum CodeElementType {
    @SerializedName("interface") INTERFACE,
    @SerializedName("class") CLASS,
    @SerializedName("enum") ENUM,
    @SerializedName("annotation-declaration") ANNOTATION_DECLARATION,
    @SerializedName("method") METHOD,
    @SerializedName("field") FIELD,
    @SerializedName("package") PACKAGE,
    @SerializedName("annotation") ANNOTATION,
    @SerializedName("schema") SCHEMA,
    @SerializedName("constructor") CONSTRUCTOR;
}