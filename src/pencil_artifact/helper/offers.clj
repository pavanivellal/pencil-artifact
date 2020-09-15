(ns pencil-artifact.helper.offers
  (:require [pencil-artifact.data        :as data]
            [pencil-artifact.helper.date :as dt]
            [clj-time.core              :as t]))

(defn- get-valid-offers [item-id offer-date qty]
  (filter #(and (dt/within-range? offer-date
                                  (:start-date %)
                                  (:end-date %))
                (<= (:qty %) qty))

          (get @data/offers item-id)))

(defn convert-percentage-offers-to-amount
  "convert offers in percentage to amount based on base price of item"
  [base-price offers]
  (let [base-price        (:price base-price)]
    (map (fn [item]
           (let [percentage (:percentage item)
                 multiple   (:qty item)
                 tot-price  (* multiple base-price)]
             (if percentage
               (merge item
                      {:price (- tot-price
                                (float (* tot-price (/ percentage 100))))}) ;; Discount
               item))) offers)))


(comment
  (convert-percentage-offers-to-amount {:qty 1
                                        :price 10}
                                       [{:qty 2
                                         :percentage 50}
                                        {:qty 1
                                         :percentage 25}])
  :end)


(defn get-special-offers [item-id base-price offer-date qty]
  (let [offers-for-item             (get-valid-offers item-id offer-date qty)
        offer-date                  (dt/parse-dt-str offer-date)
        offer-day-of-week           (t/day-of-week offer-date)
        offer-day                   (t/day offer-date)
        offer-month                 (t/month offer-date)
        weekly-offer                (filter #(and (= "week" (:frequency %))
                                                  (= offer-day-of-week (:day-in-week %))) offers-for-item)

        monthly-offer               (filter #(and (= "month" (:frequency %))
                                                  (= offer-day (:date-in-month %))) offers-for-item)

        yearly-offer                (filter #(and (= "year" (:frequency %))
                                                  (= offer-month (:month-in-year %))
                                                  (= offer-day   (:date-in-month %))) offers-for-item)
        offers                      (concat weekly-offer
                                            monthly-offer
                                            yearly-offer)
        offers                      (convert-percentage-offers-to-amount base-price offers)]
    offers))

(defn- get-volume-offers [item-id qty]
  (let [item-price  (get @data/products item-id)
        individual-price {:qty 1
                          :price (:price item-price)}
        bulk-price       (:bulkPricing item-price)
        offers           (if (and bulk-price
                                  (<= (:qty bulk-price)
                                      qty))
                           (list individual-price bulk-price)
                           (list individual-price))]
    {:base-price individual-price
     :offers offers}))

(defn get-applicable-offers
  "get list of applicable offers for item-id"
  [item-id offer-date qty]
  (let [volume-offers   (get-volume-offers item-id qty)
        all-offers      (:offers volume-offers)
        base-price      (:base-price volume-offers)
        special-offers  (when offer-date
                          (get-special-offers item-id base-price offer-date qty))]
    (println "Special offers : " special-offers)
    (println "Volume offers : " all-offers)
    (concat all-offers special-offers)))


(comment
  (data/init)

  ;; To include below test cases
  (get-special-offers 2 {:qty 1
                         :price 8} "2020-10-01" 1)
  (get-special-offers 4 {:qty 1
                         :price 0.5} "2020-09-15" 2)

  ;; To include below test cases
  (get-valid-offers 2 "2100-10-01" 1)

  ;; To include below test cases
  (get-volume-offers 3 3)
  (get-volume-offers 3 7)

  ;; To include below test cases
  (get-applicable-offers 2 "2020-10-01" 1)
  (get-applicable-offers 3 "2020-09-18" 6)
  (get-applicable-offers 3 "2020-09-18" 8)


  ;; To include below test cases
  (get-applicable-offers 3 nil 1)
  (get-applicable-offers 1 nil 4)
  (get-applicable-offers 2 nil 1)

  ;; To include below test cases
  (get-applicable-offers 3 nil 8)
  (get-applicable-offers 2 "2020-10-01" 1)

  :end)


