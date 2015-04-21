;; memorization by args 1
(defn bounded-memoize [f n]
  (if (< n 1)
    (throw (IllegalArgumentException. "cache size could not be less than 1")))
  (let [cache (agent (array-map))]
    (fn [x]
      (dosync
        (let [mem-res (get @cache x)]
          (or mem-res
            (let [result (f x)]
              (if-not (< (count @cache) n)
                (send cache dissoc (first (keys @cache))))
              (send cache assoc x result)
              result)))))))

(defn f[a]
  (do
    (println "doing some work")
    (+ a 10)
    )
  )

(def mem (bounded-memoize f 3))