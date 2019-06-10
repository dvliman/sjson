#### sjson: toy json parser in java

There are two stages in parsing json: 
  1. lexical analysis (breaking down input string into tokens)
      * recognize certain character such as: `{`, `}`, `[`, `]`, `,`
      * look go for json string (surrounded by `"`)
      * for example: 
        * input string: `{"hello": ["world"]}` 
        * consists of the following tokens: `{`, `hello`, `:`, `[`, `world`, `]`, `}`
        * note: token must be non-recursive
  2. syntactic analysis  
      * match groups of tokens according to the language grammar (see: [http://json.org/](http://json.org/))  
      * to parse a json (see: [parse](https://github.com/dvliman/sjson/blob/master/src/main/java/com/dvliman/sjson/JsonParser.java#L78))  
        * check the first token
        * if first token is `{`, parse for object  
        * if first token is `]`, parse for array 
        * otherwise return first token, and the remaining tokens  
      * to parse array (see: [parseObject](https://github.com/dvliman/sjson/blob/master/src/main/java/com/dvliman/sjson/JsonParser.java#L38)):
        * initialize resulting-array
        * call parse on each element
        * add to resulting-array
        * look for comma
        * repeat for remaining until it sees `]`
      * to parse object (see: [parseArray](https://github.com/dvliman/sjson/blob/master/src/main/java/com/dvliman/sjson/JsonParser.java#L7))
        * initialize resulting-map  
        * look for first token (the key of the pair)
        * look for `:` 
        * parse the value and set value on resulting-map
        * look for `,`
        * repeat for remaining key-value pairs until it sees `}`
    * note: many implementations do a single-pass 
      
* TODO:
  * handle unicode characters
  * handle escape characters
  * parse number with precision
  * use `Reader` or `PushBackReader` for streaming characters
  * define type container for `JsonArray`, `JsonObject`, `JsonValue`, primitives and so on
  
* links:
  * [stleary/JSON-java](https://github.com/stleary/JSON-java): sample implementation in java
  * [FasterXML/jackson](https://github.com/FasterXML/jackson): robust, supports many features
  * [ralfstx/minimal-json](https://github.com/ralfstx/minimal-json): very simple implementation with buffer and reader, as fast as Jackson with much less featuresets
  * [clojure/data.json](https://github.com/clojure/data.json): clojure implementation with `PushBackReader` allows you to 'un-see' character stream
  * [fangyidong/json-simple](https://github.com/fangyidong/json-simple): generates yylex using parse generator (JFlex)
  
