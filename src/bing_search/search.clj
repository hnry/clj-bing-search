(ns bing-search.search
  (:require [clj-http.client :as http]
            [clj-http.util :refer [url-encode]]
            [clojure.data.xml :as xml]
            [clojure.data.json :as json]))

(def bing-key "")

(defn set-key!
  [k]
  (def bing-key k))

(def not-nil? (complement nil?))

(defn parse-opts
  "convert options to a uri query string fragment"
  [opts]
  (clojure.string/join (map (fn [[k v]]
         (let [k (name k)
               v (if (keyword? v) (name v) v)]
           (str "&" k "=" v))) opts)))


(defn encode-quotes
  "wraps argument in encoded single quotes and uri encodes it"
  [{term :term encode :encode}]
  (let [term (if (= encode false) term (url-encode term))]
    (str "%27" term "%27")))

(defn parse-body [resp]
  (let [hdr (resp :headers)
        type (when (not-nil? hdr) (hdr "Content-Type"))
        r-json (when (and (not-nil? type) (re-find #"json" type))
                (((json/read-str (resp :body)) "d") "results"))
        r-xml (when (and (not-nil? type)(re-find #"xml" type))
                (xml/parse-str (resp :body)))]
        (or r-json r-xml)))

(defn search
  "https request to bing api, returns {:result <parsed body> :response <http response map>}"
  ([inf term] (search inf term {}))
  ([inf term opts]
   (let [params (parse-opts (dissoc opts :encode))
         term (encode-quotes {:term term :encode (opts :encode)})
         inf (clojure.string/capitalize (name inf))
         url (str "https://api.datamarket.azure.com/Bing/Search/" inf "?Query=" term params)
         resp (http/get url
               {:digest-auth ["", bing-key]
                :throw-exceptions false})]
       {:result (parse-body resp) :response resp})))
