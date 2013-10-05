(ns calc.shell
  (:gen-class)
  (:use calc.core))

(defn -main
  "read-eval-print (no loop)"
  [& _args]
  (try
    (let [s (read-line)]
      (if-not (= s "")
        (println (calculate s))))
    (catch Exception _e (println "ERROR")))
    (System/exit 0))
