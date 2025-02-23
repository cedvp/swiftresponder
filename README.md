This project is about responding to swift messages. To not depend on external systems for testing.
We start with MT540, MT541, MT542, MT543 and their MT548 and other settlement responses (MT544, MT545, MT546, MT547)

It can be called as:

SwiftResponder.getSwiftResponse(input).get(typeresponse)

where input is a String with the all input message (MT540, MT541, MT542 or MT543)
and the typeresponse is "ack", "match" or "settle".
Logically you should ask them sequentially. First ack, then match, then settle. 
But any answer will be provided when requested. There is no sequence logic built.

code-bike-hike..
..
