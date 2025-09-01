package com.mrfortuna.morsecraft.utils;

import java.util.Map;

public class MorseDecoder {
    private static final Map<String, String> MORSE = Map.ofEntries(
            Map.entry("....", "H"),
            Map.entry("..", "I")
            // TODO: abcde
    );

    public static String decode(String seq) {
        StringBuilder result = new StringBuilder();
        String[] words = seq.split("/");
        for (String word : words) {
            for (String letter : word.trim().split(" ")) {
                result.append(MORSE.getOrDefault(letter, "?"));
            }
            result.append(" ");
        }
        return result.toString().trim();
    }
}

