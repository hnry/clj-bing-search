(ns bing-search.search-test
  (:require [clojure.test :refer :all]
            [clojure.edn :as edn]
            [bing-search.search :refer :all]))

(deftest parse-opts-test
  (testing "converts map to a uri query"
    (is (= "&Adult=1&format=json&$top=1" (parse-opts {:Adult 1 :format :json :$top 1})))))

(deftest encode-quotes-test
  (testing "wraps arg in uri encode single quotes"
    (is (= "%27hi%27" (encode-quotes "hi"))))
  (testing "what about spaces? TODO"
    (is (= "%hi how are yoU%27") (encode-quotes "hi how are yoU"))))

(deftest parse-body-test
  (testing "parse-body"
    (testing "json"
      (let [data (edn/read-string (slurp "test/success-json.edn"))]
        (is (= (count (parse-body data))
               1))
        (is (= ((get (parse-body data) 0) "Title")
               "Cute\n   Kittens - Pictures - The Wondrous\n   Pics"))))))
    ;; (testing "xml"  TODO
    ;;   (let [data (edn/read-string (slurp "test/success-xml.edn"))]
    ;;     (is (= (parse-body data)
    ;;            {:a 1}))))
    ;; (testing "unknown")))

(deftest search-test
  (testing "TODO"))
