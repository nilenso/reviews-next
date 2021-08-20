{-# LANGUAGE DataKinds #-}
{-# LANGUAGE DeriveGeneric #-}
{-# LANGUAGE OverloadedStrings #-}
{-# LANGUAGE TypeOperators #-}

module Fixtures.MockOAuth where

import Control.Algebra (Has)
import qualified Control.Carrier.Error.Either as E
import qualified Control.Carrier.Lift as L
import qualified Control.Carrier.Reader as R
import Control.Concurrent
import Control.Monad.Except
import Data.Aeson
import qualified Data.Text as T
import GHC.Generics
import qualified Network.Wai.Handler.Warp as Warp
import Reviews.Effects.OAuth
import Reviews.Types.Common
import Servant
import Test.Tasty
import qualified Test.Tasty.QuickCheck as QC

data GithubAccessToken = GithubAccessToken
  { access_token :: String,
    scope :: String,
    token_type :: String
  }
  deriving (Generic, Show)

instance ToJSON GithubAccessToken

type MockGithubAPI =
  "login" :> "oauth" :> "access_token"
    :> QueryParam "client_id" T.Text
    :> QueryParam "client_secret" T.Text
    :> QueryParam "code" T.Text
    :> QueryParam "redirect_uri" T.Text
    :> Post '[JSON] GithubAccessToken
    :<|> "user"
    :> Get '[JSON] GithubUser

validateClientId :: (Monad m, Has (R.Reader OAuthConfig) sig m) => T.Text -> m Bool
validateClientId cid = (/= cid) <$> R.asks clientId

accessTokenController ::
  ( Has (E.Error ServerError) sig m,
    Has (R.Reader OAuthConfig) sig m,
    Has (L.Lift IO) sig m
  ) =>
  Maybe T.Text ->
  Maybe T.Text ->
  Maybe T.Text ->
  Maybe T.Text ->
  m GithubAccessToken
accessTokenController _ _ _ _ = L.sendIO $ QC.generate randomGithubAccessToken
  where
    randomGithubAccessToken =
      GithubAccessToken
        <$> QC.arbitrary
        <*> pure "user"
        <*> pure "bearer"

getUserController ::
  ( Has (E.Error ServerError) sig m,
    Has (R.Reader OAuthConfig) sig m,
    Has (L.Lift IO) sig m
  ) =>
  m GithubUser
getUserController = L.sendIO $ QC.generate randomGithubUser
  where
    randomGithubUser =
      GithubUser
        <$> pure 1234
        <*> pure "login"
        <*> pure "name"
        <*> pure (Just "email")

mockGithubServer ::
  ( Has (E.Error ServerError) sig m,
    Has (R.Reader OAuthConfig) sig m,
    Has (L.Lift IO) sig m
  ) =>
  ServerT MockGithubAPI m
mockGithubServer = accessTokenController :<|> getUserController

mockGithubApi :: Proxy MockGithubAPI
mockGithubApi = Proxy

githubApp :: OAuthConfig -> Application
githubApp oauthConfig =
  serve mockGithubApi $
    hoistServer
      mockGithubApi
      (toOAuthHandler oauthConfig)
      mockGithubServer

type MockGithubControllerC = E.ErrorC ServerError (R.ReaderC OAuthConfig (L.LiftC IO))

toOAuthHandler :: OAuthConfig -> MockGithubControllerC a -> Handler a
toOAuthHandler oauthConfig = Handler . ExceptT . L.runM . R.runReader oauthConfig . E.runError

withMockGithubApi :: TestTree -> TestTree
withMockGithubApi testTree =
  withResource
    (readConfig "./test.config.dhall" >>= forkIO . Warp.run 3005 . githubApp . githubOAuthConfig)
    killThread
    (const testTree)
