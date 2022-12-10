use std::fs::File;
use std::io::{BufReader, BufRead};


fn p1 (filename: &str) ->  i32 {
    let fh = File::open(filename).unwrap();
    let r = BufReader::new(fh);
    let mut registry = 1;
    let mut cycle = 1;
    let mut measure = 20;
    let mut signals = 0;

    for line in r.lines() {
        let instructions = match line {
            Ok(encoded) => encoded.split(" ").map(String::from).collect(),
            _ =>  Vec::new()
        };

        for instruction in instructions {
            if cycle == measure {
                signals = signals + cycle * registry;
                measure = measure + 40;
            }

            match instruction.parse::<i32>() {
                Ok(next) => registry = registry + next,
                _ => (),
            }

            cycle = cycle + 1;
        }
    }
    return signals
}

fn p2 (filename: &str) {
    let fh = File::open(filename).unwrap();
    let r = BufReader::new(fh);

    let mut reg: i32 = 1;

    let mut cycle: i32 = 1;
    let mut crt = [false;240];
    for line in r.lines() {
        let instructions = match line {
            Ok(encoded) => encoded.split(" ").map(String::from).collect(),
            _ =>  Vec::new()
        };

        for instruction in instructions {
            let pixel = (cycle - 1) % 240;
            crt[pixel as usize] = (cycle % 40 >= reg) && (cycle % 40 <= (reg + 2));

            match instruction.parse::<i32>() {
                Ok(n) => reg = reg + n,
                _ => (),
            }
            cycle = cycle + 1;
        }
    }

    for (i, b) in crt.iter().enumerate() {
        if i % 40 == 0 {
            print!("\n")
        }

        if *b {
            print!("#")
        } else {
            print!(".")
        }
    }

    println!("\nTotal cycles: {} / Registry: {}", cycle, reg)
}


fn main() {
    // 13480
    println!("{:?}", p1("input-day-10.txt"));

    // EGJNGCFK
    p2("input-day-10.txt");
}
