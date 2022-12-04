#lang racket

(define (assignment->interval s)
  (map string->number (string-split s "-")))

(define (input->assignments s)
  (map assignment->interval (string-split s ",")))

(define (contains-assignment interval)
  (let ([a1 (first interval)]
        [a2 (second interval)])
    (or
     (<= (first a1) (first a2) (second a2) (second a1) )
     (<= (first a2) (first a1) (second a1) (second a2)))))

(define (overlaps-assignment interval)
  (let ([a1 (first interval)]
        [a2 (second interval)])
    ;; Ugh, fuck meee
    (or
     (<= (first a1) (first a2) (second a1))
     (<= (first a2) (first a1) (second a2))
     (<= (first a1) (second a2) (second a1))
     (<= (first a2) (second a1) (second a2)))))

(define (problem-01 input-file)
  (let ([intervals (map input->assignments (file->lines input-file))])
    (length (filter contains-assignment intervals))))

(define (problem-02 input-file)
  (let ([intervals (map input->assignments (file->lines input-file))])
    (length (filter overlaps-assignment intervals))))


;; 644
(problem-01 "2022/input-day-04.txt")

;; 926
(problem-02 "2022/input-day-04.txt")
