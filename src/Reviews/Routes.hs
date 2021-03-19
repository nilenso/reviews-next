{-# LANGUAGE DataKinds #-}
{-# LANGUAGE OverloadedStrings #-}
{-# LANGUAGE TypeOperators #-}


module Reviews.Routes where

import Servant
import Servant.HTML.Blaze (HTML)
import Text.Blaze.Html5

import Reviews.Controller.Pages

type API = Get '[HTML] Markup

server :: Server API
server = index

api :: Proxy API
api = Proxy

app :: Application
app = serve api server
