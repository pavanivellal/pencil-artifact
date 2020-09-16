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
        base-price      (:base-price volume-offers)
        special-offers  (when offer-date
                          (get-special-offers item-id base-price offer-date qty))]
    {:special-offers special-offers
     :volume-offers volume-offers}))


