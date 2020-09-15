(ns pencil-project.helper.offers
  (:require [pencil-project.data        :as data]
            [pencil-project.helper.date :as dt]
            [clj-time.core              :as t]))

(defn- get-valid-offers [item-id offer-date qty]
  (filter #(and (dt/within-range? offer-date
                                  (:start-date %)
                                  (:end-date %))
                (<= (:qty %) qty))

          (get @data/offers item-id)))

(defn- get-special-offers [item-id offer-date qty]
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
                                            yearly-offer)]
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
    offers))

(defn get-applicable-offers
  "get list of applicable offers for item-id"
  [item-id offer-date qty]
  (let [volume-offers   (get-volume-offers item-id qty)
        special-offers  (when offer-date
                          (get-special-offers item-id offer-date qty))]
    (println "Special offers : " special-offers)
    (println "Volume offers : " volume-offers)
    (concat volume-offers special-offers)))
    ;{:vo volume-offers
    ; :so special-offers}))


(comment
  (data/init)

  ;; To include below test cases
  (get-special-offers 2 "2020-09-14" 2)
  (get-special-offers 2 "2020-10-01" 1)

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

  :end)


