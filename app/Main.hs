{-# LANGUAGE OverloadedStrings #-}
module Main where

import Network.Wai.Handler.Warp (run)
import Reviews.Routes (app)
import Reviews.Types.Config

main :: IO ()
main = do
  config <- readConfig "./config.dhall"
  run 3000 (app config)
