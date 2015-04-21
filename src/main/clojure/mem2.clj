(defn bounded-memoize [f n]
  (if (< n 1)
    (throw (IllegalArgumentException. "cache size could not be less than 1")))
  (let [mem (ref {}) v (ref [])]
    (fn [& args]
      (if-let [e (find @mem args)]
        (val e)
        (let [ret (apply f args)]
          (dosync
            (when (= (count @v) n)
              (alter mem dissoc (first @v))
              (alter v subvec 1))
            (alter mem assoc args ret)
            (alter v conj args))
          ret)))))

(defn f[a]
  (do
    (println "doing some work")
    (+ a 10)
    )
  )

(def mem (bounded-memoize f 3))