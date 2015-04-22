;; memorization function which uses asynchronous agents and array-map.
;; Agents are STM-aware and can be safely used inside transactions.
;; The code which influence refs whith some modification
;; runs within transaction. So function is thread-safe.

;; Algorithm works as following. We have agent array-map which adds entries
;; in the end.
;; At first we check cache if the result of function is present for specific args
;; If yes - return result from cache. If not - computing result of function
;; If the cache is full we will delete first element from the cache (the oldest)
;; If cache is not full yet we just insert into it new entry.
;; Then return function's result.
(defn bounded-memoize [f n]
  (if (< n 1)
    (throw (IllegalArgumentException. "cache size could not be less than 1")))
  (let [cache (agent (array-map))]
    (fn [args]
        (let [mem-res (get @cache args)]
          (or mem-res
            (let [result (f args)]
              (dosync
                (if-not (< (count @cache) n)
                  (send cache dissoc (first (keys @cache))))
                (send cache assoc args result)
                result)))))))

(defn f[a]
  (do
    (println "doing some work")
    (+ a 10)
    )
  )

(def mem (bounded-memoize f 3))