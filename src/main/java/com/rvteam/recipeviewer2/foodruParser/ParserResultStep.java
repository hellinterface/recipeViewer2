package com.rvteam.recipeviewer2.foodruParser;

public class ParserResultStep {
    private String text;
    private byte[] image;
    public ParserResultStep(String _text, byte[] _image) {
        this.text = _text;
        this.image = _image;
    }
    public String getText() {
        return this.text;
    }
    public byte[] getImage() {
        return this.image;
    }
}
