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
            () | not(null sinkList) && not(null sourceList) -> checkSinkToSource grid sinkList sourceList 
               | otherwise -> False
    | otherwise = False
    where 
      sinkList = getSinkList grid (head(head grid)) 0 0
      sourceList = getSourceList grid (head(head grid)) 0 0

checkConnectivity :: Puzzle -> Int -> Int -> [Bool]
-- x -> horizontal, y -> vertical
checkConnectivity grid x y | y > length grid -1 = []
                           | x < head (map length grid)-1 = and(checkConnectivity' grid (getTileList((grid !! y) !! x)) x y )   : checkConnectivity grid (x + 1) y
                           | otherwise = and(checkConnectivity' grid (getTileList((grid !! y) !! x)) x y ) : checkConnectivity grid 0 (y+1)

--function to get the tile edge list from 
getTileList :: Tile -> [TileEdge]
getTileList (Source a) = a
getTileList (Wire a) = a
getTileList (Sink a) = a

checkConnectivity' :: Puzzle-> [TileEdge] -> Int -> Int  -> [Bool]
checkConnectivity' grid [] _ _  = [True]
checkConnectivity' grid (x:xs) xCor yCor 
                          | x == North && (yCor-1)<0 = [False]
                          | x == South && (yCor+1)> length grid -1 = [False]
                          | x == East && (xCor+1)> head (map length grid)-1  = [False]
                          | x == West && (xCor-1)<0 = [False]
checkConnectivity' grid (x:xs) xCor yCor 
                          | x == North = (South `elem` getTileList((grid !! (yCor-1)) !! xCor)) : checkConnectivity' grid xs xCor yCor
                          | x == South = (North `elem` getTileList((grid !! (yCor+1)) !! xCor)) : checkConnectivity' grid xs xCor yCor
                          | x == East = (West `elem` getTileList((grid !! yCor) !! (xCor+1))) : checkConnectivity' grid xs xCor yCor
                          | x == West = (East `elem` getTileList((grid !! yCor) !! (xCor-1))) : checkConnectivity' grid xs xCor yCor

-- helper function to get the next tile 
getNextTile :: Puzzle -> Int -> Int -> Tile
getNextTile grid x y 
  | x < (map length grid !! y) && y < length grid = (grid !! y) !! x
  | otherwise = Wire [] --default output if no match 

getSinkList :: Puzzle  -> Tile  -> Int -> Int -> [(Int,Int)]
getSinkList grid tile x y  
  | y > length grid -1 = []
getSinkList grid (Sink a) x y 
  | x >= (map length grid !! y) -1 = (x,y) : getSinkList grid (getNextTile grid 0 (y+1))  0 (y+1)
  | otherwise = (x,y) : getSinkList grid (getNextTile grid (x+1) y) (x+1) y
getSinkList grid _ x y 
  | x >= (map length grid !! y) -1 = getSinkList grid (getNextTile grid 0 (y+1))  0 (y+1)
  | otherwise = getSinkList grid (getNextTile grid (x+1) y)  (x+1) y

getSourceList :: Puzzle  -> Tile  -> Int -> Int -> [(Int,Int)]
getSourceList grid tile x y  
  | y > length grid -1 = []
getSourceList grid (Source a) x y 
  | x == (map length grid !! y) -1 = (x,y) : getSourceList grid (getNextTile grid 0 (y+1))  0 (y+1)
  | otherwise = (x,y) : getSourceList grid (getNextTile grid (x+1) y) (x+1) y
getSourceList grid _ x y 
  | x == (map length grid !! y) -1 = getSourceList grid (getNextTile grid 0 (y+1))  0 (y+1)
  | otherwise = getSourceList grid (getNextTile grid (x+1) y)  (x+1) y  



checkSinkToSource :: Puzzle -> [(Int, Int)] -> [(Int, Int)] -> Bool
checkSinkToSource grid sinkList sourceList 
  | length (sourceList++sinkList) == sourceFind = True  
  | otherwise = False
  where 
    sourceFind =  length $ filter id $ map fst (allSinkToSource grid [] (sourceList++sinkList) (getTileList firstSink) (fst temp ) (snd temp)  )
    firstSink = (grid !! snd temp  ) !! fst temp 
    temp = head sinkList

    
-- later change back to sourceList + 
allSinkToSource :: Puzzle -> [(Int,Int)] -> [(Int,Int)] -> [TileEdge] -> Int -> Int -> [(Bool, (Int,Int) )] -- >(source coor , visited coor )
-- input the point if 
-- input nothing when run out of move
allSinkToSource grid checked _ [] _ _ = []
allSinkToSource grid [] sourceList d x y 
  | (grid !! y) !! x == Wire d  =  (False,(x,y)) : allSinkToSource grid [(x,y)] sourceList d x y
  | otherwise = (True,(x,y)) : allSinkToSource grid [(x,y)] sourceList d x y 
allSinkToSource grid checked sourceSinkList (t:ts) x y 
  | t == North =    
    case () of
                 () | (x, y-1) `notElem` checked && (x,y-1) `elem` sourceSinkList
                        -> n_c x (y-1) x y ++ allSinkToSource grid (checked ++ map snd(n_c x (y-1) x y)) sourceSinkList ts x y
                    | (x, y-1) `notElem` checked 
                        -> n_d x (y-1) x y ++ allSinkToSource grid (checked ++ map snd(n_d x (y-1) x y)) sourceSinkList ts x y
                    | otherwise -> allSinkToSource grid checked sourceSinkList ts x y 
  | t == South = 
    case () of
                 () | (x, y+1) `notElem` checked && (x,y+1) `elem` sourceSinkList
                        -> n_c x (y+1) x y ++ allSinkToSource grid (checked ++ map snd(n_c x (y+1) x y)) sourceSinkList ts x y
                    | (x, y+1) `notElem` checked 
                        -> n_d x (y+1) x y ++ allSinkToSource grid (checked ++ map snd(n_d x (y+1) x y)) sourceSinkList ts x y
                    | otherwise -> allSinkToSource grid checked sourceSinkList ts x y 
  | t == East = 
    case () of
                 () | ((x+1, y) `notElem` checked) && ((x+1, y) `elem` sourceSinkList)
                        -> n_c (x+1) y x y ++ allSinkToSource grid (checked ++ map snd(n_c (x+1) y x y)) sourceSinkList ts x y
                    | (x+1, y) `notElem` checked 
                        -> n_d (x+1) y x y ++ allSinkToSource grid (checked ++ map snd(n_d (x+1) y x y)) sourceSinkList ts x y
                    | otherwise -> allSinkToSource grid checked sourceSinkList ts x y 
  | t == West = 
    case () of
                 () | ((x-1, y) `notElem` checked) && ((x-1,y) `elem` sourceSinkList)
                        -> n_c (x-1) y x y ++ allSinkToSource grid (checked ++ map snd(n_c (x-1) y x y)) sourceSinkList ts x y
                    | (x-1, y) `notElem` checked
                        -> n_d (x-1) y x y ++ allSinkToSource grid (checked ++ map snd(n_d (x-1) y x y)) sourceSinkList ts x y
                    | otherwise -> allSinkToSource grid checked sourceSinkList ts x y 
      where 
        n_c xcor ycor a b = (True, (xcor,ycor)) : allSinkToSource grid (checked ++ [(a,b)]) sourceSinkList (getTileList((grid !! ycor ) !!xcor)) xcor ycor
        n_d xcor ycor a b = (False,(xcor,ycor)) : allSinkToSource grid (checked ++ [(a,b)]) sourceSinkList (getTileList((grid !! ycor) !! xcor)) xcor ycor


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

prettyPrint :: LExpr -> String
prettyPrint (Var a) = "x"++ show a 
prettyPrint (App first_e second_e)
  | checkVar first_e = prettyPrint first_e ++ " " ++ prettyPrint second_e
  | otherwise = "(" ++ prettyPrint first_e ++ ")" ++ " " ++ prettyPrint second_e

prettyPrint (Let bind first_e second_e)
 = "let " ++ printBind bind ++ sugarLetFront ++ "= " ++  prettyPrint sugarLetLast  ++ " in " ++ prettyPrint second_e 
   where 
      sugarLetLast =  getExpression first_e 
      sugarLetFront = getExpression1 first_e


prettyPrint (Pair first_e second_e) = "(" ++ prettyPrint first_e ++ "," ++ prettyPrint second_e ++ ")" 

prettyPrint (Abs bind e) = "\\" ++ printBind bind ++ getExpression1 e ++ "-> "++ prettyPrint (getExpression e)


prettyPrint (Fst e) = "fst(" ++ prettyPrint e  ++ ")"
prettyPrint (Snd e) = "snd(" ++ prettyPrint e  ++ ")"

checkVar :: LExpr -> Bool
checkVar (Var a) = True
checkVar _ = False

getExpression :: LExpr -> LExpr
getExpression (Abs a b) = getExpression b
getExpression a = a
getExpression1 :: LExpr -> String
getExpression1 (Abs a b) = printBind a ++ getExpression1 b
getExpression1 a = []

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
    --a = parse expr s
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
          a <- pairExp
          symbol ")"
          return (Fst a )


sndExp :: Parser LExpr
sndExp = do
          symbol "snd"
          symbol "("
          a <- pairExp  
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
expr =   appExp <|> letExp <|> absExp  <|> varExp  <|> pairExp <|> fstExp  <|> sndExp 







-- Challenge 5
-- Let Encoding in Lambda 

data LamExpr = LamVar Int | LamApp LamExpr LamExpr | LamAbs Int LamExpr 
                deriving (Eq, Show, Read)

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



bindEnc :: Bind -> [Int] -> Int
bindEnc (V a) _ = a
bindEnc Discard listOfName = foldl max 0 listOfName + 1

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

eval1cbn :: LamExpr -> LamExpr
eval1cbn (LamAbs x e) = LamAbs x e
eval1cbn (LamApp (LamAbs x e1) e2) = subst e1 x e2
eval1cbn (LamApp e1 e2) = LamApp (eval1cbn e1) e2

reductions :: (LamExpr -> LamExpr) -> (LamExpr -> [(LamExpr, LamExpr)])
reductions ssev e = [p | p <- zip evals (tail evals)]
  where evals = iterate ssev e

eval :: (LamExpr -> LamExpr) -> LamExpr -> [LamExpr]
--eval ssev = fst . head .dropWhile (uncurry (/=) ) . reductions ssev
eval ssev = (map fst) . dropWhile (uncurry (/=) ) . reductions ssev

trace :: (LamExpr -> LamExpr) -> LamExpr -> [LamExpr]
trace ssev = (map fst) . takeWhile (uncurry (/=)) . reductions ssev

traces :: (LamExpr -> LamExpr) -> LamExpr -> [(LamExpr, LamExpr)]
traces ssev = dropWhile (uncurry (/=) ) . reductions ssev 

evalcbn = eval eval1cbn
tracecbn = trace eval1cbn

test1 = traces eval1cbn
---------
-- LET --
--------- 





compareRedn :: LExpr -> Int -> (Int,Int,Int,Int)
compareRedn letExp upBound = undefined
  where 