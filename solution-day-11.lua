input = {}

local raw = io.open("./input-day-11.txt", "r")
local row = 1

for input_line in raw:lines() do
   input[row] = {}
   column = 1
   for ch in input_line:gmatch"." do
       input[row][column] = tonumber(ch)
       column = column + 1
   end
   row = row + 1
end

function step_board(rows, columns)
    local b = {}
    for i = 1, rows do
        b[i] = {}
        for j = 1, columns do
            b[i][j] = false
        end
    end
    return b
end

function neighbours(i,j, maxrow, maxcolumn)
   candidates = {
      {i-1, j-1}, {i-1, j}, {i-1, j +1},
      {i, j-1},               {i, j +1},
      {i+1, j-1}, {i+1, j}, {i+1, j +1},
   }
   valid = {}

   for _, c in ipairs(candidates) do
      if c[1] < maxrow and c[2] < maxcolumn and c[1] > 0 and c[2] > 0 then
         valid[#valid + 1] = c
      end
   end

   return valid
end

function count_flashes(board_state)
   count = 0
   for i = 1, #board_state do
      for j = 1, #board_state[i] do
         if board_state[i][j] then
            count = count + 1
         end
      end
   end
   return count
end

function process_flashes(board, board_state, flashes)
   if #flashes == 0 then
      return count_flashes(board_state)
   end

   new_flashes = {}
   for _, coordinates in ipairs(flashes) do
      board[coordinates[1]][coordinates[2]] = 0
      for _, n in ipairs(neighbours(coordinates[1], coordinates[2], #board + 1, #board[1] + 1)) do
         i = n[1]
         j = n[2]
         if not board_state[i][j] then
            -- I guess shit hasn't flashed yet?
           board[i][j] = board[i][j] + 1
           if board[i][j] > 9 then
              board_state[i][j] = true
              new_flashes[#new_flashes + 1] = {i,j}
           end
         end
      end
   end

   return process_flashes(board, board_state, new_flashes)
end


function step (board)

   -- I don't care, I can't have tuples so we can't have nice things.
   local board_state = step_board(#board, #board[1])
   local flashes =  {}


   -- This thing is exactly like the shit in process_flashes,
   -- but I need to change the storage structure to fix. uugh.
   for i, row in ipairs(board) do
       for j, v in ipairs(row) do
           board[i][j] = board[i][j] + 1
           if board[i][j] > 9 then
              board_state[i][j] = true
              flashes[#flashes + 1] = {i,j}
           end
       end
   end

   return process_flashes(board, board_state, flashes)
end


-- 1
flashes = 0
for i = 1, 100 do
   flashes = flashes + step(input)
end
print(flashes)

-- 2
all = #input * #input[1]
flashes = 0
iteration = 100
while all > flashes do
   flashes = step(input)
   iteration = iteration + 1
end

print(iteration)
