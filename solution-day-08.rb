digitsInput = File.readlines("input-day-08.txt").map {
  |l| l.split(" | ")[1]
}

encodedInput = File.readlines("input-day-08.txt")

# 1
puts digitsInput.flat_map{|l| l.split(" ").select{|s| [2,3,4,7].include?(s.length)}}.length


# 2
$origin = {
  "abcefg" => 0,
  "cf" => 1,
  "acdeg" => 2,
  "acdfg" => 3,
  "bcdf" => 4,
  "abdfg" => 5,
  "abdefg" => 6,
  "acf" => 7,
  "abcdefg" => 8,
  "abcdfg" => 9
}

$lexicon = "abcdefg".chars.to_a.permutation.map(&:join)
$base_index = "a".ord

def candidates_map(encoded_line)
  candidates = encoded_line.split(" ")
  lookup_map = {}
  for possible in $lexicon
    lookup_map = {}
    for encoded_digit in candidates
      decoded = encoded_digit.chars.map{|c| possible[c.ord - $base_index]}.sort(&:casecmp).join
      if $origin.key?(decoded)
        lookup_map[encoded_digit.chars.sort(&:casecmp).join] = $origin[decoded]
      end
    end
    if lookup_map.size == candidates.size
      break
    end
  end
  lookup_map
end

def read_number(state, lookup)
  number = 0
  digits = state.split(" ")
  for d in digits
    ordered_digits = d.chars.sort(&:casecmp).join
    number = number*10 + lookup[ordered_digits]
  end
  number
end

$numbers = []
for l in encodedInput
  encoded, state = l.split(" | ")
  lookup = candidates_map encoded
  $numbers.append read_number(state, lookup)
end

puts $numbers.sum
