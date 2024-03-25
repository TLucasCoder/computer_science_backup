-- double x = x + x

summ [] = 0
summ (x : s) = x + summ s

productt :: [Int] -> Int
productt [] = 1
productt (x : a) = x * productt a

quicksort :: [Int] -> [Int]
quicksort [] = []
quicksort (x : xs) = quicksort ls ++ [x] ++ quicksort rs
  where
    ls = [a | a <- xs, a > x]
    rs = [a | a <- xs, a <= x]

quicksortt :: [Int] -> [Int]
quicksortt [] = []
quicksortt (x : xs) = quicksortt ls ++ [x] ++ quicksortt rs
  where
    rs = [a | a <- xs, a > x]
    ls = [a | a <- xs, a < x]

n = a `div` length xs
  where
    a = 10
    xs = [1, 2, 3, 4, 5]

-- Can't use uppercase N as a variable name
-- Need backquotes for div
-- Breaks the layout rule in the where clause - columns must be aligned

second xs = head (tail xs)

swap (x, y) = (y, x)

pair x y = (x, y)

double x = x * 2

palindrome xs = reverse xs == xs

twice f x = f (f x)

--The class Eq is instantiated by all of the basic types as well as Lists and Tuples built from these. However, function types are not an instance of the Eq class. Suggest reasons why this is the case.
-- ans: function types are not structural in the same way as Lists and tuples
-- The constructor for function types is lambda (\x -> e)
-- To compare
-- two such terms for equality we would need to compare the bodies of functions
-- For example
-- add x y = x + y  and add' x y = y + x  would be considered to be unequal.
-- A more natural notion of equality (extensional equality) is to say two functions 
-- are equal if they produce equal outputs on the same inputs. Unfortunately this 
-- notion of equality is not decidable for Haskell, or indeed any sufficiently powerful language.