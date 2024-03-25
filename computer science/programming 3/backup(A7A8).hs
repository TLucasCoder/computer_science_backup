{-# LANGUAGE DeriveGeneric #-}
--SKELETON FILE FOR HANDIN 3 OF COURSEWORK 1 for COMP2209, 2023
--CONTAINS ALL FUNCTIONS REQIURED FOR COMPILATION AGAINST THE TEST SUITE
--MODIFY THE FUNCTION DEFINITIONS WITH YOUR OWN SOLUTIONS
--IMPORTANT : DO NOT MODIFY ANY FUNCTION TYPES
--Julian Rathke, Oct 2023

-- remember to correct it to Exercises
module Exercises (Expr(..),show,toCNF,resolve,valid) where

data Expr = Var Char | Not Expr | And [Expr] | Or [Expr] 
   deriving (Eq, Ord)

-- Exercise A7
instance Show Expr where
  --show :: Expr -> String


  show exprs = showing 0 exprs ""

showing :: Int -> Expr -> ShowS
showing _ (Var x) = showString [x]
showing _ (Or []) = showString "F"
showing _ ((And [])) = showString "T"
showing lv (Not x) =  showString "~" . showing 3 x  
showing lv (And (x:xs))
        | xs == [] = showing lv x
        | otherwise =  showParen (lv/= 1 && lv/=0) $ showing 3 x . showString" ^ "  . showing 1 (And xs) 
showing lv l@(Or (x:xs))
        | xs == [] = showing lv x
        | otherwise = showParen  (lv/= 2 && lv/=0) $ showing 3 x . showString" v "  . showing 2 (Or xs) 

--show And (x:xs) = 

-- Exercise A8
-- transform an arbitrary propositional logic expression
-- into conjunctive normal form
-- (a conjunction of disjunctions)
-- easier to first convert to negation normal form (i.e. where negation is applied only to variables)
-- repeatedly apply the distributive law wherever a disjunction occurs over a conjunction

toCNF :: Expr -> Expr 
toCNF exprs 
        | isCNF exprs = exprs
        | otherwise = formCNF [] temp
        where 
                temp = toNNF 0 exprs


formCNF :: [Expr] -> Expr -> Expr


formCNF  store (And [Not a]) =  Or [Not a]
formCNF  store (And [Var a]) =  Or [Var a]
formCNF store (Or l@(a:as)) 
        | as == [] = formCNF store a
        | otherwise =  formCNF  (store++[a]) (Or as)

formCNF store (And l@(a:as)) 
        | as == [] =   Or [a]
        --  | otherwise = And  [ Or $ store++[a] ]
        | otherwise =  And  (  formCNF store (a) :  [formCNF store (And as)] )
-- : [formCNF store (And as)]

formCNF  store l@(Not a) =   Or (store++[l])
formCNF  store l@(Var a) =   Or (store++[l])

--Negation Normal Form
toNNF :: Int -> Expr -> Expr


toNNF counting (Not (And l@(a:as)))
        | as == [] = toNNF (counting+1) a
        | otherwise = Or $ toNNF (counting+1) a : [toNNF (counting+1) (Or as)]

toNNF counting (Not (Or l@(a:as)))
        | as == [] = toNNF (counting+1) a
        | otherwise = And $ toNNF (counting+1) a : [toNNF (counting+1) (And as)]

toNNF counting (And l@(a:as)) 
        | as == [] = toNNF counting a
        | otherwise = And $ toNNF counting a : [toNNF counting (And as)]
toNNF counting (Or l@(a:as)) 
        | as == [] = toNNF counting a
        | otherwise = Or $ toNNF counting a : [toNNF counting (Or as)]

toNNF counting (Not exprs) = toNNF (counting+1) exprs
toNNF counting l@(Var exprs)  
        | counting `mod` 2 > 0 = Not l
        | otherwise =  l


isCNF :: Expr -> Bool
isCNF (And es) = all isClause es
isCNF _ = False
    
isClause :: Expr -> Bool
isClause (Or es) = all isLiteral es
isClause _ = False

isLiteral :: Expr -> Bool
isLiteral (Var _) = True
isLiteral (Not (Var _)) = True
isLiteral _ = False

-- Exercise A9

resolve :: Expr -> Expr -> Expr
resolve = undefined

valid :: [Expr] -> Expr -> Bool
valid = undefined
