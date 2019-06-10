package com.dvliman.sjson;

import javafx.util.Pair;
import java.util.*;

public class JsonParser {
    public static Pair<List<Object>, List<JsonToken>> parseArray(List<JsonToken> tokens) throws Exception {
        ArrayList<Object> result = new ArrayList();

        JsonToken first = head(tokens);
        if (first.value.equals(JsonToken.JSON_RIGHT_BRACKET)){
            return newPair(result, removeFirst(tokens));
        }

        while (true) {

            // parse for element in array
            Pair<Object, List<JsonToken>> pair = (Pair<Object, List<JsonToken>>) parse(tokens);

            // add element in resulting-array
            result.add(pair.getKey());
            tokens = pair.getValue();

            // look for comma (prepare to parse the next element in array)
            // if ], back out
            JsonToken head = head(tokens);

            if (head.value.equals(JsonToken.JSON_RIGHT_BRACKET)) {
                return newPair(result, removeFirst(tokens));

            } else if ((Character) head.value != JsonToken.JSON_COMMA) {
                throw new Exception(String.format("except comma after object in array, got: %s", head));

            } else {
                tokens = removeFirst(tokens);
            }
        }
    }

    public static Pair<Map<String, Object>, List<JsonToken>> parseObject(List<JsonToken> tokens) throws Exception {
        HashMap<String, Object> result = new HashMap<String, Object>();
        JsonToken first = head(tokens);

        if (first.value.equals(JsonToken.JSON_RIGHT_CURLY_BRACKET))
            return newPair(result, removeFirst(tokens));

        while (true) {
            // look for json pair key
            JsonToken jsonKey = head(tokens);

            if (!jsonKey.type.equals(String.class))
                throw new Exception(String.format("expect json-key as string, got: %s", jsonKey));

            tokens = removeFirst(tokens);

            // look for : after json pair key
            if ((Character) tokens.get(0).value != JsonToken.JSON_COLON) {
                throw new Exception(String.format("expect colon, got: %s", tokens.get(0).value));
            }

            // look for json pair value
            Pair<Object, List<JsonToken>> pair = (Pair<Object, List<JsonToken>>)
                parse(tokens.subList(1, tokens.size()));
            result.put(jsonKey.value.toString(), pair.getKey());

            // looking for } or ,
            tokens = pair.getValue();
            JsonToken head = tokens.get(0);
            if (head.value.equals(JsonToken.JSON_RIGHT_CURLY_BRACKET)){
                return newPair(result, tokens.subList(1, tokens.size()));

            } else if ((Character) head.value != JsonToken.JSON_COMMA) {
                throw new Exception(String.format("except comma after json pair, got: %s", head));
            }

            tokens = tokens.subList(1, tokens.size());
        }
    }

    public static Object parse(List<JsonToken> tokens) throws Exception {
         JsonToken first = head(tokens);

         if (first.value.equals(JsonToken.JSON_LEFT_BRACKET))
             return parseArray(removeFirst(tokens));

         if (first.value.equals(JsonToken.JSON_LEFT_CURLY_BRACKET))
            return parseObject(removeFirst(tokens));

         return newPair(first, removeFirst(tokens));
    }

    static Pair<Map<String, Object>, List<JsonToken>> newPair(Map<String, Object> map, List<JsonToken> tokens) {
        return new Pair<Map<String, Object>, List<JsonToken>>(map, tokens);
    }

    static Pair<List<Object>, List<JsonToken>> newPair(List<Object> map, List<JsonToken> tokens) {
        return new Pair<List<Object>, List<JsonToken>>(map, tokens);
    }

    static Pair<Object, List<JsonToken>> newPair(Object token, List<JsonToken> tokens) {
        return new Pair<Object, List<JsonToken>>(token, tokens);
    }

    static List<JsonToken> removeFirst(List<JsonToken> tokens) {
        return Collections.unmodifiableList(tokens.subList(1, tokens.size()));
    }

    static JsonToken head(List<JsonToken> tokens) {
        return tokens.get(0);
    }

    static void printTokens(List<JsonToken> tokens) {
        for (JsonToken t: tokens) {
            System.out.println(t);
        }
    }

    public static void main(String[] args) throws Exception {
//        String input = "{\"hello\": \"world\", \"args\": {\"vincent\": \"david\"}}";
        String input = "{\"hello\": \"world\", \"args\": [\"vincent\"]}";
//        String input = "{\"args\": [\"vincent\"]}";

//        String input = "[1, 2, 3]";
        List<JsonToken> tokens = JsonLexer.tokens(input);

        Object result = JsonParser.parse(tokens);

//        Map<String, Object> result = (Map<String, Object>) ((Pair<Object, Object>) JsonParser.parse(tokens)).getKey();
//        System.out.println(result.get("hello"));
//        System.out.println(result.get("args"));
        System.out.println("stop");
    }


}
