{-# LANGUAGE DataKinds #-}
{-# LANGUAGE NamedFieldPuns #-}
{-# LANGUAGE OverloadedStrings #-}
{-# LANGUAGE TypeOperators #-}

module Reviews where

import Control.Monad.Except
import Control.Monad.Reader
import qualified Data.ByteString.Char8 as BSC
import Reviews.Config
import Reviews.Html
import Servant
import Servant.HTML.Blaze (HTML)
import Text.Blaze.Html5 (Markup)

type API =
  Get '[HTML] Markup
    :<|> "sign-in" :> Get '[JSON] ()
    :<|> "assets" :> Raw

type HandlerM = ExceptT ServerError (Reader Config)

toHandler :: Config -> HandlerM a -> Handler a
toHandler config m =
  case runReader (runExceptT m) config of
    Right result -> return result
    Left err -> throwError err

index :: HandlerM Markup
index = return landingPage

signIn :: HandlerM ()
signIn = do
  Config {github = GithubOAuth {clientID, loginURL}} <- ask
  let redirectURL = BSC.pack $ loginURL ++ "?client_id=" ++ clientID
  throwError err302 {errHeaders = [("Location", redirectURL)]}

server :: ServerT API HandlerM
server =
  index
    :<|> signIn
    :<|> serveDirectoryWebApp "assets"

api :: Proxy API
api = Proxy

app :: Config -> Application
app config = serve api $ hoistServer api (toHandler config) server
