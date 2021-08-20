{-# LANGUAGE DataKinds #-}
{-# LANGUAGE NamedFieldPuns #-}
{-# LANGUAGE OverloadedStrings #-}
{-# LANGUAGE TypeOperators #-}

module Main where

import Network.Wai.Handler.Warp (run)
import Network.Wai.Middleware.RequestLogger (logStdout)
import Reviews
import Reviews.Config

main :: IO ()
main = do
  config <- readConfig "./config.dhall"
  putStrLn $ "Starting webserver on port: " ++ show (port config)
  run (port' config) $ application config
  where
    port' = fromIntegral . port
    application = logStdout . app
