(ns pencil-project.helper.items-test
  (:require [clojure.test :refer :all])
  (:require [pencil-project.helper.items :as items]))

(deftest parse-item-test
  (testing "parse items"
    (is (= {"Cookie" 1, "Brownie" 4, "Cheesecake" 1}
           (items/get-cart-items "Cookie, Brownie x 4, Cheesecake")))
    (is (= {"Cookie" 8}
           (items/get-cart-items "Cookie x 8")))
    (is (= {"Cookie" 1, "Brownie" 4, "Cheesecake" 1}
           (items/get-cart-items "Cookie, Brownie x 4, Cheesecake")))
    (is (= {"Cookie" 1, "Brownie" 1, "Cheesecake" 1, "Donut" 2}
           (items/get-cart-items "Cookie, Brownie, Cheesecake, Donut x 2")))
    (is (= {"Cookie" 8, "Cheesecake" 4}
           (items/get-cart-items "Cookie x 8, Cheesecake x 4")))
    (is (= {"Cookie" 5, "Brownie" 4, "Cheesecake" 1}
           (items/get-cart-items "Cookie x 3, Brownie x 4, Cheesecake, Cookie x 2")))))

