package com.dvliman.sjson;

public class JsonToken {
    public static char JSON_QUOTE = '"';
    public static char JSON_COMMA = ',';
    public static char JSON_COLON = ':';
    public static char JSON_LEFT_BRACKET        = '[';
    public static char JSON_RIGHT_BRACKET       = ']';
    public static char JSON_LEFT_CURLY_BRACKET  = '{';
    public static char JSON_RIGHT_CURLY_BRACKET = '}';

    // Class type; to keep track of value's type
    Object value;
    String remaining;

    public JsonToken(Object value, String remaining) {
        this.value     = value;
        this.remaining = remaining;
    }

    public boolean matchCharacter(char c) {
        return this.value.equals(c);
    }

    public String toString() {
        return this.value.toString();
//        return String.format("JsonToken(type = %s, value = %s, rest = %s)\n",
//            this.type.getName(),
//            this.value,
//            this.remaining);
    }
}
