package com.ghasto.create_so.util;

public enum BeltEnding{
    UNRESOLVED(0), EJECT(0), INSERT(.25f), FUNNEL(.5f), BLOCKED(.45f);

    public float margin;

    BeltEnding(float f) {
        this.margin = f;
    }
}
