(defn bounded-memoize [f k]
  (if (< k 1)
    (throw (IllegalArgumentException. "cache size could not be less than 1")))
  (let [cache (ref {})
        values (ref clojure.lang.PersistentQueue/EMPTY)]
    (fn [& args]
      (if-let [e (find @cache args)]
        (val e)
        (let [result (apply f args)]
          (dosync
            (alter values conj args)
            (alter cache assoc args result)
            (if (> (count @values) k)
              (let [evict (peek @values)]
                (alter values pop)
                (alter cache dissoc evict)))
            result))))))

(defn f[a]
  (do
    (println "doing some work")
    (+ a 10)
    )
  )

(def mem (bounded-memoize f 3))