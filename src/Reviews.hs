{-# LANGUAGE DataKinds #-}
{-# LANGUAGE DerivingVia #-}
{-# LANGUAGE FlexibleContexts #-}
{-# LANGUAGE FlexibleInstances #-}
{-# LANGUAGE GeneralizedNewtypeDeriving #-}
{-# LANGUAGE MultiParamTypeClasses #-}
{-# LANGUAGE NamedFieldPuns #-}
{-# LANGUAGE OverloadedStrings #-}
{-# LANGUAGE RankNTypes #-}
{-# LANGUAGE RecordWildCards #-}
{-# LANGUAGE StandaloneDeriving #-}
{-# LANGUAGE TypeOperators #-}
{-# LANGUAGE UndecidableInstances #-}

module Reviews where

import Control.Carrier.Error.Either
import Control.Carrier.Lift
import Control.Carrier.Reader (Reader, ReaderC, runReader)
import qualified Control.Carrier.Reader as R
import Control.Concurrent.STM
import qualified Control.Concurrent.STM as STM
import Control.Monad.Except
import qualified Data.Map as M
import qualified Data.Text as T
import qualified Reviews.Controllers.Auth as Auth
import Reviews.Effects.OAuth
import Reviews.Html
import Reviews.Types.Cache
import Reviews.Types.Common
import Reviews.Types.User
import Servant hiding (NoSuchUser)
import Servant.Auth.Server
import Servant.HTML.Blaze (HTML)
import Text.Blaze.Html5 (Markup)

type API =
  Auth '[Cookie, JWT] Session :> Get '[HTML] Markup
    :<|> "oauth" :> "login" :> "github" :> Get '[JSON] ()
    :<|> ( "oauth"
             :> "callback"
             :> "github"
             :> QueryParam "code" T.Text
             :> QueryParam "state" T.Text
             :> Get '[JSON] ()
         )
    :<|> "assets" :> Raw

type ControllerC =
  ErrorC
    ServerError
    ( GithubOAuthC
        ( ReaderC
            JWTSettings
            ( ReaderC
                CookieSettings
                (ReaderC (TMVar Cache) (ReaderC Config (LiftC IO)))
            )
        )
    )

toHandler :: AppContext -> ControllerC a -> Handler a
toHandler AppContext {..} = do
  Handler
    . ExceptT
    . runM
    . runReader config
    . runReader cache
    . runReader cookieSettings
    . runReader jwtSettings
    . runGithubOAuth httpManager githubProvider
    . runError

index :: AuthResult Session -> Controller Markup
index (Authenticated authToken) = do
  cache <- R.ask
  maybeUser <-
    sendIO . STM.atomically $
      M.lookup (sessionUserId authToken) . usersMap <$> STM.readTMVar cache
  maybe (index NoSuchUser) (return . userHome) maybeUser
index r = do
  sendIO $ print r
  return landingPage

server ::
  ( MonadIO m,
    Has (Reader Config) sig m,
    Has (Reader (TMVar Cache)) sig m,
    Has (Reader JWTSettings) sig m,
    Has (Reader CookieSettings) sig m,
    Has (Error ServerError) sig m,
    Has (OAuth GithubUser) sig m,
    Has (Lift IO) sig m
  ) =>
  ServerT API m
server =
  index
    :<|> Auth.signInWithGithub
    :<|> Auth.oauthCallbackForGithub
    :<|> serveDirectoryWebApp "assets"

api :: Proxy API
api = Proxy

app :: AppContext -> Application
app ctx =
  serveWithContext
    api
    (cookieSettings ctx :. jwtSettings ctx :. EmptyContext)
    $ hoistServerWithContext
      api
      (Proxy :: Proxy '[CookieSettings, JWTSettings])
      (toHandler ctx)
      server
