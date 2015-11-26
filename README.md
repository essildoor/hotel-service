# hotel-service

to build with maven use 'mvn repackage'

to run app 'java -jar hotel-service-1.0.jar'

app binds to localhost:8080/hotels resource

query parameters:

city (string) - get hotels in specified city (required)

sortByPrice (true/false) - sort by price (optional)

asc (true/false) - sorting order (optional)

each api call should contain header with api key:

"api-key" : "wow_such_api_key_1000_much_security_oO", where 1000 is call rate limit in ms, so in this case rate limit is 1 call per second

configurable parameters:

in the ratelimit.properties you can specify:

hotel db file name and extension;
minimum delay between api calls in milliseconds;
postpone delay for those who exceeded rate limit, also in milliseconds;
http header name for api key;
