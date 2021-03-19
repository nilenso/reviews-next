module Main where

import Network.Wai.Handler.Warp (run)
import Reviews.Config
import Reviews.Routes
import Reviews.Database

main :: IO ()
main = run 8080 app

