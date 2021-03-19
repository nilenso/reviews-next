{-# LANGUAGE DataKinds #-}
{-# LANGUAGE OverloadedStrings #-}
{-# LANGUAGE TypeOperators #-}


module Reviews.Routes where

import Servant
import Servant.HTML.Blaze (HTML)
import Text.Blaze.Html5

import Reviews.Controllers.Pages

type API = Get '[HTML] Markup
  :<|> "assets" :> Raw

server :: Server API
server = index :<|> serveDirectoryWebApp "assets"

api :: Proxy API
api = Proxy

app :: Application
app = serve api server
