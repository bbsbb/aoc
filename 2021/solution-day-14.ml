open Core
module StringMap = Map.Make(String)

let make_instruction s =
  (String.sub s ~pos:0 ~len:2, Str.last_chars s 1)

let is_instruction s  =
  let re = Str.regexp ".+-.+" in
  Str.string_match re s 0;;

let read_input filename =
  let ic = In_channel.create filename in
  In_channel.fold_lines ic
    ~init:(("", []))
    ~f:(fun (o, ins) line ->
      match line with
      | "" -> (o, ins)
      | _ -> match is_instruction line with
             | true -> (o, make_instruction line::ins)
             | false -> (line, ins)
    )

let map_add m target cnt =
  StringMap.update m target ~f:(fun c -> match c with
                                         | Some c -> (c + cnt)
                                         | _ -> cnt)

let polymer_sum m =
  StringMap.fold m ~f:(fun ~key:k ~data:v acc ->
      let s = (String.sub k ~pos:0 ~len:1) in
      map_add acc s v
    ) ~init:StringMap.empty

let print_map m =
  print_string "==\n";
  StringMap.fold m ~f:(fun ~key:k ~data:v acc -> Printf.printf "%s : %d\n" k v; acc) ~init:();
  print_string "==\n"

let rec chain_lookups proteins last pairs =
  match proteins with
  | [] -> pairs
  | p::rest -> let pair = (last ^ p) in
               chain_lookups rest p (map_add pairs pair 1)

let rec apply_instructions pairs instructions next_pairs =
  match instructions with
  | [] -> next_pairs
  | (condition, s)::rest -> let current = match StringMap.find pairs condition with
                              | Some v -> v
                              | _ -> 0 in
                            let new_pairs = if current > 0
                                            then
                                              let with_prefix = map_add next_pairs ((String.sub condition ~pos:0 ~len:1) ^ s) current in
                                              map_add with_prefix (s ^ (Str.last_chars condition 1)) current
                                            else next_pairs in
                            apply_instructions pairs rest new_pairs

let rec steps pairs instructions cnt =
  if cnt > 0
  then
    let new_pairs = apply_instructions pairs instructions StringMap.empty in
    steps new_pairs instructions (cnt - 1)
  else pairs

let map_min_max m initial =
  StringMap.fold m ~f:(fun ~key:_ ~data:v (mn, mx) ->
      if v > mx then (mn, v)
      else if v < mn then (v, mx)
      else (mn, mx)) ~init:(initial, initial)

let () =
  let (origin, is) = read_input "input-day-14.txt" in
  let proteins = List.map ~f:(fun e -> String.make 1 e) (List.init (String.length origin) ~f:(String.get origin)) in
  let (initial, rest) = match proteins with
    | [] -> ("", [])
    | s::rest -> (s, rest) in

  let pairs = chain_lookups rest initial StringMap.empty in
  let p1_pairs = steps pairs is 10 in
  let p1_polymers = (polymer_sum p1_pairs) in
  let p1_fuck_my_life = map_add p1_polymers (Str.last_chars origin 1) 1 in
  let (min, max) = map_min_max p1_fuck_my_life (match StringMap.find p1_fuck_my_life "N" with
                                                | Some v -> v
                                                | _ -> 0) in
  Printf.printf "P1: %d\n" (max - min);

  let p2_pairs = steps pairs is 40 in
  let p2_polymers = (polymer_sum p2_pairs) in
  let p2_fuck_my_life = map_add p2_polymers (Str.last_chars origin 1) 1 in
  let (min, max) = map_min_max p2_fuck_my_life (match StringMap.find p2_fuck_my_life "N" with
                                                | Some v -> v
                                                | _ -> 0) in
  Printf.printf "P2: %d\n" (max - min);

  ()
