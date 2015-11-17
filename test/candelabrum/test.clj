(ns candelabrum.test
  (:require [clojure.test :refer [deftest is]]
            [clojure.java.io :as io]
            [candelabrum.core :refer [parse]]))

(def clippings
  "Structure and Interpretation of Computer Programs, Second Edition (Harold Abelson, Gerald Jay Sussman, Julie Sussman)
   - Your Highlight on Location 299-301 | Added on Tuesday, October 20, 2015 11:29:10 AM

   It is better to have 100 functions operate on one data structure than to have 10 functions operate on 10 data structures.
   ==========
   sicp
   - Your Highlight on page 114 | Added on Tuesday, October 27, 2015 12:41:17 PM

   We are using here a powerful strategy of synthesis: wishful thinking
   ==========")

(def parsed-clippings
  [{:book {:title "Structure and Interpretation of Computer Programs, Second Edition"
           :author "Harold Abelson, Gerald Jay Sussman, Julie Sussman"}
    :type :highlight
    :added #inst "2015-10-20T11:29:10.000-00:00"
    :text "It is better to have 100 functions operate on one data structure than to have 10 functions operate on 10 data structures."
    :location {:type :location
               :start 299
               :end 301}}
   {:book {:title "sicp"}
    :type :highlight
    :added #inst "2015-10-27T12:41:17"
    :text "We are using here a powerful strategy of synthesis: wishful thinking"
    :location {:type :page
               :start 114
               :end nil}}])

(defn string-reader [s]
  (java.io.BufferedReader.
   (java.io.StringReader. s)))

(defn resource-reader [path]
  (io/reader (.getFile (io/resource path))))

(deftest test-parse
  (let [parsed (parse (string-reader clippings))]
    (is (= 2 (count parsed)))
    (is (= parsed-clippings parsed))))

(deftest test-with-file
  (with-open [file (resource-reader "test-clippings.txt")]
    (let [parsed (parse file)]
      (is (= 226 (count parsed))))))
