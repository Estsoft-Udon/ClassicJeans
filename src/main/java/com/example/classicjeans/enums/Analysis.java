package com.example.classicjeans.enums;

public enum Analysis {
    BASIC("기본 검진"),
    DEMENTIA("치매 검진");

    private final String displayName;

    Analysis(String displayName) {this.displayName = displayName;}

    public String getDisplayName() {return displayName;}
}
