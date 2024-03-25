data Check = Green | Yellow | Grey deriving (Eq, Show, Read)
type Marking = [(Char, Check)]

-- Helper function to find characters marked Green
findGreen :: String -> String -> Marking
findGreen [] _ = []
findGreen _ [] = []
findGreen (s:secret) (g:guess)
    | s == g = (g, Green) : findGreen secret guess
    | otherwise = findGreen secret guess

-- Helper function to find characters marked Yellow
findYellow :: String -> String -> Marking -> Marking
findYellow _ [] _ = []
findYellow [] _ _ = []
findYellow _ _ [] = []
findYellow (s:secret) (g:guess) marking
    | s == g = findYellow secret guess marking
    | (g, Green) `elem` marking = findYellow secret guess marking
    | (g, Yellow) `elem` marking = findYellow secret guess marking
    | (g, Grey) `elem` marking = findYellow secret guess marking
    | otherwise = (g, Yellow) : findYellow secret guess marking

-- Main function to mark the guess
markGuess :: String -> String -> Marking
markGuess secret guess
    | length secret /= length guess = error "Secret and guess must be of the same length"
    | otherwise = greenMarks ++ yellowMarks ++ greyMarks
    where
        greenMarks = findGreen secret guess
        yellowMarks = findYellow secret guess greenMarks
        greyMarks = [(g, Grey) | g <- guess, g `notElem` (map fst greenMarks ++ map fst yellowMarks)]

-- Example usage:
-- markGuess "aaabb" "bbxxb" should return [('b',Yellow),('b',Grey),('x',Grey),('x',Grey),('b',Green)]