package com.comp2042.logic.bricks;

public enum BrickType {
    EMPTY(0, "empty"),
    I(1, "i-brick"),
    J(2, "j-brick"),
    L(3, "l-brick"),
    O(4, "o-brick"),
    S(5, "s-brick"),
    T(6, "t-brick"),
    Z(7, "z-brick");

    private final int id;
    private final String cssClass;

    BrickType(int id, String cssClass) {
        this.id = id;
        this.cssClass = cssClass;

    }

    public int getId() {
        return id;
    }

    public String getCssClass() {
        return cssClass;
    }

    public static BrickType getById(int id) {
        for (BrickType type : values()) {
            if (type.id == id) {
                return type;
            }
        }
        return  EMPTY;
    }
}