# clojure bing search

it's basically a wrapper around the https request that automates somethings like parsing the results and setting up the URI 

## usage

#### setting api key
Bing search api requires a api key to use, so set it or else the search is not usuable 
   
```clojure
    (bing-search/set-key! "...")
```

Afterwards you can do the following:

```clojure
    ;; search takes a search category (as defined by Bing) 
    ;; and a query
    (bing-search/search :Image "kittens")
 
    ;; optionally pass in query parameters
    ;; note: parameters are keywords named as Bing names them
    (bing-search/search :Image "kittens" {:$format :json $skip: 1})

    ;; all query strings are URI encoded
    ;; so this is ok
    (bing-search/search :Image "cute kittens playing")
    
    ;; if you already have a encoded string
    (bing-search/search :Image "cute+kittens+playing" {:encode false})
```

And the output is a map:

```clojure
    {:result <parsed json/xml> :response <map of original https response>}
```

note: i never use xml, so not sure how great the parsed result is (let me know!)

