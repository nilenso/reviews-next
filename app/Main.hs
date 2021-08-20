{-# LANGUAGE DataKinds #-}
{-# LANGUAGE OverloadedStrings #-}
{-# LANGUAGE TypeOperators #-}

module Main where

import Network.Wai.Handler.Warp (run)
import Network.Wai.Middleware.RequestLogger (logStdout)
import Reviews.Config
import Reviews.Html
import Servant
import Servant.HTML.Blaze (HTML)
import Text.Blaze.Html5 (Markup, h1)

type API = Get '[HTML] Markup :<|> "assets" :> Raw

index :: Handler Markup
index = return landingPage

server :: Server API
server =
  index :<|> serveDirectoryWebApp "assets"

api :: Proxy API
api = Proxy

app :: Application
app = serve api server

main :: IO ()
main = do
  config <- readConfig "./config.dhall"
  putStrLn $ "Starting webserver on port: " ++ show (port config)
  run (port' config) application
  where
    port' = fromIntegral . port
    application = logStdout app
