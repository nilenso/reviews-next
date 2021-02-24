{-# LANGUAGE DataKinds #-}
{-# LANGUAGE OverloadedStrings #-}
{-# LANGUAGE TypeOperators #-}


module Reviews.Routes where

import qualified Data.Text as T
import Servant
import Servant.HTML.Blaze (HTML)

type API = Get '[HTML] T.Text

server :: Server API
server = return "Hello World!"

api :: Proxy API
api = Proxy

app :: Application
app = serve api server
