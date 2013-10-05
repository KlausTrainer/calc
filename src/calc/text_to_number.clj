(ns calc.text-to-number
  (:use [clojure.string :only [split]]))

(defn text-to-number
  "Converts numbers written as words to their numeric representation.
  Based on http://git.hewgill.com/gitweb?p=text2num.git;a=summary.
  See also this discussion on Stack Overflow:
  http://stackoverflow.com/questions/70161/how-to-read-values-from-numbers-written-as-words"
  [text]
  (let [small
        {"and" 0
         "zero" 0
         "one" 1
         "two" 2
         "three" 3
         "four" 4
         "five" 5
         "six" 6
         "seven" 7
         "eight" 8
         "nine" 9
         "ten" 10
         "eleven" 11
         "twelve" 12
         "thirteen" 13
         "fourteen" 14
         "fifteen" 15
         "sixteen" 16
         "seventeen" 17
         "eighteen" 18
         "nineteen" 19
         "twenty" 20
         "thirty" 30
         "forty" 40
         "fifty" 50
         "sixty" 60
         "seventy" 70
         "eighty" 80
         "ninety" 90}
        magnitude
        {"thousand" 1000
         "million" 1000000
         "billion" 1000000000
         "trillion" 1000000000000}
        words
        (split text #"[\s-]+")
        sign
        (if (= (get words 0) "minus")
          -1
          1)
        i
        (if (= sign -1)
          1
          0)]
    (loop [i i n 0 g 0]
      (if-let [word (get words i)]
        (if-let [s (get small word)]
          (recur (inc i) n (+ g s))
          (if (= word "hundred")
            (recur (inc i) n (* g 100))
            (if-let [m (get magnitude word)]
              (recur (inc i) (+ n (* g m)) 0)
              (throw (Exception. (str "unknown number: " word))))))
        (* sign (+ n g))))))
