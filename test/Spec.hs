import qualified Reviews.AuthSpec as AuthSpec
import Test.Tasty

main :: IO ()
main = defaultMain tests

tests :: TestTree
tests = testGroup "All Tests" [AuthSpec.tests]
