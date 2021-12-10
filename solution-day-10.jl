input = readlines("input-day-10.txt")

scores = Dict(")"=>Dict("score"=>3, "open"=>"("),
              "]"=>Dict("score"=>57, "open"=>"["),
              "}"=>Dict("score"=>1197, "open"=>"{"),
              ">"=>Dict("score"=>25137, "open"=>"<"))

function line_value(line, pending)
    if isempty(line)
        return 0
    else
        current = line[1]
        if haskey(scores, current)
            if pending[1] != scores[current]["open"]
                return scores[current]["score"]
            else
                popfirst!(line)
                popfirst!(pending)
                return line_value(line, pending)
            end
        end
        popfirst!(line)
        return line_value(line, insert!(pending, 1, current))
    end
end

sum = 0
for l in input
    global sum = sum + line_value(split(l,""), [])
end

println(sum)


#2
completes = Dict("("=>1,
                 "["=>2,
                 "{"=>3,
                 "<"=>4)

function leftovers(line, pending)
    if isempty(line)
        v = 0
        for rest in pending
            v = v * 5 + completes[rest]
        end
        return v
    elseif haskey(completes, line[1])
        current = line[1]
        popfirst!(line)
        return leftovers(line, insert!(pending, 1, current))
    end
    popfirst!(line)
    popfirst!(pending)
    return leftovers(line, pending)
end

incomplete_lines = filter((l) -> line_value(split(l,""), []) == 0, input)
incomplete_values = sort(map((l) -> leftovers(split(l,""), []), incomplete_lines))
println(incomplete_values[length(incomplete_values) ÷ 2 + 1])
