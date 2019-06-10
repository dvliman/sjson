package com.dvliman.sjson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class JsonLexer {
    static JsonToken jsonString(String input) {
        String jsonString = "";

        if (input.charAt(0) != JsonToken.JSON_QUOTE) {
            return null;
        }

        input = input.substring(1); // remove first quote
        for (char c: input.toCharArray()) {
            if (c == JsonToken.JSON_QUOTE) {
                String remaining = input.substring(jsonString.length() + 1);
                return new JsonToken(String.class, jsonString, remaining);
            }
            jsonString += c;
        }

        return null;
    }

    static JsonToken jsonNumber(String input) {
        String jsonNumber = "";

        for (char c: input.toCharArray()) {
            if (isNumber(c)) {
                jsonNumber += c;
            } else {
                break;
            }
        }

        if (jsonNumber == "")
            return null;

        String remaining = input.substring(jsonNumber.length());

        return new JsonToken(Integer.class,
            Integer.parseInt(jsonNumber),
            remaining);
    }

    static int trueLength  = "true".length();
    static int falseLength = "false".length();
    static int nullLength  = "null".length();

    static JsonToken jsonBoolean(String input) {
        if (input.length() >= trueLength && input.substring(0, trueLength) == "true")
            return new JsonToken(Boolean.class, true, input.substring(trueLength));

        if (input.length() >= falseLength && input.substring(0, falseLength) == "false")
            return new JsonToken(Boolean.class, false, input.substring(falseLength));

        return null;
    }

    static JsonToken jsonNull(String input) {
        if (input.length() >= nullLength && input.substring(0, nullLength) == "null")
            return new JsonToken(Object.class, null, input.substring(nullLength));

        return null;
    }

    static List<JsonToken> tokens(String input) throws Exception {
        ArrayList<JsonToken> tokens = new ArrayList();

        while (input.length() > 0) {

            JsonToken t1 = jsonString(input);
            if (t1 != null) {
                tokens.add(t1);
                input = t1.remaining;
                continue;
            }

            JsonToken t2 = jsonNumber(input);
            if (t2 != null) {
                tokens.add(t2);
                input = t2.remaining;
                continue;
            }

            JsonToken t3 = jsonBoolean(input);
            if (t3 != null) {
                tokens.add(t3);
                input = t3.remaining;
                continue;
            }

            JsonToken t4 = jsonNull(input);
            if (t4 != null) {
                tokens.add(t4);
                input = t4.remaining;
                continue;
            }

            char c = input.charAt(0);

            if (isWhitespace(c)) { // ignore whitespace
                input = input.substring(1);

            } else if (isJsonSyntax(c)) {
                tokens.add(new JsonToken(Character.class, c, null));
                input = input.substring(1);
            } else {
                throw new Exception(String.format("unexpected character: %s", c));
            }

        }

        return tokens;
    }

    static boolean isNumber(char c) {
        List<Integer> validNumbers = Arrays.asList(
            0, 1, 2, 3, 4, 5, 6, 7, 8, 9);

        return validNumbers.contains(Character.getNumericValue(c));
    }

    static boolean isWhitespace(char c) {
        return Arrays.asList(' ', '\t', '\b', '\n', '\r').contains(c);
    }

    static boolean isJsonSyntax(char c) {
        return Arrays.asList(
            JsonToken.JSON_COMMA,
            JsonToken.JSON_COLON,
            JsonToken.JSON_LEFT_BRACKET,
            JsonToken.JSON_RIGHT_BRACKET,
            JsonToken.JSON_LEFT_CURLY_BRACKET,
            JsonToken.JSON_RIGHT_CURLY_BRACKET).contains(c);
    }
}
