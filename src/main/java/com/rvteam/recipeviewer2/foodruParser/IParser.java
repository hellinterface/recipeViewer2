package com.rvteam.recipeviewer2.foodruParser;

import java.io.IOException;

public interface IParser {
    public ParserResult parseURL(String url) throws IOException;
}
