package com.dvliman.sjson;

import javafx.util.Pair;
import java.util.*;

public class JsonParser {
    public static Object parse(List<JsonToken> tokens) throws Exception {
        if (tokens == null || tokens.isEmpty())
            return null;

        JsonToken first = head(tokens);

        if (first.value.equals(JsonToken.JSON_LEFT_BRACKET))
            return parseArray(removeFirst(tokens));

        if (first.value.equals(JsonToken.JSON_LEFT_CURLY_BRACKET))
            return parseObject(removeFirst(tokens));

        return newPair(first, removeFirst(tokens));
    }

    public static Object parseJson(List<JsonToken> tokens) throws Exception {
        Object result = parse(tokens);

        if (result == null)
            return null;

        return ((Pair) result).getKey();
    }

    public static Pair<List<Object>, List<JsonToken>> parseArray(List<JsonToken> tokens) throws Exception {
        ArrayList<Object> result = new ArrayList();

        JsonToken first = head(tokens);

        if (first.matchCharacter(JsonToken.JSON_RIGHT_BRACKET))
            return newPair(result, removeFirst(tokens));

        // parsing a, b, 1, {}, ..]
        while (true) {

            // parse each element (value can be leaf, an array or object)
            Pair<Object, List<JsonToken>> pair = (Pair<Object, List<JsonToken>>) parse(tokens);

            // add element in resulting-array
            result.add(pair.getKey());
            tokens = pair.getValue();

            // look for comma, continue parsing the rest
            JsonToken head = head(tokens);

            if (head.matchCharacter(JsonToken.JSON_RIGHT_BRACKET))
                return newPair(result, removeFirst(tokens));

            if (!head.matchCharacter(JsonToken.JSON_COMMA))
                throw new Exception(String.format("except comma after object in array, got: %s", head));

            tokens = removeFirst(tokens);
        }
    }

    public static Pair<Map<String, Object>, List<JsonToken>> parseObject(List<JsonToken> tokens) throws Exception {
        HashMap<String, Object> result = new HashMap<>();

        JsonToken first = head(tokens);

        if (first.matchCharacter(JsonToken.JSON_RIGHT_CURLY_BRACKET))
            return newPair(result, removeFirst(tokens));

        // parsing "hello": "world"} or {"hello": <any>}
        while (true) {
            // look for "hello"
            JsonToken jsonKey = head(tokens);

            if (!(jsonKey.value instanceof String))
                throw new Exception(String.format("expect json-key as string, got: %s", jsonKey));

            tokens = removeFirst(tokens);

            // look for a colon
            if (!head(tokens).matchCharacter(JsonToken.JSON_COLON))
                throw new Exception(String.format("expect colon, got: %s", tokens.get(0).value));

            // look for (parse) json pair value, could be <any>
            Pair<Object, List<JsonToken>> pair = (Pair<Object, List<JsonToken>>) parse(removeFirst(tokens));
            result.put(jsonKey.value.toString(), pair.getKey());

            // look for a comma, continue parsing the rest of key-value pairs
            tokens = pair.getValue();
            JsonToken head = head(tokens);

            if (head.matchCharacter(JsonToken.JSON_RIGHT_CURLY_BRACKET))
                return newPair(result, removeFirst(tokens));

            if (!head.matchCharacter(JsonToken.JSON_COMMA))
                throw new Exception(String.format("except comma after json pair, got: %s", head));

            tokens = removeFirst(tokens);
        }
    }

    static Pair<Map<String, Object>, List<JsonToken>> newPair(Map<String, Object> map, List<JsonToken> tokens) {
        return new Pair<>(map, tokens);
    }

    static Pair<List<Object>, List<JsonToken>> newPair(List<Object> map, List<JsonToken> tokens) {
        return new Pair<>(map, tokens);
    }

    static Pair<Object, List<JsonToken>> newPair(Object token, List<JsonToken> tokens) {
        return new Pair<>(token, tokens);
    }

    static List<JsonToken> removeFirst(List<JsonToken> tokens) {
        return Collections.unmodifiableList(tokens.subList(1, tokens.size()));
    }

    static JsonToken head(List<JsonToken> tokens) {
        return tokens.get(0);
    }
}
