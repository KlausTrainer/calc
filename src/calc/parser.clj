(ns calc.parser
  "https://github.com/budu/sy-challenge")

(defmacro if-eof [true-f false-f]
  `(try
     ~false-f
     (catch Exception e#
       (if (= (.getMessage e#) "EOF while reading")
         ~true-f
         (throw e#)))))

(defprotocol parser-p
  (pop-info [this key])
  (push-info [this key val])
  (lower? [this op])
  (->sexp [this op])
  (shift-op [this op])
  (step [this])
  (parse* [this]))

(defrecord Parser [tokenizer out-stack op-stack ops]
  parser-p

  (pop-info [this key]
    [(peek (key this))
     (assoc this key (next (key this)))])

  (push-info [this key val]
    (assoc this key (conj (key this) val)))

  (lower? [this op]
    (<= (or (ops op) 0)
        (or (ops (peek op-stack)) 0)))

  (->sexp [this op]
    (let [[s parser] (pop-info this :out-stack)
          [f parser] (pop-info parser :out-stack)]
      (push-info parser :out-stack (list (resolve op) f s))))

  (shift-op [this op]
    (if (lower? this op)
      (let [[op2 parser] (pop-info this :op-stack)]
        (if op2
          (-> (->sexp parser op2)
              (shift-op op))
          parser))
      this))

  (step [this]
    (let [token (tokenizer)]
      (if (number? token)
        (push-info this :out-stack token)
        (-> this
            (shift-op token)
            (push-info :op-stack token)))))

  (parse* [this]
    (if-eof
      (->> (iterate #(shift-op % nil) this)
           (take (inc (count op-stack)))
           last
           :out-stack
           first)
      (parse* (step this)))))

(defn make-parser [tokenizer operators]
  (Parser. tokenizer '() '() operators))
