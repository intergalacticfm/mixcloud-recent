(defproject ifmmxcl "0.1.0-SNAPSHOT"
  :description "get recent mixcloud mixes"
  :url "http://intergalacticfm.net"
  :dependencies [[com.taoensso/timbre            "4.3.1"]
                 [funcool/promesa                "1.1.1"]
                 [org.clojure/clojure            "1.8.0"]
                 [org.clojure/clojurescript      "1.8.34"]
                 [org.clojure/core.async         "0.2.374"]
                 [io.nervous/cljs-lambda         "0.3.0"]
                 [io.nervous/cljs-nodejs-externs "0.2.0"]
                 [io.nervous/eulalie             "0.6.8-SNAPSHOT"]
                 [io.nervous/glossop             "0.2.1"]
                 [io.nervous/kvlt                "0.1.1"]]
  :plugins [[lein-cljsbuild "1.1.3"]
            [lein-npm       "0.6.0"]
            [lein-doo       "0.1.7-SNAPSHOT"]
            [io.nervous/lein-cljs-lambda "0.5.1"]]
  :npm {:dependencies [[source-map-support "0.4.0"]
                       [aws-sdk "2.2.39"]]}
  :source-paths ["src"]
  :cljs-lambda
  {:defaults {:role "arn:aws:iam::999371153281:role/cljs-lambda-default"
              :timeout 30
              :memory-size 256}
   :resource-dirs ["static"]
   :functions
   [{:name "get-mixes"
     :invoke ifmmxcl.core/get-mixes}]}
  :cljsbuild
  {:builds [{:id "ifmmxcl"
             :source-paths ["src"]
             :compiler {:output-to     "target/ifmmxcl/ifmmxcl.js"
                        :output-dir    "target/ifmmxcl"
                        :target        :nodejs
                        :language-in   :ecmascript5
                        :optimizations :none}}
            {:id "ifmmxcl-test"
             :source-paths ["src" "test"]
             :compiler {:output-to     "target/ifmmxcl-test/ifmmxcl.js"
                        :output-dir    "target/ifmmxcl-test"
                        :target        :nodejs
                        :language-in   :ecmascript5
                        :optimizations :none
                        :main          ifmmxcl.test-runner}}]})
