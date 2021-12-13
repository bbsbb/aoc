defmodule Aoc do
  def read_input(filename) do
    {:ok, s} = File.read(filename)
    String.split(s, "\n")
    |> Enum.reduce(
      %{"dots" => MapSet.new(), "folds" => [], "target"=> "dots"},
    fn line, acc ->
      case line do
        "" -> %{acc | "target" => "folds"}
        _ -> case acc["target"] do
               "dots" -> %{acc | "dots" => MapSet.put(acc["dots"], String.split(line, ",") |> Enum.map(&String.to_integer/1))}
               _ -> %{acc | "folds" => acc["folds"] ++ [[String.split(line, "=") |> Enum.at(0) |> String.at(-1),
                                                        String.split(line, "=") |> Enum.at(1) |> String.to_integer]]}
             end
      end
    end)
  end

  def apply_fold(sheet_fold, dots) do
    [direction, change] = sheet_fold
    if direction == "y" do
      Enum.filter(dots, fn [_,y] -> y <= change*2 end)
      |>  Enum.reduce(MapSet.new(),
      fn dot, visible ->
        case dot do
          [_, y] when y == change -> visible
          [x, y] when y > change -> MapSet.put(visible, [x, change*2 - y])
          _ -> MapSet.put(visible, dot)
        end
      end
      )
    else
      Enum.filter(dots, fn [x,_] -> x <= change*2 end)
      |>  Enum.reduce(MapSet.new(),
        fn dot, visible ->
          case dot do
            [x, _] when x == change -> visible
            [x,y] when x > change -> MapSet.put(visible, [change*2 - x, y])
            _ -> MapSet.put(visible, dot)
          end
        end
      )
    end
  end


  def p1() do
    input = read_input("input-day-13.txt")
    apply_fold(Enum.at(input["folds"], 0), input["dots"])
  end

  def p2() do
    input = read_input("input-day-13.txt")
    folded_paper = Enum.reduce(input["folds"], input["dots"], fn f, dots -> apply_fold(f, dots) end)
    Enum.each(0..6, fn i  ->
      IO.write("\n")
      Enum.each(0..40, fn j ->
        if MapSet.member?(folded_paper, [j, i]) do
          IO.write("0")
        else
          IO.write(".")
        end
      end)
    end)
  end
end

#1.
IO.inspect MapSet.size(Aoc.p1)

#2.
IO.inspect Aoc.p2
