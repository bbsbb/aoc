<?php

$input = file_get_contents("input-day-06.txt");

function wut($input, $window_size = 4 ) {
    $window = [];
    foreach (str_split($input) as $index => $c) {
        foreach ($window as $windex => $distinct) {
            if ($distinct == $c) {
                $window = array_slice($window, $windex + 1);
                break;
            }
        }
        array_push($window, $c);
        if (count($window) == $window_size) {
            return $index + 1;
        }
    }
}

// 1912
print(wut($input) . "\n");

// 2122
print(wut($input, 14) . "\n");
