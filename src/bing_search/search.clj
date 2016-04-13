(ns bing-search.search
  (:require [clj-http.client :as http]
            [clojure.data.xml :as xml]
            [clojure.data.json :as json]))

(def bing-key "")

(defn set-key!
  [k]
  (def bing-key k))

(defn parse-opts
  "convert options to a uri query string fragment"
  [opts]
  (clojure.string/join (map (fn [[k v]]
         (let [k (name k)
               v (if (keyword? v) (name v) v)]
           (str "&" k "=" v))) opts)))


(defn encode-quotes
  "wraps argument in encoded single quotes"
  [arg]
  (str "%27" arg "%27"))

(defn parse-body [resp]
  (let [type ((resp :headers) "Content-Type")
        r-json (when (re-find #"/json" type)
                (((json/read-str (resp :body)) "d") "results"))
        r-xml (when (re-find #"xml" type)
                (xml/parse-str (resp :body)))
        result (or r-json r-xml)]
    (if-not result (resp :body) result)))

(defn search
  "https request to bing api, returns {:result <parsed body> :response <http response map>}"
  ([if term] (search if term {}))
  ([if term opts]
   (let [params (parse-opts opts)
         if (clojure.string/capitalize (name if))
         url (str "https://api.datamarket.azure.com/Bing/Search/" if "?Query=" (encode-quotes term) params)
         resp (http/get url
               {:digest-auth ["", bing-key]
                :throw-exceptions false})]
     (if-not (= 200 (resp :status))
       (throw (ex-info "Error" resp))
       {:result (parse-body resp) :response resp}))))
