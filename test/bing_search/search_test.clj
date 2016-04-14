(ns bing-search.search-test
  (:require [clojure.test :refer :all]
            [clojure.edn :as edn]
            [bing-search.search :refer :all]))

(deftest parse-opts-test
  (testing "converts map to a uri query"
    (is (= "&Adult=1&format=json&$top=1" (parse-opts {:Adult 1 :format :json :$top 1})))))

(deftest encode-quotes-test
  (testing "wraps arg in uri encode single quotes"
    (is (= "%27hihey%27" (encode-quotes {:term "hihey"}))))
  (testing "automatically encodes"
    (is (= "%27hi+how+are+yoU%27") (encode-quotes {:term "hi how are yoU"})))
  (testing "turn off encoding"
    (is (= "%27hi how are & you%27" (encode-quotes {:term "hi how are & you" :encode false})))))

(deftest parse-body-test
  (testing "parse-body"
    (testing "nil"
      (is (= nil (parse-body {}))))
    (testing "json"
      (let [data (edn/read-string (slurp "test/success-json.edn"))]
        (is (= (count (parse-body data))
               1))
        (is (= ((get (parse-body data) 0) "Title")
               "Cute\n   Kittens - Pictures - The Wondrous\n   Pics"))))))

(deftest search-test
  (testing "TODO"))
