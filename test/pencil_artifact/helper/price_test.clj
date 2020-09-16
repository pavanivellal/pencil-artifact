(ns pencil-artifact.helper.price-test
  (:require [clojure.test :refer :all])
  (:require [pencil-artifact.helper.price :refer [calculate-cart-total]]))

(deftest calculate-cart-total-test
  (testing "Test 1 : with special offers"
  ;; Test 1 -------------------
  ;On 10/1/2021, add these items: 8 Cookies, 4 Cheesecakes. Verify the price is $30.
  ;
  ;-> 8 cookies (item code 3) =  6
  ;-> 4 cheesecakes (item code 2) = 24
  ;TOTAL = 30

  ;; With special offers))
    (= 30.00
       (calculate-cart-total [{:id 3
                               :offer-date "2021-10-01"
                               :qty 8}
                              {:id 2
                               :offer-date "2021-10-01"
                               :qty 4}])))

  (testing "Test 2 : without special offers"
    ;; Test 2 -------------------
    ;Cookie, Brownie x 4, Cheesecake. Verify the total is $16.25.
    ;
    ;-> 1 Cookie (item code 3) = 1.25
    ;-> 4 Brownie (item code 1) = 7
    ;-> 1 Cheesecake (item code 2) = 8
    ;TOTAL = 16.25

    ;; Without special offers
    (= 16.25
       (calculate-cart-total [{:id 3
                               :qty 1}
                              {:id 1
                               :qty 4}
                              {:id 2
                               :qty 1}])))

  (testing "Test 3 : without special offers"
    ;; Test 2 -------------------
    ;Cookie x 8. Verify the total is $8.50
    ;
    ;-> 8 Cookie (item code 3) = 8.5
    ;TOTAL = 8.5

    ;; Without special offers
    (= 8.5
       (calculate-cart-total [{:id 3
                               :qty 8}])))


  (testing "Test 4 : without special offers"
    ;; Test 4 -------------------
    ;Cookie, Brownie, Cheesecake, Donut x 2. Verify the price is $12.25.
    ;
    ;-> 1 Cookie (item code 3) = 1.25
    ;-> 1 Brownie (item code 1) = 2
    ;-> 1 Cheesecake (item code 2) = 8
    ;-> 2 Donut (item code 4) = 1
    ;TOTAL = 12.25

    ;; Without special offers
    (= 12.25
       (calculate-cart-total [{:id 3
                               :qty 1}
                              {:id 1
                               :qty 1}
                              {:id 2
                               :qty 1}
                              {:id 4
                               :qty 2}]))))


