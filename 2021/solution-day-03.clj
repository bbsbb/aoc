;;(ns playground-clj.core)

(defn load-input! [filepath]
  (-> filepath
      clojure.java.io/resource
      clojure.java.io/reader
      line-seq))

(defn report->bits [report-lines]
  (reduce (fn [acc line]
            (into [] (map-indexed (fn [idx bit]
                                    (conj (get-in acc [idx] [])
                                          (Character/digit bit 2)))
                                  line)))
          []
          report-lines))

(defn problem-one [filepath]
  (let [input       (load-input! filepath)
        bits        (report->bits input)
        diagnostics (reduce (fn [diagnostics bits-column]
                              (let [fq    (frequencies bits-column)
                                    gamma (key (apply max-key val fq))]
                                (-> diagnostics
                                    (update :gamma #(bit-or (bit-shift-left % 1)
                                                            gamma))
                                    ;; don't want to deal with msb
                                    (update :epsilon #(bit-or (bit-shift-left % 1)
                                                              (bit-xor gamma 1))))))
                            {:gamma 0 :epsilon 0}
                            bits)]
    (* (:gamma diagnostics)
       (:epsilon diagnostics))))


(defn important-bit [pred fq]
  (if (pred (get fq 1 0) (get fq 0 0))
    1
    0))

(defn relevant-measurements [pred measurements position]
  (let [fqs     (mapv frequencies (report->bits measurements))
        leading (important-bit pred (get fqs position))]
    (filter #(= leading (Character/digit (nth % position) 2)) measurements)))

(defn measurement [pred measurements]
  (loop [ms  measurements
         idx 0]
    (if (= 1 (count ms))
      (Integer/parseInt (first ms) 2)
      (recur (relevant-measurements pred ms idx)
             (inc idx)))))

(defn problem-two [filepath]
  (let [input (load-input! filepath)
        ox-rating (measurement >= input)
        co-rating (measurement < input)]
    (* ox-rating co-rating)))

#_(problem-two "input.txt")
#_(problem-one "input.txt")
