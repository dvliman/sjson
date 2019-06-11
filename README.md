#### sjson: toy json parser in java

There are two stages in parsing json: 
  1. lexical analysis (breaking down input string into tokens)
      * recognize certain character such as: `{`, `}`, `[`, `]`, `,`
      * look for json string (surrounded by `"`)
      * for example: 
        * input string: `{"hello": ["world"]}` 
        * consists of the following tokens: `{`, `hello`, `:`, `[`, `world`, `]`, `}`
        * note: token must be non-recursive
  2. syntactic analysis  
      * match groups of tokens according to the language grammar (see: [http://json.org/](http://json.org/))  
      * to parse a json (see: [parse](https://github.com/dvliman/sjson/blob/master/src/main/java/com/dvliman/sjson/JsonParser.java#L78))  
        * check the first token
        * if first token is `{`, parse for object  
        * if first token is `[`, parse for array 
        * otherwise return first token, and the remaining tokens  
      * to parse array (see: [parseArray](https://github.com/dvliman/sjson/blob/master/src/main/java/com/dvliman/sjson/JsonParser.java#L7))
        * initialize resulting-array
        * call parse on each element
        * add to resulting-array
        * look for comma
        * repeat for remaining until it sees `]`
      * to parse object (see: [parseObject](https://github.com/dvliman/sjson/blob/master/src/main/java/com/dvliman/sjson/JsonParser.java#L38)):
        * initialize resulting-map  
        * look for first token (the key of the pair)
        * look for `:` 
        * parse the value and set value on resulting-map
        * look for `,`
        * repeat for remaining key-value pairs until it sees `}`
    * note: many implementations do a single-pass 

How to use this library:
```
import com.dvliman.sjson.JsonLexer;
import com.dvliman.sjson.JsonParser;

String json = "{\"hello\": \"world\"}";

Object result = JsonParser.parseJson(JsonLexer.tokens(json));
HashMap<String, Object> map = (HashMap) result;
System.out.println(result.get("hello")); // => "world"
        
```      

See more examples on the test case:
1. testLexer: parse tokens from input string
2. testEmptyJson: parse empty json
3. testJsonArray: parse top level json array
4. testJsonObject: parse top level json object
5. testJsonObjectArray: parse json array values
6. testNestedJson: parse nested json object/array
7. testInvalidJson: expect colon in json pair
8. testInvalidJsonObjectKey: expect json field to be a string
note: run `mvn test` to run all the tests

* TODO:
  * handle unicode characters
  * handle escape characters
  * parse number with precision
  * use `Reader` or `PushBackReader` for streaming characters
  * define type container for `JsonArray`, `JsonObject`, `JsonValue`, primitives and so on
  * handle edge cases (see: [Parsing JSON is a minefield](http://seriot.ch/parsing_json.php))  
  * handle top-level scalars - RFC 7158
  
* links:
  * [stleary/JSON-java](https://github.com/stleary/JSON-java): sample implementation in java
  * [FasterXML/jackson](https://github.com/FasterXML/jackson): robust, supports many features
  * [ralfstx/minimal-json](https://github.com/ralfstx/minimal-json): very simple implementation with buffer and reader, as fast as Jackson with much less featuresets
  * [clojure/data.json](https://github.com/clojure/data.json): clojure implementation with `PushBackReader` allows you to 'un-see' character stream
  * [fangyidong/json-simple](https://github.com/fangyidong/json-simple): generates yylex using parse generator (JFlex)
  
