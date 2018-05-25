(defproject ifmmxcl "0.1.0-SNAPSHOT"
  :description "get recent mixcloud mixes"
  :url "http://intergalacticfm.net"
  :dependencies [[com.taoensso/timbre            "4.7.4"]
                 [org.clojure/clojure            "1.8.0"]
                 [org.clojure/clojurescript      "1.8.51"]
                 [org.clojure/core.async         "0.2.395"]
                 [io.nervous/cljs-lambda         "0.3.5"]
;;                 [io.nervous/cljs-nodejs-externs "0.2.0"]
                 [io.nervous/eulalie             "0.6.10"]
                 [io.nervous/glossop             "0.2.1"]
                 [io.nervous/kvlt                "0.1.4"]]
  :plugins [[lein-cljsbuild              "1.1.2"]
            [lein-npm                    "0.6.2"]
            [lein-doo                    "0.1.7"]
            [io.nervous/lein-cljs-lambda "0.6.6"]]
  :npm {:dependencies [[source-map-support "0.4.0"]
                       [aws-sdk "2.3.15"]]}
  :jvm-opts ["--add-modules" "java.xml.bind"]
  :source-paths ["src"]
  :cljs-lambda
  {:defaults {:role "arn:aws:iam::999371153281:role/cljs-lambda-default"
              :runtime "nodejs6.10"
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
