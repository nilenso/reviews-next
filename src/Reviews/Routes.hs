{-# LANGUAGE DataKinds #-}
{-# LANGUAGE FlexibleInstances #-}
{-# LANGUAGE GeneralizedNewtypeDeriving #-}
{-# LANGUAGE MultiParamTypeClasses #-}
{-# LANGUAGE OverloadedStrings #-}
{-# LANGUAGE TypeOperators #-}
{-# LANGUAGE UndecidableInstances #-}

module Reviews.Routes where

import qualified Control.Algebra as Alg
import Control.Carrier.Reader
import Servant 
import Servant.HTML.Blaze (HTML)
import Text.Blaze.Html5

import Reviews.Controllers.Pages
import Reviews.Controllers.Users
import Reviews.Types.Config
import Reviews.Types.Common
import Reviews.Types.User
import Control.Monad.Except

instance (Algebra sig (ExceptT ServerError IO)) => Alg.Algebra sig Servant.Handler where
  alg hdl sig ctx = Handler $ Alg.alg (runHandler' . hdl) sig ctx

type API = Get '[HTML] Markup
  :<|> "users" :> Get '[HTML, JSON] [User]
  :<|> "assets" :> Raw

server 
  :: (Has (Reader Config) sig m) 
  => ServerT API m
server = index 
  :<|> listUsers
  :<|> serveDirectoryWebApp "assets"

api :: Proxy API
api = Proxy

app :: Config -> Application
app config = serve api $ hoistServer api toHandler server
  where toHandler = runReader config
