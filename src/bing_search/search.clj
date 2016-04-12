(ns bing-search.search
  (:require [clj-http.client :as http]
            [clojure.data.xml :as xml]
            [clojure.data.json :as json]))

(def bing-key "")

(defn set-key!
  [k]
  (def bing-key k))

(def params
  "Bing uri query parameters"
  {:format "&$format="
   :skip "&skip="
   :top "&$top="      ;; results to return, default 50 web,image,video, 15 new
   :adult "&Adult="}) ;; TODO there's only really 3 options, should restrict to the 3?

(defn parse-opts
  "helper function for parsing options to `search`"
  [opts]
  (clojure.string/join ""
                       (map (fn [[k, v]]
                              (let [k (params k)
                                    v (if (keyword? v) (name v) v)]
                                (if k (str k v)))) opts)))

(defn encode-quotes
  "wraps argument in encoded single quotes"
  [arg]
  (str "%27" arg "%27"))

(defn parse-json [])

(defn parse-xml [resp]
  (xml/parse-str (resp :body)))

(defn search
  "https request to bing api"
  ([term] (search term {}))
  ([term opts]
   (let [params (parse-opts opts)
         url (str "https://api.datamarket.azure.com/Bing/Search/Image?Query=" (encode-quotes term) params)]
     (http/get url
               {:digest-auth ["", bing-key]
                :throw-exceptions false}))))
