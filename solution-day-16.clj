(ns playground-clj.djk)

(defn load-input! [filepath]
  (-> filepath
      clojure.java.io/resource
      clojure.java.io/reader
      line-seq))

(defn ->bitstring [input]
  (reduce (fn [s hex]
            (let [bits (Integer/toBinaryString (Integer/parseInt (str hex) 16))]
              (str s (apply str (concat (repeat (- 4 (count bits)) \0) bits))))) "" input))

(defn read-bits [bits position size]
  {:bits          (subs bits position (+ position size))
   :next-position (+ position size)})

(defn get-version-type [bits position]
  (let [{version :bits
         current :next-position} (read-bits bits position 3)
        {type    :bits
         current :next-position} (read-bits bits current 3)]
    {:version       (Integer/parseInt version 2)
     :type          (Integer/parseInt type 2)
     :next-position current}))

(defn get-packet-value [type packet-values]
  (cond
    (= 0 type) (apply + packet-values)
    (= 1 type) (apply * packet-values)
    (= 2 type) (apply min packet-values)
    (= 3 type) (apply max packet-values)
    (= 5 type) (if (apply > packet-values) 1 0)
    (= 6 type) (if (apply < packet-values) 1 0)
    (= 7 type) (if (apply = packet-values) 1 0)))

(defn read-literal [version bits position]
  (loop [bitinfo (read-bits bits position 5)
         packet  ""]
    (if (= (.charAt (:bits bitinfo) 0) \0)
      {:payload       (str packet (subs (:bits bitinfo) 1 5))
       :count         1
       :value         (Long/parseLong (str packet (subs (:bits bitinfo) 1 5)) 2)
       :version       version
       :next-position (:next-position bitinfo)}
      (recur (read-bits bits (:next-position bitinfo) 5)
             (str packet (subs (:bits bitinfo) 1 5))))))

(defn read-operator [type version bits position]
  (let [{length  :bits
         current :next-position} (read-bits bits position 1)]
    (if (= length "0")
      (let [{packet-size :bits
             current     :next-position} (read-bits bits current 15)
            packet-end                   (+ current (Integer/parseInt packet-size 2))]
        (loop [packets       []
               next-position current
               total-version version]
          (if (>= next-position packet-end)
            {:payload       (str length packet-size)
             :next-position next-position
             :version       total-version
             :value         (get-packet-value type (mapv :value packets))
             :subs          packets}
            (let [{:keys [version type next-position]} (get-version-type bits next-position)
                  sub-packet                           (if (= type 4)
                                                         (read-literal version bits next-position)
                                                         (read-operator type version bits next-position))]
              (recur (conj packets (assoc sub-packet :version version :type type))
                     (:next-position sub-packet)
                     (+ total-version (:version sub-packet)))))))
      (let [{encoded-count :bits
             current       :next-position} (read-bits bits current 11)
            packet-count                   (Integer/parseInt encoded-count 2)]
        (loop [packets       []
               next-position current
               total-version version]
          (if (= (count packets) packet-count)
            {:payload       (str length encoded-count)
             :next-position next-position
             :version       total-version
             :value         (get-packet-value type (mapv :value packets))
             :subs          packets}
            (let [{:keys [version type next-position]} (get-version-type bits next-position)
                  sub-packet                           (if (= type 4)
                                                         (read-literal version bits next-position)
                                                         (read-operator type version bits next-position))]
              (recur (conj packets (assoc sub-packet :version version :type type))
                     (:next-position sub-packet)
                     (+ total-version (:version sub-packet))))))))))

(defn get-packet [bits position]
  (let [{:keys [version type next-position]} (get-version-type bits position)
        packet                               (if (= type 4)
                                               (read-literal version bits next-position)
                                               (read-operator type version bits next-position))]
    packet))

(defn problems [filepath]
  (let [bits (->> filepath
                  load-input!
                  first
                  ->bitstring)]
    (select-keys (get-packet bits 0) [:version :value])))

(problems "input-day-16.txt") ;; {:version 906, :value 819324480368}
