package com.dvliman.sjson;

public class JsonToken {
    public static char JSON_QUOTE = '"';
    public static char JSON_COMMA = ',';
    public static char JSON_COLON = ':';
    public static char JSON_LEFT_BRACKET        = '[';
    public static char JSON_RIGHT_BRACKET       = ']';
    public static char JSON_LEFT_CURLY_BRACKET  = '{';
    public static char JSON_RIGHT_CURLY_BRACKET = '}';

    Class type;
    Object value;
    String remaining;

    public JsonToken(Class type, Object value, String remaining) {
        this.type      = type;
        this.value     = value;
        this.remaining = remaining;
    }

    public String toString() {
        return this.value.toString();
//        return String.format("JsonToken(type = %s, value = %s, rest = %s)\n",
//            this.type.getName(),
//            this.value,
//            this.remaining);
    }
}
