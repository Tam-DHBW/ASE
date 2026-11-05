package com.github.ase.impl;

public enum Color {
    BLACK("Black"),
    WHITE("White");

    private final String titleCaseName;
    Color(String titleCaseName) {
        this.titleCaseName = titleCaseName;
    }

    public String getNameTitleCase() {
        return titleCaseName;
    }

    public Color getOpponent() {
        return switch (this) {
            case BLACK -> WHITE;
            case WHITE -> BLACK;
        };
    }
}
