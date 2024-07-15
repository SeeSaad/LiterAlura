package br.com.alura.LiterAlura.service;

public class GuntexUrlConverter {
    private static final String partialUrl = "https://gutendex.com/books/";

    public static String searchBookURL(String name) {
        String newName = name.replace(" ", "%20");
        String search = "?search=" + newName;

        return partialUrl + search;
    }
}
