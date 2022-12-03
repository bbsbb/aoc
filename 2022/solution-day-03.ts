import * as fs from "fs";
// string == definitely typed

const input = fs.readFileSync("input-day-03.txt", "utf-8").split("\n");

function itemPriority(c: string): number {
  const item = c.charCodeAt(0);
  return item < 96 ? item - 38 : item - 96;
}

function duplicateString(compartments: string): string | null {
  const first: Record<string, boolean> = {};
  const second: Record<string, boolean> = {};
  for (let i = 0, j = compartments.length - 1; i < j; i++, j--) {
    first[compartments[i]] = true;
    second[compartments[j]] = true;
    if (typeof first[compartments[j]] !== "undefined") {
      return compartments[j];
    } else if (typeof second[compartments[i]] !== "undefined") {
      return compartments[i];
    }
  }
  return null;
}

//1. 8053
console.log(
  input.reduce((s, line) => {
    const duplicateItem = duplicateString(line);
    return s + (duplicateItem ? itemPriority(duplicateItem) : 0);
  }, 0)
);

// 2.
function common(s1: string, s2: string): string {
  const freq: Record<string, boolean> = {};
  [...s1].forEach((c) => (freq[c] = true));
  return Object.keys(
    [...s2].reduce((common: Record<string, boolean>, c) => {
      if (typeof freq[c] !== "undefined") {
        common[c] = true;
      }
      return common;
    }, {})
  ).join("");
}

console.log(
  input.slice(1).reduce(
    ({ carry, index, total }, current) => {
      const partition = index % 3;
      return {
        index: index + 1,
        carry: partition === 0 ? current : common(carry, current),
        total: partition === 0 ? total + itemPriority(carry) : total,
      };
    },
    {
      index: 1,
      carry: input[0],
      total: 0,
    }
  ).total
);
