(ns mibig-pics.core
  (:require [cheshire.core :refer [parse-stream]]
            [rhizome.viz :as viz]
            [com.walmartlabs.datascope :as ds]))

(defn isjson? [f]
  "check to see if a file is json"
  (.endsWith (.getName f) ".json"))

(defn save
  [root-object file-name]
  (-> (ds/dot root-object)
      viz/dot->image
      (viz/save-image file-name)))

(defn process-mibig
  [jsonfile]
  (let [r (clojure.java.io/reader jsonfile)
        edn (parse-stream r)
        bgc (-> jsonfile .getName (.substring 0 10))
        outfile (str "images/" bgc ".png")]
    (save edn outfile)))

(def jsonfiles
  (->>
    (clojure.java.io/file "data")
    (file-seq)
    (filter isjson?)
    (filter #(> (.length %) 0))))

(def all_mibig (into [] (map #(parse-stream (clojure.java.io/reader %) true) jsonfiles)))

(def -main (doall (map process-mibig jsonfiles)))

