(ns ifmmxcl.core
  (:require [cljs-lambda.util :as lambda]
            [cljs-lambda.context :as ctx]
            [cljs-lambda.macros :refer-macros [deflambda]]
            [cljs.reader :refer [read-string]]
            [cljs.nodejs :as nodejs]
            [cljs.core.async :as async :refer [<!]]
            [eulalie.creds :as creds]
            [glossop.util :refer [close-with!]]
            [kvlt.core :as kvlt]
            [kvlt.chan :refer [request!]])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(def creds (creds/env))

(def aws (nodejs/require "aws-sdk"))
(def S3 (.. aws -S3))

(def config
  (-> (nodejs/require "fs")
      (.readFileSync "static/config.edn" "UTF-8")
      read-string))

(def path (str "/cloudcasts/?limit="  (:max-per-req config)))

(defn get-mixes-for-response [body]
  (let [{:keys [data name]} (js->clj (.parse js/JSON (:body body)) :keywordize-keys true)]
    (map #(select-keys % [:created_time :name :url :user]) data)))

(defn put-s3-object! [bucket key body]
  (println (str "put-s3-object! bucket=" bucket ", key=" key))
  (let [ch (async/chan)
        s3-params (clj->js {:signatureVersion "v4"})
        s3 (S3. s3-params)
        params (clj->js {:Bucket bucket :Key key :Body body :ACL "public-read"})]
    (.putObject s3 params #(close-with! ch (or %1 %2)))
    ch))

(deflambda get-mixes [{} context]
  (go
    (let [urls (map #(str (:url config) "/" % path) (:mixers config))
          requests (map #(request! {:url %}) urls)
          responses (<! (async/into [] (async/merge requests)))
          mixes (flatten (map #(get-mixes-for-response %) responses))
          recent
          (.stringify js/JSON (clj->js (take (:total-results config) (reverse (sort-by :created_time mixes)))))
          s3-response (<! (put-s3-object! (:s3-bucket config) (:s3-key config) recent))]
      (println "s3-response" s3-response))))
