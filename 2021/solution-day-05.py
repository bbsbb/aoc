# Why do maths, when you can bruteforce it?????

import re
from pathlib import Path
from typing import Dict, List


class Coordinates:
    def __init__(self, *, x: int, y: int):
        self.x = x
        self.y = y

    @staticmethod
    def from_encoded_input(coordinates: str) -> "Coordinates":
        xy = coordinates.split(",")
        return Coordinates(x=int(xy[0]), y=int(xy[1]))

    def __repr__(self) -> str:
        return f"({self.x},{self.y})"


class Vent:
    def __init__(self, start: Coordinates, end: Coordinates):
        self.start = start
        self.end = end

    def points(self) -> List[Coordinates]:
        points: List[Coordinates] = []

        min_x = min(self.start.x, self.end.x)
        min_y = min(self.start.y, self.end.y)
        distance_x = abs(self.start.x - self.end.x) + min_x
        distance_y = abs(self.start.y - self.end.y) + min_y
        if distance_x == min_x:
            for y in range(min_y, distance_y + 1):
                points.append(Coordinates(x=self.start.x, y=y))
        elif distance_y == min_y:
            for x in range(min_x, distance_x + 1):
                points.append(Coordinates(x=x, y=self.start.y))
        else:  # 45d
            x = self.start.x
            y = self.start.y
            x_direction = 1 if self.start.x < self.end.x else -1
            y_direction = 1 if self.start.y < self.end.y else -1
            for i in range(0, abs(self.start.x - self.end.x) + 1):
                points.append(Coordinates(x=x, y=y))
                x = x + (1 * x_direction)
                y = y + (1 * y_direction)
        return points

    def is_axis_parallel(self) -> bool:
        return self.start.x == self.end.x or self.start.y == self.end.y

    @staticmethod
    def from_encoded_input(filepath: Path) -> List["Vent"]:
        vents: List[Vent] = []
        with open(filepath) as f:
            for line in f.readlines():
                coordinates_input = re.sub(r"(\s|\n)", "", line).split("->")
                vents.append(
                    Vent(
                        Coordinates.from_encoded_input(coordinates_input[0]),
                        Coordinates.from_encoded_input(coordinates_input[1]),
                    )
                )
        return vents

    def __repr__(self) -> str:
        return f"{self.start}-{self.end}"


def overlapping(all_lines: bool = False) -> int:
    vents = [
        vent
        for vent in Vent.from_encoded_input("input-day-05.txt")
        if all_lines or vent.is_axis_parallel()
    ]

    seabed: Dict[str, int] = {}
    for vent in vents:
        for p in vent.points():
            ph = p.__repr__()

            if ph not in seabed:
                seabed[ph] = 1
            else:
                seabed[ph] += 1

    return [(k, v) for k, v in seabed.items() if v > 1]


# 1
print(len(overlapping()))


# 2.
print(len(overlapping(True)))
