(ns pencil-artifact.helper.offers-test
  (:require [clojure.test :refer :all])
  (:require [pencil-artifact.helper.offers :as offers]
            [pencil-artifact.data :as data]))

(data/init)

(deftest get-special-offers-test
  (testing "percentage offer to amount offer"
    (is (= '({:end-date "2100-10-01",
              :frequency "year",
              :recurring true,
              :percentage 25,
              :day-in-week nil,
              :offer-description "25% off Key Lime Cheesecakes",
              :date-in-month 1,
              :qty 1,
              :start-date "2020-10-01",
              :price 6.0,
              :month-in-year 10})
           (offers/get-special-offers 2 {:qty 1
                                         :price 8} "2020-10-01" 1)))
    (is (= '({:end-date "2100-01-01",
              :frequency "week",
              :recurring true,
              :percentage 50,
              :day-in-week 2,
              :offer-description "Mini Gingerbread Donuts, Two for one",
              :date-in-month nil,
              :qty 2,
              :start-date "2020-01-01",
              :price 0.5,
              :month-in-year nil})
           (offers/get-special-offers 4 {:qty 1
                                         :price 0.5} "2020-09-15" 2)))))

(deftest get-applicable-offers-test
  (testing "get list of all applicable offers"
    (is (= {:special-offers '({:end-date "2100-10-01",
                               :frequency "year",
                               :recurring true,
                               :percentage 25,
                               :day-in-week nil,
                               :offer-description "25% off Key Lime Cheesecakes",
                               :date-in-month 1,
                               :qty 1,
                               :start-date "2020-10-01",
                               :price 6.0,
                               :month-in-year 10}),
            :volume-offers {:base-price {:qty 1, :price 8}, :offers '({:qty 1, :price 8})}}
           (offers/get-applicable-offers 2 "2020-10-01" 1)))))
