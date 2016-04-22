(ns ifmmxcl.test-runner
 (:require [doo.runner :refer-macros [doo-tests]]
           [ifmmxcl.core-test]
           [cljs.nodejs :as nodejs]))

(try
  (.install (nodejs/require "source-map-support"))
  (catch :default _))

(doo-tests
 'ifmmxcl.core-test)
