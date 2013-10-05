(ns calc.core-test
  (:require [clojure.test :refer :all]
            [calc.core :refer :all]))

(deftest calculate-test
  (testing "calculate"
    (is (= 2 (calculate "8/3")))
    (is (= 9 (calculate "3*3")))
    (is (= 9 (calculate "7+2")))
    (is (= -9 (calculate "-9")))
    (is (thrown? Exception (calculate "+9")))
    (is (thrown? Exception (calculate "7-2")))
    (is (thrown? Exception (calculate "3 divided by 0")))
    (is (= 1 (calculate "  minus forty-two minus minus forty three minus 2 plus 3 times 2 - minus 1 minus 5  ")))
    (is (= 9 (calculate "minus thirty-three-hundred / 3300 * minus four minus -5 minus 2 plus 3 times 2 - minus 1 minus 5  ")))
    (is (= 12 (calculate "four minus -5 minus 2 plus 3 times 2 - 1")))
    (is (= 12 (calculate "  four - -5 minus 2 plus 3 times 2 plus minus 1 divided by 1 * one")))
    (is (= 42 (calculate "twenty one times 2")))
    (is (= 42 (calculate "forty two - minus 5 minus 2 plus 3 times 2 plus -9")))
    (is (= 42 (calculate "  42 - minus 5 minus 2 plus three times 2 - nine  ")))
    (is (= 42 (calculate "42 minus minus 5 minus 2 plus 3 times 2 - 9  ")))
    (is (= 42 (calculate "42")))
    (is (= 9999999 (calculate "3 - 7 + 4 + 9999999")))
    (is (thrown? Exception (calculate "9999999 + 3 - 7 + 4")))
    (is (= -9999999 (calculate "-3 + 7 - 4 - 9999999")))
    (is (thrown? Exception (calculate "-9999999 - 3 + 7 - 4")))
    (is (thrown? Exception (calculate "10000000")))
    (is (thrown? Exception (calculate "-10000000")))
    (is (= 9958539 (calculate "minus 5 plus seventy four times minus six minus three plus four - forty-two minus minus one plus 5 - 8 divided by 2 plus nine million and nine hundred fifty nine thousand twenty seven")))))
