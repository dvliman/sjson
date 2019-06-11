package com.dvliman.sjson;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;
import java.util.stream.Collectors;

public class JsonTest {

    @Test
    public void testLexer() {
        HashMap<String, List<String>> tests = new HashMap<String, List<String>>() {{
            put("",   Arrays.asList());
            put("{}", Arrays.asList("{", "}"));
            put("[]", Arrays.asList("[", "]"));
            put("[1]", Arrays.asList("[", "1", "]"));
            put("{\"foo\"}", Arrays.asList("{", "foo", "}"));       // note: not valid json
            put("{\"foo\":}", Arrays.asList("{", "foo", ":", "}")); // note: not valid json
            put("{\"foo\": \"bar\"}", Arrays.asList("{", "foo", ":", "bar", "}"));
        }};

        tests.forEach((testCase, expected) ->
            assertEquals(expected, tokenStrings(testCase)));
    }

    @Test
    public void testEmptyJson() throws Exception {
        String json1 = "";
        assertNull(JsonParser.parse(JsonLexer.tokens(json1)));

        String json2 = "{}";
        Object result2 = JsonParser.parseJson(JsonLexer.tokens(json2));
        assertTrue(result2 instanceof Map);
        assertTrue(((HashMap) result2).size() == 0);

        String json3 = "[]";
        Object result3 = JsonParser.parseJson(JsonLexer.tokens(json3));
        assertTrue(result3 instanceof List);
        assertTrue(((ArrayList) result3).size() == 0);
    }

    @Test
    public void testJsonArray() throws Exception {
        String json = "[1, 2, 3]";
        ArrayList<JsonToken> result = (ArrayList) JsonParser.parseJson(JsonLexer.tokens(json));

        assertEquals(Arrays.asList(1, 2, 3),
            result.stream()
                .map(t -> Integer.parseInt(t.value.toString()))
                .collect(Collectors.toList()));
    }

    @Test
    public void testJsonObject() throws Exception {
        String json = "{\"hello\": \"world\"}";

        HashMap<String, Object> result = (HashMap) JsonParser.parseJson(JsonLexer.tokens(json));
        assertEquals(result.get("hello").toString(), "world");
    }

    // TODO: need better type to store json array & json object
    @Test
    public void testJsonObjectArray() throws Exception {
        String json = "{\"names\": [\"david\", \"liman\"]}";

        HashMap<String, Object> result = (HashMap) JsonParser.parseJson(JsonLexer.tokens(json));
        assertEquals(((ArrayList) result.get("names")).get(0).toString(), "david");
        assertEquals(((ArrayList) result.get("names")).get(1).toString(), "liman");
    }

    @Test
    public void testNestedJson() throws Exception {
        String json = "{\"hello\": {\"winners\": [\"david\", \"liman\"]}}";

        HashMap<String, Object> result = (HashMap) JsonParser.parseJson(JsonLexer.tokens(json));
        ArrayList<JsonToken> winners = (ArrayList) ((HashMap) result.get("hello")).get("winners");
        assertEquals(winners.get(0).toString(), "david");
        assertEquals(winners.get(1).toString(), "liman");
    }

    @Test(expected = Exception.class)
    public void testInvalidJson() throws Exception {
        String json = "{\"hello\" \"world\"}"; // expecting colon
        JsonParser.parseJson(JsonLexer.tokens(json));
    }

    @Test(expected = Exception.class)
    public void testInvalidJsonObjectKey() throws Exception {
        String json = "{1 :\"world\"}"; // json object field must be string
        JsonParser.parseJson(JsonLexer.tokens(json));
    }

    static List<String> tokenStrings(String input)  {
        try {
            return JsonLexer.tokens(input)
                .stream()
                .map(t -> t.value.toString())
                .collect(Collectors.toList());

        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
