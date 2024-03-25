{-# LANGUAGE DeriveGeneric #-}
{-# OPTIONS_GHC -Wno-unrecognised-pragmas #-}
{-# HLINT ignore "Use <$>" #-}
{-# HLINT ignore "Use uncurry" #-}
-- comp2209 Functional Programming Challenges
-- (c) University of Southampton 2021
-- Skeleton code to be updated with your solutions
-- The dummy functions here simply return an arbitrary value that is usually wrong 

-- DO NOT MODIFY THE FOLLOWING LINES OF CODE
module Challenges (TileEdge(..),Tile(..),Puzzle,isPuzzleComplete,
                   Rotation(..),solveCircuit,
                   LExpr(..),Bind(..),prettyPrint,parseLetx,
                   LamExpr(..),letEnc,compareRedn)
                    where

-- Import standard library and parsing definitions from Hutton 2016, Chapter 13
import Parsing
import Data.Array
import Data.Graph
import Data.List
import Data.Maybe
import Control.Applicative
import Data.Char


-- Challenge 1
-- Testing Circuits

data TileEdge = North | East | South | West  deriving (Eq,Ord,Show,Read)
data Tile = Source [ TileEdge ] | Sink [ TileEdge ] | Wire [ TileEdge ]  deriving (Eq,Show,Read)
type Puzzle = [ [ Tile ] ] 
-- puzzle: list of list of tile
  -- tile: an data structure that maybe the 1) source, 2) sink, 3) Wire
isPuzzleComplete :: Puzzle -> Bool
isPuzzleComplete grid 
    | and (checkConnectivity grid 0 0) = 
        case () of
          ()| sinkList==empty || sourceList == empty -> False
            | otherwise ->
              and (checkSinkToSource grid sinkList sourceList) 
              && and (checkSourceToSink grid sinkList sourceList)

    | otherwise = False
    where 
      sinkList = getSinkList grid (head(head grid)) 0 0
      sourceList = getSourceList grid (head(head grid)) 0 0
      
-- the function that checks the connectivity of each tile before moving on 
checkConnectivity :: Puzzle -> Int -> Int -> [Bool]
checkConnectivity grid x y | y > length grid -1 = []
                           | x < head (map length grid)-1 = 
                              and(checkConnectivity' grid (getDirectionList((grid !! y) !! x)) x y )   : checkConnectivity grid (x + 1) y
                           | otherwise = and(checkConnectivity' grid (getDirectionList((grid !! y) !! x)) x y ) : checkConnectivity grid 0 (y+1)

--function to get the tile edge list from 
getDirectionList :: Tile -> [TileEdge]
getDirectionList (Source a) = a
getDirectionList (Wire a) = a
getDirectionList (Sink a) = a

-- helper function of the helper function for checking for the connectivity
checkConnectivity' :: Puzzle-> [TileEdge] -> Int -> Int  -> [Bool]
checkConnectivity' grid [] _ _  = [True]
checkConnectivity' grid (x:xs) xCor yCor 
                          | x == North && (yCor-1)<0 = [False]
                          | x == South && (yCor+1)> length grid -1 = [False]
                          | x == East && (xCor+1)> head (map length grid)-1  = [False]
                          | x == West && (xCor-1)<0 = [False]
checkConnectivity' grid (x:xs) xCor yCor 
                          | x == North = (South `elem` getDirectionList((grid !! (yCor-1)) !! xCor)) : checkConnectivity' grid xs xCor yCor
                          | x == South = (North `elem` getDirectionList((grid !! (yCor+1)) !! xCor)) : checkConnectivity' grid xs xCor yCor
                          | x == East = (West `elem` getDirectionList((grid !! yCor) !! (xCor+1))) : checkConnectivity' grid xs xCor yCor
                          | x == West = (East `elem` getDirectionList((grid !! yCor) !! (xCor-1))) : checkConnectivity' grid xs xCor yCor

-- helper function to get the next tile 
getNextTile :: Puzzle -> Int -> Int -> Tile
getNextTile grid x y 
  | x < (map length grid !! y) && y < length grid = (grid !! y) !! x
  | otherwise = Wire [] --default output if no match 

--helper function to retrieve the list of sink tiles
getSinkList :: Puzzle  -> Tile  -> Int -> Int -> [(Int,Int)]
getSinkList grid tile x y  
  | y > length grid -1 = []
getSinkList grid (Sink a) x y 
  | x >= (map length grid !! y) -1 = (x,y) : getSinkList grid (getNextTile grid 0 (y+1))  0 (y+1)
  | otherwise = (x,y) : getSinkList grid (getNextTile grid (x+1) y) (x+1) y
getSinkList grid _ x y 
  | x >= (map length grid !! y) -1 = getSinkList grid (getNextTile grid 0 (y+1))  0 (y+1)
  | otherwise = getSinkList grid (getNextTile grid (x+1) y)  (x+1) y

--helper function to retrieve the list of source tiles
getSourceList :: Puzzle  -> Tile  -> Int -> Int -> [(Int,Int)]
getSourceList grid tile x y  
  | y > length grid -1 = []
getSourceList grid (Source a) x y 
  | x == (map length grid !! y) -1 = (x,y) : getSourceList grid (getNextTile grid 0 (y+1))  0 (y+1)
  | otherwise = (x,y) : getSourceList grid (getNextTile grid (x+1) y) (x+1) y
getSourceList grid _ x y 
  | x == (map length grid !! y) -1 = getSourceList grid (getNextTile grid 0 (y+1))  0 (y+1)
  | otherwise = getSourceList grid (getNextTile grid (x+1) y)  (x+1) y  

--function that iterate the list of sink tiles to see if every one of them are connected to at least one source
checkSinkToSource :: Puzzle -> [(Int,Int)] -> [(Int,Int)] -> [Bool]
checkSinkToSource _ [] _ = []
checkSinkToSource grid sinkList@(k:ks) sourceList = or a : checkSinkToSource grid ks sourceList 
  where tile = (grid !! snd k) !! fst k
        a = map fst (allSinkToSource grid [] sourceList (getDirectionList tile) (fst k) (snd k))

--function that iterate the list of source tiles to see if every one of them are connected to at least one sink 
checkSourceToSink :: Puzzle -> [(Int,Int)] -> [(Int,Int)] -> [Bool]
checkSourceToSink _ _ []  = []
checkSourceToSink grid sinkList sourceList@(k:ks) = or a : checkSourceToSink grid sinkList ks  
  where tile = (grid !! snd k) !! fst k
        a = map fst (allSourceToSink grid [] sinkList (getDirectionList tile) (fst k) (snd k))

--function that check if individual sink's condition
allSinkToSource :: Puzzle -> [(Int,Int)] -> [(Int,Int)] -> [TileEdge] -> Int -> Int -> [(Bool, (Int,Int) )] 
-- grid -> checked coordinate -> sourceList -> direction list -> x-coordinate -> y-coordinate -> [(isSource, visited coordinate)]
-- stop when having no more direction in the list
allSinkToSource grid checked _ [] x y = [(False,(x,y))]
-- initial value
allSinkToSource grid [] sourceList d x y 
  = (False,(x,y)) : allSinkToSource grid [(x,y)] sourceList d x y
allSinkToSource grid checked sourceList (t:ts) x y 
  | t == North =    
    case () of
                 () | (x, y-1) `notElem` checked && (x,y-1) `elem` sourceList
                        -> n_c x (y-1) x y 
                    | (x, y-1) `notElem` checked 
                        -> n_d x (y-1) x y ++ allSinkToSource grid (checked ++ map snd(n_d x (y-1) x y)) sourceList ts x y
                    | otherwise -> allSinkToSource grid checked sourceList ts x y 
  | t == South = 
    case () of
                 () | (x, y+1) `notElem` checked && (x,y+1) `elem` sourceList
                        -> n_c x (y+1) x y 
                    | (x, y+1) `notElem` checked 
                        -> n_d x (y+1) x y ++ allSinkToSource grid (checked ++ map snd(n_d x (y+1) x y)) sourceList ts x y
                    | otherwise -> allSinkToSource grid checked sourceList ts x y 
  | t == East = 
    case () of
                 () | ((x+1, y) `notElem` checked) && ((x+1, y) `elem` sourceList)
                        -> n_c (x+1) y x y 
                    | (x+1, y) `notElem` checked 
                        -> n_d (x+1) y x y ++ allSinkToSource grid (checked ++ map snd(n_d (x+1) y x y)) sourceList ts x y
                    | otherwise -> allSinkToSource grid checked sourceList ts x y 
  | t == West = 
    case () of
                 () | ((x-1, y) `notElem` checked) && ((x-1,y) `elem` sourceList)
                        -> n_c (x-1) y x y 
                    | (x-1, y) `notElem` checked
                        -> n_d (x-1) y x y ++ allSinkToSource grid (checked ++ map snd(n_d (x-1) y x y)) sourceList ts x y
                    | otherwise -> allSinkToSource grid checked sourceList ts x y 
      where 
        --end when the tile is found, returning the checking result and the coordinate
        n_c xcor ycor a b = [(True, (xcor,ycor)) ]
        n_d xcor ycor a b = (False,(xcor,ycor)) : allSinkToSource grid (checked ++ [(a,b)]) sourceList (getDirectionList((grid !! ycor) !! xcor)) xcor ycor

--function that check if individual source's condition
allSourceToSink :: Puzzle -> [(Int,Int)] -> [(Int,Int)] -> [TileEdge] -> Int -> Int -> [(Bool, (Int,Int) )] -- >(source coor , visited coor )
-- grid -> checked coordinate -> sinkList -> direction list -> x-coordinate -> y-coordinate -> [(isSource, visited coordinate)]
-- stop when having no more direction
allSourceToSink grid checked _ [] x y = [(False,(x,y))]
-- initial value
allSourceToSink grid [] sinkList d x y 
  = (False,(x,y)) : allSourceToSink grid [(x,y)] sinkList d x y

allSourceToSink grid checked sinkList (t:ts) x y 
  | t == North =    
    case () of
                 () | (x, y-1) `notElem` checked && (x,y-1) `elem` sinkList
                        -> n_c x (y-1) x y 
                    | (x, y-1) `notElem` checked 
                        -> n_d x (y-1) x y ++ allSourceToSink grid (checked ++ map snd(n_d x (y-1) x y)) sinkList ts x y
                    | otherwise -> allSourceToSink grid checked sinkList ts x y 
  | t == South = 
    case () of
                 () | (x, y+1) `notElem` checked && (x,y+1) `elem` sinkList
                        -> n_c x (y+1) x y 
                    | (x, y+1) `notElem` checked 
                        -> n_d x (y+1) x y ++ allSourceToSink grid (checked ++ map snd(n_d x (y+1) x y)) sinkList ts x y
                    | otherwise -> allSourceToSink grid checked sinkList ts x y 
  | t == East = 
    case () of
                 () | ((x+1, y) `notElem` checked) && ((x+1, y) `elem` sinkList)
                        -> n_c (x+1) y x y 
                    | (x+1, y) `notElem` checked 
                        -> n_d (x+1) y x y ++ allSourceToSink grid (checked ++ map snd(n_d (x+1) y x y)) sinkList ts x y
                    | otherwise -> allSourceToSink grid checked sinkList ts x y 
  | t == West = 
    case () of
                 () | ((x-1, y) `notElem` checked) && ((x-1,y) `elem` sinkList)
                        -> n_c (x-1) y x y 
                    | (x-1, y) `notElem` checked
                        -> n_d (x-1) y x y ++ allSourceToSink grid (checked ++ map snd(n_d (x-1) y x y)) sinkList ts x y
                    | otherwise -> allSourceToSink grid checked sinkList ts x y 
      where 
        n_c xcor ycor a b = [(True, (xcor,ycor)) ]
        n_d xcor ycor a b = (False,(xcor,ycor)) : allSourceToSink grid (checked ++ [(a,b)]) sinkList (getDirectionList((grid !! ycor) !! xcor)) xcor ycor


-- Challenge 2
-- Solving Circuits
data Rotation = R0 | R90 | R180 | R270 
  deriving (Eq,Show,Read)

solveCircuit :: Puzzle -> Maybe [[ Rotation ]]
solveCircuit = undefined

-- Challenge 3
-- Pretty Printing Let Expressions

data LExpr = Var Int | App LExpr LExpr | Let Bind LExpr LExpr | Pair LExpr LExpr | Fst LExpr | Snd LExpr  | Abs Bind LExpr 
    deriving (Eq,Show,Read)
data Bind = Discard | V Int 
    deriving (Eq,Show,Read)

--function pretty print the expression from LExpr to String
prettyPrint :: LExpr -> String
prettyPrint (Var a) = "x"++ show a 
prettyPrint (App first_e second_e) = prettyPrint first_e ++ " " ++ prettyPrint second_e
prettyPrint (Let bind first_e second_e)
 = "let " ++ printBind bind ++ sugarLetFront ++ "= " ++  prettyPrint sugarLetLast  ++ " in " ++ prettyPrint second_e 
   where 
      sugarLetLast =  getExpression first_e 
      sugarLetFront = getExpression1 first_e
prettyPrint (Pair first_e second_e) = "(" ++ prettyPrint first_e ++ " , " ++ prettyPrint second_e ++ ")" 
prettyPrint (Abs bind e) = "\\" ++ printBind bind ++ getExpression1 e ++ "-> "++ prettyPrint (getExpression e)
prettyPrint (Fst e) = "fst (" ++ prettyPrint e  ++ ")"
prettyPrint (Snd e) = "snd (" ++ prettyPrint e  ++ ")"

--helper function faciliate the translation of the syntatic sugar (abstraction bit)
getExpression :: LExpr -> LExpr
getExpression (Abs a b) = getExpression b
getExpression a = a

getExpression1 :: LExpr -> String
getExpression1 (Abs a b) = printBind a ++ getExpression1 b
getExpression1 a = []

--helper function for those binding variables
printBind :: Bind -> String
printBind Discard = "_ "
printBind (V a) = "x" ++ show a ++ " "


-- Challenge 4 - Parsing Let Expressions

parseLetx :: String -> Maybe LExpr
parseLetx s 
  | null a = Nothing
  | not (null (snd (head a))) = Nothing
  | otherwise = Just (fst (head a))
  where 
    a = parse expr removeSugar1
    removeSugar1 = checkConvert s
    first_chr = head s

-- iterate to see if need to apply the below convert function
checkConvert :: String -> String
checkConvert [] = []
checkConvert inp
  | take 3 inp == "let" || take 3 inp == "Let"   =  take 3 (convertLet inp True True 0) ++ checkConvert (drop 3 (convertLet inp True True 0))
  | take 1 inp == "\\" =   take 1 (convertAbs inp True True 0) ++ checkConvert (drop 1 (convertAbs inp True True 0))
  | otherwise = take 1 inp ++   checkConvert (drop 1 inp)


-- convert the syntatic sugar of abs
convertLet :: String -> Bool -> Bool -> Int -> String
convertLet inp c2 c1 pos 
  | ((inp !! pos) == '=') && c2 = inp
  | c2 = convertLet inp (fst checking) (snd checking) (pos +1)
  | otherwise = take pos inp ++ "= " ++  addLam  (take (eqtPos - pos) (drop pos inp)) 0 True ++ "->" ++ take (length inp - eqtPos) (drop (eqtPos+1) inp)
    where 
      Just eqtPos =  elemIndex '=' inp
      checking = checkXorDiscard (inp !! (pos+1)) c2 c1

-- convert the syntatic sugar of abs

convertAbs :: String -> Bool -> Bool -> Int -> String
convertAbs inp c2 c1 pos
  | (cap == '>') && c2 = inp
  | c2 = convertAbs inp (fst checking) (snd checking)  (pos +1)
  | otherwise = take pos inp ++ addAbs (take (arrowPos - pos) (drop pos inp)) 0 ++ take (length inp - arrowPos) (drop arrowPos inp)
  where 
    cap = inp !! pos
    Just arrowPos = elemIndex '-' inp
    checking = checkXorDiscard (inp !! (pos+1)) c2 c1

addAbs :: String -> Int -> String
addAbs [] _ = []
addAbs inp pos 
  | take 1 inp == "x" || take 1 inp == "_" = " -> \\" ++ take 1 inp ++ addAbs (drop 1 inp) (pos+1) 
  | otherwise = take 1 inp ++ addAbs (drop 1 inp) (pos+1) 

-- converting the syntatic sugar
-- first result bool: second check
-- second result bool: inital check
checkXorDiscard :: Char -> Bool -> Bool -> (Bool,Bool)
checkXorDiscard 'x' True True = (True, False)
checkXorDiscard 'x' True False = (False, False)
checkXorDiscard '_' True True = (True, False)
checkXorDiscard '_' True False = (False, False)
checkXorDiscard _ a b = (a,b)

addLam :: String -> Int -> Bool -> String
addLam "" _ _ = []
addLam inp pos True
  | take 1 inp == "x" = "\\" ++ take 1 inp ++ addLam (drop 1 inp) (pos+1) False
  | take 1 inp == "_" = "\\" ++ take 1 inp ++ addLam (drop 1 inp) (pos+1) False
  | otherwise =  take 1 inp ++ addLam (drop 1 inp) (pos+1) True
addLam inp pos first_check 
  | take 1 inp == "x" = "-> \\" ++ take 1 inp ++ addLam (drop 1 inp) (pos+1) first_check
  | take 1 inp == "_" = "-> \\" ++ take 1 inp ++ addLam (drop 1 inp) (pos+1) first_check
  | otherwise =  take 1 inp ++ addLam (drop 1 inp) (pos+1) first_check

--parse application
appExp :: Parser LExpr
appExp = do 
          cx <- lambdaAtom
          space
          y <- many (lambdaAtom <* space)
          return $ foldl App cx y

lambdaAtom :: Parser LExpr
lambdaAtom = parens expr <|> varExp

letExp :: Parser LExpr
letExp = do
          symbol "let"
          space
          bind <- bindExp
          space
          symbol "="
          space
          x <- expr
          space 
          symbol "in"
          space
          y <- expr
          return  (Let bind x y)
          
bindExp :: Parser Bind
bindExp =  vExp <|> discardExp

-- expression of bind var
vExp :: Parser Bind
vExp = do 
        char 'x'
        x <- integer
        return (V x)

discardExp :: Parser Bind
discardExp = do 
            char '_'
            return Discard


parens :: Parser a -> Parser a
parens p = do
    char '('
    space
    x <- p
    space
    char ')'
    return x

varExp :: Parser LExpr
varExp = do 
          symbol "x"
          x <- integer
          return (Var x)

absExp :: Parser LExpr
absExp = do 
          symbol "\\"
          space
          x <- bindExp
          space
          symbol "->"
          space
          y <- expr
          space
          return (Abs x y)

fstExp :: Parser LExpr
fstExp = do
          symbol "fst"
          symbol "("
          a <- expr
          symbol ")"
          return (Fst a )


sndExp :: Parser LExpr
sndExp = do
          symbol "snd"
          symbol "("
          a <- expr  
          symbol ")"
          return (Snd a )

pairExp :: Parser LExpr
pairExp = do
            symbol "("
            a <- expr
            space
            symbol ","
            space
            b <- expr
            symbol ")"
            return (Pair a b)


expr :: Parser LExpr
expr =   appExp <|> absExp  <|> letExp  <|> pairExp <|> fstExp  <|> sndExp  <|> varExp

-- Challenge 5
-- Let Encoding in Lambda 

data LamExpr = LamVar Int | LamApp LamExpr LamExpr | LamAbs Int LamExpr 
                deriving (Eq, Show, Read)
-- pattern matching the LExpr to turn them into the LamExpr form
letEnc :: LExpr -> LamExpr 
letEnc (Var a) = LamVar a
letEnc (App a b) = LamApp (letEnc a) (letEnc b)
letEnc (Abs bind b) = LamAbs (bindEnc bind (getNameList(letEnc b))) (letEnc b)
letEnc (Let bind e1 e2) = LamApp (LamAbs (bindEnc bind (getNameList (letEnc e2))) (letEnc e2)) (letEnc e1)

letEnc (Pair e1 e2) = LamAbs c ( LamApp (LamApp (LamVar c) (letEnc e1)) (letEnc e2))
  where c = bindEnc Discard (getNameList (letEnc e1) ++ getNameList (letEnc e2))
letEnc (Fst a@(Pair e1 e2)) = LamApp (letEnc a) (LamAbs 0 (LamAbs 1 (LamVar 0))) 
letEnc (Snd a@(Pair e1 e2)) = LamApp (letEnc a) (LamAbs 0 (LamAbs 1 (LamVar 1))) 
letEnc (Fst a) = letEnc a
letEnc (Snd a) = letEnc a

-- the function dealing with binding variables
-- if there is discard symbol, it searches through all the variables to make sure the naming won't interfere other variables
bindEnc :: Bind -> [Int] -> Int
bindEnc (V a) _ = a
bindEnc Discard listOfName = foldl max 0 listOfName + 1

--helper function for getting the name list of the naming of other variables
getNameList :: LamExpr -> [Int] 
getNameList (LamVar a) = [a]
getNameList (LamAbs bind b) = bind : getNameList b
getNameList (LamApp a b) = getNameList a ++ getNameList b


-- Challenge 6
-- Compare Innermost Reduction for Let_x and its Lambda Encoding

------------
-- LAMBDA --
------------

free :: Int -> LamExpr -> Bool
free x (LamVar y) =  x == y
free x (LamAbs y e) | x == y = False
free x (LamAbs y e) | x /= y = free x e
free x (LamApp e1 e2)  = (free x e1) || (free x e2)

rename :: Int -> LamExpr -> Int
rename x e | free (x+1) e = rename (x+1) e
           | otherwise = x+1 

subst :: LamExpr -> Int ->  LamExpr -> LamExpr
subst (LamVar x) y e | x == y = e
subst (LamVar x) y e | x /= y = LamVar x
subst (LamAbs x e1) y e  |  x /= y && not (free x e)  = LamAbs x (subst e1 y e)
subst (LamAbs x e1) y e  |  x /= y &&     (free x e)  = let x' = (rename x e1) in subst (LamAbs x' (subst e1 x (LamVar x'))) y e
subst (LamAbs x e1) y e  | x == y  = LamAbs x e1
subst (LamApp e1 e2) y e = LamApp (subst e1 y e) (subst e2 y e) 

isLamValue :: LamExpr -> Bool
isLamValue (LamVar _) = True
isLamValue (LamAbs _ _) = True
isLamValue _ = False

-- CALL BY VALUE -- 
cbvlam1 :: LamExpr -> Maybe LamExpr
-- Contexts
cbvlam1 (LamApp e1 e2) | not (isLamValue e1) = 
  do e' <- cbvlam1 e1
     return (LamApp e' e2)
cbvlam1 (LamApp e1 e2) | not (isLamValue e2) = 
  do e' <- cbvlam1 e2
     return (LamApp e1 e')
-- Reductions 
cbvlam1 (LamApp (LamAbs x e1) e) | isLamValue e = Just (subst e1 x e)
-- Otherwise terminated or blocked
cbvlam1 _ = Nothing

-- CALL BY NAME --
cbnlam1 :: LamExpr -> Maybe LamExpr
-- Reductions 
cbnlam1 (LamApp (LamAbs x e1) e) = Just (subst e1 x e)
-- Contexts
cbnlam1 (LamApp e1 e2) = 
  do e' <- cbnlam1 e1
     return (LamApp e' e2)
-- Otherwise terminated or blocked
cbnlam1 _ = Nothing

---------
-- LET --
--------- 





compareRedn :: LExpr -> Int -> (Int,Int,Int,Int)
compareRedn  = undefined
