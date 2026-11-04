package com.github.ase.impl;

public enum Color {
    BLACK("Black"),
    WHITE("White");

    private final String displayName;
    Color(String displayName) {
        this.displayName = displayName;
    }

    public String getNameTitleCase() {
        return displayName;
    }
}
