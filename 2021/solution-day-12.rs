use std::collections::{HashMap, HashSet};
use std::collections::hash_map::Entry;
use std::fs::File;
use std::io::{BufReader, BufRead};
use std::iter::FromIterator;

fn read_graph (filename: &str) -> HashMap<String, Vec<String>> {
    let mut g = HashMap::new();
    let fh = File::open(filename).unwrap();
    let r = BufReader::new(fh);

    for line in r.lines() {
        let nodes = match line {
            Ok(edge) => edge.split("-").map(String::from).collect(),
            _ => Vec::new(),
        };

        if nodes[1] != "start" {
            match g.entry(nodes[0].clone()) {
                Entry::Vacant(e) => {e.insert(vec![nodes[1].clone()]);},
                Entry::Occupied(mut e) => e.get_mut().push(nodes[1].clone()),
            }
        }

        if nodes[0] != "start" {
            match g.entry(nodes[1].clone()) {
                Entry::Vacant(e) => {e.insert(vec![nodes[0].clone()]);},
                Entry::Occupied(mut e) => {
                    let ns = e.get_mut();
                    if !ns.iter().any(|s| *s == *nodes[0]) {
                        e.get_mut().push(nodes[0].clone());
                    }
                }
            }
        }
    }
    return g
}

fn has_small_cave_duplicates(p: Vec<String>) -> bool {
    let small_caves:Vec<String> = p.iter().filter(|n| n.chars().nth(0).unwrap().is_lowercase()).map(String::from).collect();
    let unique_small_caves: HashSet<String> = HashSet::from_iter(small_caves.iter().cloned());
    return small_caves.len() != unique_small_caves.len();
}

fn all_paths<'a>(current: &'a str, target: &'a str, graph: HashMap<String, Vec<String>>, path: Vec<String>, allow_duplicate: bool) -> Vec<Vec<String>> {
    let current_path = [path, vec![String::from(current)]].concat();
    if *current == *target {
        return vec![current_path]
    }

    let mut paths_from_node:Vec<Vec<String>> = Vec::new();

    match graph.clone().get(current) {
        Some(neighbours) => {
            for node in neighbours.iter() {

                if !(node.chars().nth(0).unwrap().is_uppercase() || !current_path.clone().iter().any(|s| *s == *node)) {
                    //println!("Skipping {:?} for path {:?}", *node, current_path);
                }

                if node.chars().nth(0).unwrap().is_uppercase()
                    || !current_path.clone().iter().any(|s| *s == *node)
                    || (allow_duplicate && !has_small_cave_duplicates(current_path.clone()))
                {

                    let node_paths = all_paths(node, target.clone(), graph.clone(), current_path.clone(), allow_duplicate);
                    for np in node_paths.iter() {
                        paths_from_node.push(np.iter().map(String::from).collect())
                    }
                }
            }
        },
        _ => {println!("No neighbours for {:?}", &current)},
    }

    return paths_from_node
}


fn main() {
    let graph = read_graph("input-day-12.txt");
    println!("{:?}", graph);


    // 1.
    let paths = all_paths("start", "end", graph.clone(), Vec::new(), false);
    println!("Path count: {:?}", paths.len());

    // 2.
    let with_duplicate_caves = all_paths("start", "end", graph.clone(), Vec::new(), true);
    println!("Path count: {:?}", with_duplicate_caves.len());
}
