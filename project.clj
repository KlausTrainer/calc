(defproject calc "0.1.0"
  :dependencies [[org.clojure/clojure "1.5.1"]]
  :main calc.shell
  :repl-options {:init-ns calc.core}
  :profiles {:uberjar {:aot :all}})
