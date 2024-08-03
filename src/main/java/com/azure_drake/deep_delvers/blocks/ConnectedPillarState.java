package com.azure_drake.deep_delvers.blocks;

import net.minecraft.util.StringRepresentable;

public enum ConnectedPillarState implements StringRepresentable {
    Single("single"),
    Cap("cap"),
    Edge("edge"),
    Middle("middle");

    private final String name;

    ConnectedPillarState(String pName) {
        this.name = pName;
    }


    @Override
    public String getSerializedName() {
        return this.name;
    }
}
