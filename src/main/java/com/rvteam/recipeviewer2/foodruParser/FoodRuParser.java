package com.rvteam.recipeviewer2.foodruParser;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class FoodRuParser implements IParser {
    private byte[] getImage(String imageSrc) {
        try {
            Connection con = Jsoup.connect(imageSrc);
            Connection.Response resp = con.ignoreContentType(true).execute();
            return resp.bodyAsBytes();
        }
        catch(IOException exception) {
            throw new RuntimeException(exception);
        }
    }
    public ParserResult parseURL(String url) throws IOException {
            Document document = Jsoup.connect(url).get();
            System.out.println(document.title());
            // Название ------------------------------------
            String title = "";
            Element titleElement = document.selectFirst("#description [itemprop=\"name\"]");
            if (titleElement != null) title = titleElement.text();
            // Описание ------------------------------------
            String description = "";
            Element descriptionElement = document.selectFirst("[itemprop=\"description\"]");
            if (descriptionElement != null) description = descriptionElement.text();
            // Категория ------------------------------------
            String category = "";
            Elements categoryElement = document.select("[aria-label=\"Breadcrumb\"] [itemprop=\"name\"]");
            if (!categoryElement.isEmpty() && categoryElement.size() > 2) {
                category = categoryElement.get(1).text();
            }
            // Время ------------------------------------
            int time = 0;
            String timeString = "";
            Element timeElement = document.selectFirst(".duration");
            if (timeElement != null) {
                String[] timeString_split = timeElement.text().split(" ");
                for (var i : timeString_split) {
                    try {
                        int k = Integer.parseInt(i);
                        time = time*60 + k;
                    }
                    catch(Exception e) {}
                }
            }
            // Сложность ------------------------------------
            int difficulty = 1;
            Elements difficultyElements = document.select(".properties_level__U_Lm9 .properties_iconActive__Qf5Nf");
            difficulty = difficultyElements.size();
            // Фотография ------------------------------------
            byte[] photo = new byte[]{};
            Element photoElement = document.selectFirst(".cover_themeLogoWrapper__m1zPo link[itemprop=\"url\"]");
            if (photoElement != null) {
                String imageSrc = photoElement.attr("href");
                photo = getImage(imageSrc);
            }
            // Шаги ------------------------------------
            Elements stepElements = document.select(".instruction");
            ArrayList<ParserResultStep> stepArray = new ArrayList<ParserResultStep>(); // Создание массива с шагами
            for (var stepElement : stepElements) {
                // Фотография ------------------------------------
                Element imageElement = stepElement.selectFirst("img.photo");
                byte[] imageAsByteArray = new byte[]{};
                if (imageElement != null) {
                    String imageSrc = imageElement.attr("src");
                    imageAsByteArray = getImage(imageSrc);
                }
                // Текст ------------------------------------
                String text = stepElement.text();
                //Element textElement = step.selectFirst("[itemprop=\"text\"]");
                //if (textElement != null) text = textElement.text();
                Element adviceElement = stepElement.selectFirst("stepContent_advice__HdTTo");
                if (adviceElement != null) text += "\n\n" + adviceElement.text();
                stepArray.add( new ParserResultStep(text, imageAsByteArray) ); // Добавление в массив
            }
            // Продукты ------------------------------------
            Elements ingredientElements = document.select("[itemprop=\"recipeIngredient\"]");
            ArrayList<ParserResultIngredient> ingredientArray = new ArrayList<ParserResultIngredient>();
            for (var ingredientElement : ingredientElements) {
                // Название ------------------------------------
                String name = "";
                Element nameElement = ingredientElement.selectFirst(".name");
                if (nameElement != null) name = nameElement.text();
                // Вес ------------------------------------
                Element weightTypeElement = ingredientElement.selectFirst(".type");
                float weight = 0;
                float zero = 0;
                if (weightTypeElement != null) {
                    if (weightTypeElement.text().equals("г")) {
                        Element weightElement = ingredientElement.selectFirst(".value");
                        if (nameElement != null) weight = Float.parseFloat(weightElement.text());
                    }
                }
                ingredientArray.add( new ParserResultIngredient(name, weight, zero, zero, zero, zero) ); // Добавление в массив
            }
            // Результат
            ParserResult output = new ParserResult(title, description, time, difficulty, category, photo, stepArray, ingredientArray);
            return output;
    }
}
