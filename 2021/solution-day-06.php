<?php

function populationAtDay($initial, $day) {
    $population = array_fill_keys(range(0,8),0);
    foreach ($initial as $gestation) {
        $population[$gestation]++;
    }

    while ($day > 0) {
        $next_day_population = array_fill_keys(range(0,8),0);
        foreach ($population as $gestation => $current) {
            if ($gestation == 0) {
                $next_day_population[6] += $current;
                $next_day_population[8] += $current;
            } else {
                $next_day_population[$gestation - 1] += $current;
            }
        }
        $population = $next_day_population;
        $day--;
    }
    return array_sum($population);
}

$initial = explode(
    ",",
    "1,1,3,5,3,1,1,4,1,1,5,2,4,3,1,1,3,1,1,5,5,1,3,2,5,4,1,1,5,1,4,2,1,4,2,1,4,4,1,5,1,4,4,1,1,5,1,5,1,5,1,1,1,5,1,2,5,1,1,3,2,2,2,1,4,1,1,2,4,1,3,1,2,1,3,5,2,3,5,1,1,4,3,3,5,1,5,3,1,2,3,4,1,1,5,4,1,3,4,4,1,2,4,4,1,1,3,5,3,1,2,2,5,1,4,1,3,3,3,3,1,1,2,1,5,3,4,5,1,5,2,5,3,2,1,4,2,1,1,1,4,1,2,1,2,2,4,5,5,5,4,1,4,1,4,2,3,2,3,1,1,2,3,1,1,1,5,2,2,5,3,1,4,1,2,1,1,5,3,1,4,5,1,4,2,1,1,5,1,5,4,1,5,5,2,3,1,3,5,1,1,1,1,3,1,1,4,1,5,2,1,1,3,5,1,1,4,2,1,2,5,2,5,1,1,1,2,3,5,5,1,4,3,2,2,3,2,1,1,4,1,3,5,2,3,1,1,5,1,3,5,1,1,5,5,3,1,3,3,1,2,3,1,5,1,3,2,1,3,1,1,2,3,5,3,5,5,4,3,1,5,1,1,2,3,2,2,1,1,2,1,4,1,2,3,3,3,1,3,5"
);

print("At day 80 the population is: " . populationAtDay($initial, 80) . "\n");
print("At day 80 the population is: " . populationAtDay($initial, 256) . "\n");
