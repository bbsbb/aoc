import Data.Char
-- a -  rock     - x
-- b -  paper    - y
-- c -  scissors - z

loadInput :: FilePath -> IO [String]
loadInput filepath = fmap lines (readFile filepath)


rpsRound :: String -> Int
rpsRound (evil:_:good:_) = choice + case (distance, distance == abs(distance)) of
                             (0,_) -> 3
                             (1,_) -> 6
                             (-2,False) -> 6
                             (_,_) -> 0
  where distance = ord good - (ord evil + 23)
        choice = ord good `mod` ord 'W'

rpsRound2 :: String -> Int
rpsRound2 (evil:_:good:_) = 3 * outcome + case outcome of
                                            0 -> [1,2,3]!!((distance + 2) `mod` 3)
                                            1 -> [1,2,3]!!distance
                                            2 -> [1,2,3]!!((distance + 1) `mod` 3)
  where
    outcome = ord good `mod` ord 'X'
    distance = ord evil `mod` ord 'A'


score :: [String] -> Int
score rounds = foldl (\ total current -> total + rpsRound2 current) 0 rounds

-- 11063
one :: String -> IO Int
one filepath = fmap (foldl (\ total current -> total + rpsRound current) 0) (loadInput filepath)

--10349
two :: String -> IO Int
two filepath = fmap score $ (loadInput filepath)
