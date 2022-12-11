(defn line->items [s]
  (as-> s $
    (clojure.string/split $ #":")
    (second $)
    (clojure.string/trim $)
    (clojure.string/split $ #", ")
    (mapv #(Integer/parseInt % 10) $)))

(defn line->op [s]
 (as-> s $
   (re-find #"(\*|\+).*" $)
   (first $)
   (clojure.string/split $ #" ")))

;;Added cause p2
(defn line->fuck [s]
  (-> s first (clojure.string/split #"by ") second (Integer/parseInt 10)))

(defn line->move [move]
  (let [pred?   #(zero? (mod % (line->fuck move)))
        targets [(-> move second (clojure.string/split #"monkey ") second (Integer/parseInt 10))
                 (-> move last (clojure.string/split #"monkey ") second (Integer/parseInt 10))]]
    (fn [worry]
      (if (pred? worry)
        (first targets)
        (second targets)))))


(defn load-input [fname]
  (as-> fname $
    (slurp $)
    (clojure.string/split $ #"\n\n")
    (map (fn [m]
           (let [monkey-lines (clojure.string/split m #"\n")]
             {:items      (line->items (second monkey-lines))
              :divider    (line->fuck (drop 3 monkey-lines))
              :op         (line->op (nth monkey-lines 2))
              :move       (line->move (drop 3 monkey-lines))
              :inspection 0})) $)))


(defn next [operator current target]
  (let [f      (if (= operator "+")
                 +
                 *)
        static (try (Integer/parseInt target 10)
                    (catch Exception _ current))]
    (f current static)))


(defn monkey-do [m idx item modifier]
  (let [{:keys [op move]} (get m idx)
        [operator target] op
        new-item          (modifier (next operator item target))
        target            (move new-item)]
    #_(printf "Index: %d | Item: %d | Target %d\n" idx new-item target)
    (-> m
        (update-in [target :items] conj new-item)
        (update-in [idx :inspection] inc))))


(defn monkey-round [monkeys modifier]
  (reduce (fn [m current-idx]
            (loop [items           (get-in m [current-idx :items])
                   monkey-progress m]
              (if-not (seq items)
                (assoc-in monkey-progress [current-idx :items] [])
                (recur (rest items)
                       (monkey-do monkey-progress current-idx (first items) modifier)))))
          monkeys
          (range 0 (-> monkeys vals count))))

(defn gcd [n k]
  (loop [current n
         next    k]
    (if (zero? next)
      current
      (recur next (mod current next)))))


(defn lcm [n k]
  (/ (* n k)
     (gcd n k)))


(defn p [rounds modifier]
  (let [input        (load-input "input-day-11.txt")
        monkeys      (zipmap (range 0 (count input)) input)
        all-dividers (->> monkeys vals (mapv :divider))
        multiple     (reduce lcm all-dividers)
        modifier-fn  (if modifier
                       modifier
                       #(mod % multiple))
        final        (loop [m monkeys
                            r rounds]
                       (if (= r 0)
                         m
                         (recur (monkey-round m modifier-fn) (dec r))))]
    (apply * (->> final vals (mapv :inspection) (sort >) (take 2)))))



;; 76728
(println (p 20 #(quot % 3)))

;; 21553910156
(println (p 10000 nil))
