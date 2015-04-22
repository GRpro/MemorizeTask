;; Memoize function example using refs.
;;
;; The code which influence refs whith some modification
;; runs within transaction. So function is thread-safe.
;;
;; Algorithm works as following. We have two refs:
;; cache - map which stores args as key and function's result with this args as value
;; vals - vector with keys (stores insertion ordering)
;; At first we check cache if the result of function is present for specific args
;; If yes - return result from cache. If not - computing result of function
;; If the cache is full we will delete element from the cache by args which are
;; first args was inserted. Then remove first args from vals.
;; If cache is not full yet we just insert into map new entry and insert into val
;; new args (in the end). Then return function's result.
(defn bounded-memoize [f n]
  (if (< n 1)
    (throw (IllegalArgumentException. "cache size could not be less than 1")))
  (let [cache (ref {}) vals (ref [])]
    (fn [& args]
      (if-let [e (find @cache args)]
        (val e)
        (let [ret (apply f args)]
          (dosync
            (when (= (count @vals) n)
              (alter cache dissoc (first @vals))
              (alter vals rest))
            (alter cache assoc args ret)
            (alter vals conj args))
          ret)))))

(defn f[a]
  (do
    (println "doing some work")
    (+ a 10)
    )
  )

(def mem (bounded-memoize f 3))