{-# LANGUAGE DeriveGeneric #-}
--SKELETON FILE FOR HANDIN 1 OF COURSEWORK 1 for COMP2209, 2023
--CONTAINS ALL FUNCTIONS REQIURED FOR COMPILATION AGAINST THE TEST SUITE
--MODIFY THE FUNCTION DEFINITIONS WITH YOUR OWN SOLUTIONS
--IMPORTANT : DO NOT MODIFY ANY FUNCTION TYPES
--Julian Rathke, Oct 2023

module Exercises (histogram,renderMaze,markGuess,Check(..),Marking) where

-- Add your own imports here --

-- Exercise A1
histogram :: Int -> [Int] -> [Int]
histogram n xs
    | n <= 0 = error "n is not positive"
    | otherwise  = counting n 0 xs

counting :: Int -> Int -> [Int] -> [Int]
counting n d [] = []
counting n d xs =
    sum [1 | x <- xs, x `div` n == d ] : counting n (d+1) [x | x <- xs , x `div` n /= d ]




-- Exercise A2

renderMaze :: [ ((Int,Int),(Int,Int)) ] -> [String]
--renderMaze [] = error "empty maze"
renderMaze path
    | null path = [] 
    | otherwise =
        [[if  (x, y) `elem` points  then '#' else ' ' |  x<-[0..maxX]] | y<-[0..maxY ]]
        where
            point = [(x_axis,y_axis) |  ((x1,y1),(x2,y2)) <- path , x_axis <-[x1..x2],y_axis<-[y1..y2] ]
            pointss = [(x_axis,y_axis) |  ((x1,y1),(x2,y2)) <- path , x_axis <-[x2..x1],y_axis<-[y2..y1] ]
            points = point ++ pointss
            maxX = maximum $ map (maximum . (\((x1, _), (x2, _)) -> [x1, x2])) path
            maxY = maximum $ map (maximum . (\((_, y1), (_, y2)) -> [y1, y2])) path


        
-- Exercise A3

data Check = Green | Yellow | Grey deriving (Eq,Show,Read)
type Marking = [(Char,Check)]

markGuess  :: String -> String -> Marking
markGuess secret "" = []
markGuess secret l = yellow
    where
        green = greenlist secret l
        secrett = [ x  | (x,(y,z)) <-zip secret green, x/=y ]
        yellow = yellowlist secrett green
    --let yellow = yellowlist 

greenlist :: String -> String -> Marking
greenlist secret guess = [ if front == end then  (end, Green) else (end, Grey) | let zipped = zip secret guess, (front,end) <- zipped ]

-- use recursion
yellowlist :: String -> Marking -> Marking
yellowlist _ [] = []
yellowlist k  ((x,y):rest)
    | y == Grey &&  position/= -1 =  (x,Yellow) : yellowlist (take position k ++ drop (position +1) k) rest
    | otherwise = (x,y) : yellowlist k rest
    where
        position = findElementPosition x k



findElementPosition :: Eq a => a -> [a] ->  Int
findElementPosition _ [] = -1
findElementPosition x (y:ys)
  | x == y = 0
  | findElementPosition x ys == -1 = -1
  | otherwise = findElementPosition x ys + 1