{-# LANGUAGE OverloadedStrings #-}
module Main where

import Network.Wai.Handler.Warp (run)
import Network.Wai.Middleware.RequestLogger (logStdout)

import Reviews.Routes (app)
import Reviews.Types.Config

main :: IO ()
main = do
  config <- readConfig "./config.dhall"
  putStrLn $ "Starting reviews-next on port: " ++ show (port config)
  run 3000 $ application config
  where
  application = logStdout . app
