(ns candelabrum.core
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(defn- normalize [s]
  (str/trim s))

(defn- maybe-int [s]
  (when s (Integer/parseInt s)))

(def ^:private date-format
  (let [tz (java.util.TimeZone/getTimeZone "UTC")]
    (doto
     (java.text.SimpleDateFormat. "EEEE, MMMM d, yyyy h:m:s a")
      (.setTimeZone tz))))

(defn- delimiter? [s]
  (= "==========" s))

(defn- raw-entries [reader]
  (->> (line-seq reader)
       (map normalize)
       (partition-by delimiter?)
       (remove #(delimiter? (first %)))))

(defn- parse-type [meta]
  (let [re #"- Your (\w+) "]
    (-> (re-find re meta)
        (get 1 "unknown")
        str/lower-case
        keyword)))

(defn- parse-location [meta]
  (let [[_ type start end] (re-find #"(Location|page) (\d+)(?:-(\d+))?" meta)]
    {:type (keyword (str/lower-case type))
     :start (maybe-int start)
     :end (maybe-int end)}))

(defn- parse-timestamp [meta]
  (let [date (get (str/split meta #"Added on ") 1)]
    (try
      (.parse date-format date)
      (catch Exception e
        nil))))

(defn- parse-meta [meta]
  {:type (parse-type meta)
   :added (parse-timestamp meta)
   :location (parse-location meta)})

(defn- parse-book [book]
  (if-let [[_ title author] (re-find #"(.+)\((.+)\)$" book)]
    {:title (normalize title)
     :author (normalize author)}
    {:title (normalize book)}))

(defn- parse-entry [e]
  (let [[book meta _ text] e]
    (merge (parse-meta meta)
           {:book (parse-book book)
            :text (normalize text)})))

(defn parse [reader]
  (->> (raw-entries reader)
       (map parse-entry)))
