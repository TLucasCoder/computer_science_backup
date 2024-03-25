import Test.HUnit
import Challenges
    ( Bind(Discard, V),
      LExpr(Var, Let, Abs, Fst, Snd, Pair, App),
      Tile(Source, Sink, Wire),
      TileEdge(North, South, East, West),
      LamExpr(LamVar,LamApp,LamAbs),
      isPuzzleComplete,
      prettyPrint,letEnc,
      parseLetx )
-- run command : runTestTT <tests>

-- testing for challenge 1

test1 = TestCase ( assertBool "The puzzle should return True" (isPuzzleComplete [[ Sink [East], Wire [East, West], Source[West] ] ]))
test2 = TestCase (assertBool "The puzzle should return False"  (not (isPuzzleComplete [[Wire []]])))
test3 = TestCase (assertBool "The puzzle should return False"  (not (isPuzzleComplete [[Wire [East, West], Source [North] ],[Sink [North], Wire []]])))
test4 = TestCase (assertBool "The puzzle should return True" (isPuzzleComplete [[Wire [], Source [South]] , [Sink[East], Wire [West, North]]]))
test5 = TestCase (assertBool "The puzzle should return False" (not(isPuzzleComplete [[Wire [], Source [South]] , [Sink[East], Wire [West, North,South,East]]])))
test6 = TestCase (assertBool "The puzzle should return True" (isPuzzleComplete [[Source [East], Sink [West]] , [Source [East], Sink [West]] ]))
test7 = TestCase (assertBool "The puzzle should return False" (not(isPuzzleComplete [[Wire [], Source [South]] , [ Wire [East,West], Source [North,West]]])))
test8 = TestCase (assertBool "The puzzle should return False" (not(isPuzzleComplete [[Wire [], Sink [South]] , [Sink[East], Sink [North,West]]])))
test9 = TestCase (assertBool "The puzzle should return False"  
    (not(isPuzzleComplete[ [ Wire [North,West] , Wire [North,South] , Source [North] ], 
    [ Wire [North,West], Wire [East,West], Wire [North,East] ], 
    [ Sink [West] , Wire [North,South] , Wire [North,West] ] ] )))
test10 = TestCase (assertBool "The puzzle should return True" 
    (isPuzzleComplete [ [ Wire [East, South], Wire [East, West], Source [West] ], 
    [Wire[East, North], Wire[East, West], Wire[West, South] ], 
    [ Sink [East], Wire[East, West], Wire[West, North] ] ]))
test11 = TestCase (assertBool "The puzzle should return False" 
    (not(isPuzzleComplete [ [ Wire [East, South], Source [East, West], Source [West,South] ], 
                        [Wire[East, North], Source[East, West], Wire[West, North] ], 
                        [ Sink [East], Wire[East, West], Source[West] ] ])))
test12 = TestCase (assertBool "The puzzle should return True" 
    (isPuzzleComplete [ [ Wire [East, South], Source [East, West,South], Source [West,South] ], 
                        [Wire[East, North,South], Source[East, West,South,North], Wire[West, North,South] ], 
                        [ Sink [East,North], Wire[East, West,North], Source[West, North] ] ]))
testChallenge1 = TestList 
    [TestLabel "Test 1" test1, 
    TestLabel "Test 2" test2,TestLabel "Test 3" test3,TestLabel "Test 4" test4,TestLabel "Test 5" test5,
    TestLabel "Test 6" test6,TestLabel "Test 7" test7,TestLabel "Test 8" test8,TestLabel "Test 9" test9,TestLabel "Test 10" test10,TestLabel "Test 11" test11,
    TestLabel "Test 12" test12]
-- testing for challenge 3
test01 = TestCase (assertEqual "prettyPrint:  "  "(\\x1 -> x1) \\x1 -> x1" (prettyPrint (App (Abs (V 1) (Var 1)) (Abs (V 1) (Var 1))) ))
test02 = TestCase (assertEqual "prettyPrint:  "  "let _ = x0 in \\x1 -> x1 \\x1 -> x1"
     (prettyPrint (Let Discard (Var 0) (Abs (V 1) (App (Var 1) (Abs (V 1) (Var 1)))) )))
test03 = TestCase (assertEqual "prettyPrint:  "  "\\x1 _ x2 -> x2 x1" (prettyPrint (Abs (V 1) (Abs Discard (Abs (V 2) (App (Var 2 ) (Var 1 ) ) ) )) ))
test04 = TestCase (assertEqual "prettyPrint:  "  "x2 \\x1 _ -> x1" (prettyPrint (App (Var 2) (Abs (V 1) (Abs Discard (Var 1)))) ))
test05 = TestCase (assertEqual "prettyPrint: "  "let x2 x3 _ = x0 in fst ((x2 x4 x5 , x2 x5 x4)) snd ((x2 x4 x5 , x2 x5 x4))" 
    (prettyPrint (Let (V 2) (Abs (V 3) (Abs Discard (Var 0))) (App (Fst (Pair (App (App (Var 2) (Var 4)) (Var 5)) (App (App (Var 2) (Var 5)) (Var 4)))) 
        (Snd (Pair (App (App (Var 2) (Var 4)) (Var 5)) (App (App (Var 2) (Var 5)) (Var 4)))))) ) )

test06 = TestCase (assertEqual "prettyPrint: " "let x0 = x1 in x2 x3 x4" (prettyPrint(Let (V 0) (Var 1) (App (App (Var 2) (Var 3)) (Var 4) ))))
test07 = TestCase (assertEqual "prettyPrint: " "let x0 _ x2 = x2 x3 in fst ((\\x1 _ -> x4 , \\x3 -> x4))" 
    (prettyPrint(Let (V 0) (Abs Discard (Abs (V 2) (App (Var 2) (Var 3)))) (Fst (Pair (Abs(V 1) (Abs Discard (Var 4) )) (Abs (V 3) (Var 4)))))))
testChallenge3 = TestList 
    [TestLabel "Test 01" test1,TestLabel "Test 02" test02,TestLabel "Test 03" test03,TestLabel "Test 04" test04 ,
        TestLabel "Test 05" test05,TestLabel "Test 06" test06,TestLabel "Test 07" test07  ]


-- testing for challenge 4
-- test for the parser
test_1 = TestCase $ assertEqual "Test case 1" (Just (App (Var 1) (App (Var 2) (Var 3)))) (parseLetx "x1 (x2 x3)" )
test_2 = TestCase $ assertEqual "Test case 2" (Just (App (App (Var 1) (Var 2)) (Var 3))) (parseLetx "x1 x2 x3" )
test_3 = TestCase $ assertEqual "Test case 3" (Just (Let (V 1) (Abs (V 3) (Var 2)) (App (Var 1) (Var 2)))) (parseLetx "let x1 x3 = x2 in x1 x2" )
test_4 = TestCase $ assertEqual "Test case 4" (Just (Let (V 1) (Abs Discard (Abs (V 3) (Var 3)))
    (Abs (V 3) (App (App (Var 1) (Var 3)) (Var 3))))) (parseLetx "let x1 _ x3 = x3 in \\x3 -> x1 x3 x3" )
test_5 = TestCase $ assertEqual "Test case 5" (Just (Let (V 0) (Abs Discard (Abs (V 2) (App (Var 2) (Var 3)))) 
    (Fst (Pair (Abs(V 1) (Abs Discard (Var 4) )) (Abs (V 3) (Var 4)))))) (parseLetx "let x0 _ x2 = x2 x3 in fst ((\\x1 _ -> x4 , \\x3 -> x4))")
test_6 = TestCase $ assertEqual "Test case 6" (Just (Let (V 2) (Abs (V 3) (Abs Discard (Var 0))) 
    (App (Fst (Pair (App (App (Var 2) (Var 4)) (Var 5)) (App (App (Var 2) (Var 5)) (Var 4)))) (Snd (Pair (App (App (Var 2) (Var 4)) (Var 5)) 
    (App (App (Var 2) (Var 5)) (Var 4))))) )) (parseLetx "let x2 x3 _ = x0 in fst ((x2 x4 x5 , x2 x5 x4)) snd ((x2 x4 x5 , x2 x5 x4))")
-- test for the converter removing the syntatic sugar
testChallenge4 = TestList 
    [TestLabel "Test 01" test_1 ,TestLabel "Test 02" test_2,TestLabel "Test 03" test_4,TestLabel "Test 04" test_4,
    TestLabel "Test 05" test_5,TestLabel "Test 06" test_6]

-- testing for challenge 5
test__1 = TestCase $ assertEqual "Test case 1" (LamApp (LamAbs 2 (LamAbs 1
    (LamVar 1))) (LamAbs 1 (LamVar 1))) (letEnc (Let Discard (Abs (V 1) (Var 1)) (Abs (V 1) (Var 1))) )
test__2 = TestCase $ assertEqual "Test case 2" (LamApp (LamAbs 4 (LamApp (LamApp (LamVar 4) (LamAbs 2 (LamVar 2))) 
    (LamAbs 3 (LamVar 2)))) (LamAbs 0 (LamAbs 1 (LamVar 0)))) (letEnc (Fst (Pair (Abs (V 1) (Var 1)) (Abs Discard (Var 2)))) )
testChallenge5 = TestList 
    [TestLabel "Test 01" test__1,TestLabel "Test 02" test__2 ]