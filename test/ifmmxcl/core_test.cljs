(ns ifmmxcl.core-test
  (:require [ifmmxcl.core :refer [config get-mixes]]
            [cljs.test :refer-macros [deftest is]]
            [cljs-lambda.local :refer [invoke channel]]
            [cljs.core.async :refer [<!]])
  (:require-macros [cljs.core.async.macros :refer [go]]))



(deftest get-mixes!
  (cljs.test/async
   done
   (go
     (let [mixes (second (<! (channel get-mixes)))]
       (doseq [m mixes]
         (let [user (get-in m [:user :name])]
           (println user))))
     (done))))
