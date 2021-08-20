{-# LANGUAGE DataKinds #-}
{-# LANGUAGE NamedFieldPuns #-}
{-# LANGUAGE TypeOperators #-}

module Main where

import Network.Wai.Handler.Warp (run)
import Network.Wai.Middleware.RequestLogger (logStdout)
import Reviews
import Reviews.Types.Common

main :: IO ()
main = do
  ctx <- createContext "./config.dhall"
  putStrLn $ "Starting webserver on port: " ++ show (port' ctx)
  run (port' ctx) $ application ctx
  where
    port' = fromIntegral . port . config
    application = logStdout . app
