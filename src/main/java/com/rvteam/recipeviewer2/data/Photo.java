package com.rvteam.recipeviewer2.data;

import java.util.Arrays;

public class Photo implements IEntity {

    private Integer id;
    private byte[] bytes;

    public Photo(Integer id, byte[] bytes) {
        this.id = id;
        this.bytes = bytes;
    }

    @Override
    public Integer getID() {
        return this.id;
    }

    @Override
    public void setID(int n) {
        this.id = n;
    }

    public byte[] getBytes() { return this.bytes; }
    public int getHash() {
        return Arrays.hashCode(this.bytes);
    }
}
