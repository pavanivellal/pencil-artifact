(ns pencil-artifact.helper.items-test
  (:require [clojure.test :refer :all])
  (:require [pencil-artifact.helper.items :as items]
            [pencil-artifact.data :as data]))

(data/init)

(deftest parse-item-test
  (testing "parse items"
    (is (= '({:id 3, :qty 1, :name "cookie"} {:id 1, :qty 4, :name "brownie"} {:id 2, :qty 1, :name "cheesecake"})
           (items/get-cart-items "Cookie, Brownie x 4, Cheesecake")))
    (is (= '({:id 3, :qty 8, :name "cookie"})
           (items/get-cart-items "Cookie x 8")))
    (is (= '({:id 3, :qty 1, :name "cookie"} {:id 1, :qty 4, :name "brownie"} {:id 2, :qty 1, :name "cheesecake"})
           (items/get-cart-items "Cookie, Brownie x 4, Cheesecake")))
    (is (= '({:id 3, :qty 1, :name "cookie"}
             {:id 1, :qty 1, :name "brownie"}
             {:id 2, :qty 1, :name "cheesecake"}
             {:id 4, :qty 2, :name "donut"})
           (items/get-cart-items "Cookie, Brownie, Cheesecake, Donut x 2")))
    (is (= '({:id 3, :qty 8, :name "cookie"} {:id 2, :qty 4, :name "cheesecake"})
           (items/get-cart-items "Cookie x 8, Cheesecake x 4")))))

