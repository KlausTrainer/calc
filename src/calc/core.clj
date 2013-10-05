(ns calc.core
  (:require
    [calc.text-to-number :as text-to-number]
    [calc.parser :as parser]
    [clojure.string :as string]))

(def ^:private operators {'+ 10 '- 10 '* 20 '/ 20})

(def ^:private operator #"[\+\-\*\/]")
(def ^:private non-operator #"[^\+\-\*\/\s]")
(def ^:private integer #"\-?\d+")

(defn ^:private replace-non-operator-minus-signs [s]
  "Replace each minus sign that is no operator with a space, e.g.
  twenty-one -> twenty one."
  (string/replace s #"([a-z])\-([a-z])" "$1 $2"))

(defn ^:private normalize-operators [s]
  "Normalizes all operators so that they are represented as their
  respective operator symbol."
  (-> s (string/replace #"\s+plus\s+" " + ")
        (string/replace #"\s+times\s+" " * ")
        (string/replace #"\s+divided\s+by\s+" " / ")
        (string/replace ; add some space around operators (except for minus)
          #"([^\s])([\+\*\/])([^\s])" "$1 $2 $3")
        (string/replace ; binary textual minus
          (re-pattern (str "(" non-operator ")" "\\s+minus\\s+")) "$1 - ")
        (string/replace ; unary textual minus followed by numerical integer
          #"(^|\s+)minus\s+(\d+)" " -$2")))

(defn ^:private normalize-numbers [s]
  "Normalizes all numbers so that they are represented as their
  respective numeric symbol."
  (let [numbers (map string/trim (string/split s operator))]
    (reduce
      (fn [s text]
        (if (re-matches #"[a-z ]+" text)
          (string/replace-first s text (text-to-number/text-to-number text))
          s))
      s
      numbers)))

(defn ^:private tokenizer [s]
  (let [tokens
        (atom
          (reverse
            (reduce
              (fn [acc token]
                (cons
                  (cond
                    (re-matches operator token) (symbol token)
                    (re-matches integer token) (Long/parseLong token)
                    :else (throw (Exception. (str "invalid token: " token))))
                  acc))
              '()
              (string/split (string/trim s) #"\s+"))))]
    (fn []
      (if-let [token (first @tokens)]
        (do (swap! tokens rest) token)
        (throw (Exception. (str "EOF while reading")))))))

(defn ^:private evaluate [expr]
  (let [result
        (if (list? expr)
          (let [[op arg1 arg2] expr]
            (apply op (list (evaluate arg1) (evaluate arg2))))
          expr)]
    (if (or (< result -9999999) (> result 9999999))
      (throw (Exception. (str "number out of range: " result)))
      result)))

(defn calculate
  "Let's calculate!"
  [s]
  (-> s string/lower-case
        replace-non-operator-minus-signs
        normalize-operators
        normalize-numbers
        tokenizer
        (parser/make-parser operators)
        parser/parse*
        evaluate
        int))
