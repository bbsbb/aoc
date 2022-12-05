import re
from dataclasses import dataclass
from pathlib import Path
from typing import Dict, List, Tuple


@dataclass
class Move:
    source: int
    target: int
    crates: int


def read_input(filepath: Path) -> Tuple[Dict[int, List[str]], List[Move]]:
    stacks: Dict[int, List[str]] = {}
    moves: List[Move] = []
    with open(filepath) as f:
        for line in f.readlines():
            if line.startswith(" 1") or line == "\n":
                pass
            elif line.startswith("m"):
                move = re.findall(r"\d+", line)
                moves.append(
                    Move(crates=int(move[0]), source=int(move[1]), target=int(move[2]))
                )
            else:
                for stack_idx, encoded_crate in enumerate(
                    re.findall(r".{1,4}", line.replace("\n", ""))
                ):
                    crate = encoded_crate.strip()
                    if (stack_idx + 1) not in stacks:
                        stacks[stack_idx + 1] = []
                    if crate != "":
                        stacks[stack_idx + 1].insert(0, crate.replace("\n", ""))

    return (stacks, moves)


def one(filepath: Path) -> None:
    stacks, moves = read_input(filepath)
    for m in moves:
        for _ in range(0, m.crates):
            stacks[m.target].append(stacks[m.source].pop())

    return "".join([s.pop()[1] for s in stacks.values()])


def two(filepath: Path) -> None:
    stacks, moves = read_input(filepath)

    for m in moves:
        stacks[m.target].extend(stacks[m.source][-m.crates :])
        stacks[m.source] = stacks[m.source][0 : -m.crates]

    return "".join([s.pop()[1] for s in stacks.values()])


# VQZNJMWTR
print(one("input-day-05.txt"))
# NLCDCLVMQ
print(two("input-day-05.txt"))
