#lang racket

(define (larger-than-previous depths)
  (count identity (map < depths (append (cdr depths) '(-1)))))

(define (problem-01 input-file)
  (let ([depths (map string->number (file->lines input-file))])
    (larger-than-previous depths)))

(define (problem-02 input-file)
  (let* ([depths (map string->number (file->lines input-file))]
         [rolling-depths (foldl (lambda (depth window)
                                  (list (list (cadar window) depth)
                                        (append (cadr window) (list (apply + depth (car window))))))
                                (list (take depths 2) '())
                                (cddr depths))])
    (larger-than-previous (cadr rolling-depths))))

;;(problem-01 "input-day-01-01.txt")

;;(problem-02 "input-day-01-01.txt")
