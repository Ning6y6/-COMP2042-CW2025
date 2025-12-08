package com.comp2042.logic.bricks;

/**
 * BrickType is part of the bricks package and encapsulates behaviour related to brick type.
 */
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

    /**
     * Returns the id.
     *
     * @return the result of the operation
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the css class.
     *
     * @return the result of the operation
     */
    public String getCssClass() {
        return cssClass;
    }

    /**
     * Returns the by id.
     *
     * @param id the id
     * @return the result of the operation
     */
    public static BrickType getById(int id) {
        for (BrickType type : values()) {
            if (type.id == id) {
                return type;
            }
        }
        return  EMPTY;
    }
}