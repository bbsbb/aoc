import Data.Char

loadInput :: FilePath -> IO [String]
loadInput filepath = fmap lines (readFile filepath)

data Position = Position {x :: Int, y :: Int} deriving (Show)
data Direction = DOWN | UP | FORWARD deriving (Read, Show)

newPosition :: Direction -> Int -> Position -> Position
newPosition DOWN units (Position x y) = Position x (y + units)
newPosition UP units (Position x y) = Position x (y - units)
newPosition FORWARD units (Position x y) = Position (x + units) y

takeStep :: Position -> String -> Position
takeStep currentPosition encodedStep = newPosition direction units currentPosition where
  units = read adjustment :: Int
  direction = read $ map toUpper instruction :: Direction
  (instruction:adjustment:_) = words encodedStep

endPosition :: [String] -> Position
endPosition encodedPath = foldl takeStep (Position {x = 0, y = 0}) encodedPath


problemOne :: String -> IO Position
problemOne filepath = fmap endPosition $ (loadInput filepath)


data PositionWithAim = PositionWithAim {ax :: Int, ay :: Int, aim :: Int } deriving Show

takeStepWithAim :: PositionWithAim -> String -> PositionWithAim
takeStepWithAim currentPosition encodedStep = newPositionWithAim direction units currentPosition where
  units = read adjustment :: Int
  direction = read $ map toUpper instruction :: Direction
  (instruction:adjustment:_) = words encodedStep

newPositionWithAim :: Direction -> Int -> PositionWithAim -> PositionWithAim
newPositionWithAim DOWN units (PositionWithAim ax ay aim) = PositionWithAim ax ay (aim + units)
newPositionWithAim UP units (PositionWithAim ax ay aim) = PositionWithAim ax ay (aim - units)
newPositionWithAim FORWARD units (PositionWithAim ax ay aim) = PositionWithAim (ax + units) (ay + (aim*units)) aim

endPositionWithAim :: [String] -> PositionWithAim
endPositionWithAim encodedPath = foldl takeStepWithAim (PositionWithAim {ax = 0, ay = 0, aim = 0}) encodedPath


problemTwo :: String -> IO PositionWithAim
problemTwo filepath = fmap endPositionWithAim $ (loadInput filepath)
