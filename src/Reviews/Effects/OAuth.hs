{-# LANGUAGE DeriveGeneric #-}
{-# LANGUAGE FlexibleContexts #-}
{-# LANGUAGE FlexibleInstances #-}
{-# LANGUAGE GADTs #-}
{-# LANGUAGE GeneralizedNewtypeDeriving #-}
{-# LANGUAGE KindSignatures #-}
{-# LANGUAGE MultiParamTypeClasses #-}
{-# LANGUAGE TypeOperators #-}
{-# LANGUAGE UndecidableInstances #-}

module Reviews.Effects.OAuth where

import Control.Algebra
import Control.Carrier.Reader
import Control.Monad.IO.Class (MonadIO, liftIO)
import Data.Aeson
import qualified Data.ByteString as BS
import qualified Data.ByteString.Lazy as BSL
import Data.Kind (Type)
import Data.Proxy
import qualified Data.Text as T
import GHC.Generics (Generic)
import qualified Network.HTTP.Client.Conduit as Http
import qualified Network.OAuth.OAuth2 as OAuth
import qualified Network.OAuth.OAuth2.TokenRequest as TokenRequest
import qualified URI.ByteString as URI

data GithubUser = GithubUser
  { github_id :: Integer,
    github_login :: T.Text,
    github_name :: T.Text,
    github_email :: Maybe T.Text
  }
  deriving (Generic, Show)

instance FromJSON GithubUser where
  parseJSON = genericParseJSON defaultOptions {fieldLabelModifier = drop 7}

instance ToJSON GithubUser where
  toEncoding = genericToEncoding defaultOptions {fieldLabelModifier = drop 7}

data GithubProvider = GithubProvider
  { providerName :: String,
    fetchUserUri :: URI.URIRef URI.Absolute,
    oauth2Config :: OAuth.OAuth2
  }

data OAuth user (m :: Type -> Type) k where
  RedirectToLoginURI ::
    Proxy user ->
    [(BS.ByteString, BS.ByteString)] ->
    OAuth user m BS.ByteString
  FetchToken ::
    Proxy user ->
    T.Text ->
    OAuth user m (OAuth.OAuth2Result TokenRequest.Errors OAuth.OAuth2Token)
  FetchUser ::
    OAuth.OAuth2Token ->
    OAuth user m (Either BSL.ByteString user)

newtype GithubOAuthC m a = GithubOAuthC {runGithubOAuthC :: ReaderC GithubProvider (ReaderC Http.Manager m) a}
  deriving (Applicative, Functor, Monad, MonadIO)

instance (MonadIO m, Algebra sig m) => Algebra (OAuth GithubUser :+: sig) (GithubOAuthC m) where
  alg hdl sig ctx = GithubOAuthC $
    case sig of
      L (RedirectToLoginURI _ params) -> (<$ ctx) <$> asks (createCodeUri params . oauth2Config)
      L (FetchToken _ code) -> do
        manager <- ask
        provider <- ask
        let action = OAuth.fetchAccessToken manager (oauth2Config provider) $ OAuth.ExchangeToken code
        (<$ ctx) <$> liftIO action
      L (FetchUser token) -> do
        manager <- ask
        provider <- ask
        let action = OAuth.authGetJSON manager (OAuth.accessToken token) (fetchUserUri provider)
        (<$ ctx) <$> liftIO action
      R other -> alg (runGithubOAuthC . hdl) (R . R $ other) ctx

createCodeUri :: [(BS.ByteString, BS.ByteString)] -> OAuth.OAuth2 -> BS.ByteString
createCodeUri params =
  URI.serializeURIRef'
    . OAuth.appendQueryParams params
    . OAuth.authorizationUrl

runGithubOAuth :: Http.Manager -> GithubProvider -> GithubOAuthC m a -> m a
runGithubOAuth httpManager provider =
  runReader httpManager
    . runReader provider
    . runGithubOAuthC

redirectToGithubLogin ::
  Has (OAuth GithubUser) sig m =>
  [(BS.ByteString, BS.ByteString)] ->
  m BS.ByteString
redirectToGithubLogin = send . RedirectToLoginURI (Proxy :: Proxy GithubUser)

fetchGithubToken ::
  Has (OAuth GithubUser) sig m =>
  T.Text ->
  m (OAuth.OAuth2Result TokenRequest.Errors OAuth.OAuth2Token)
fetchGithubToken = send . FetchToken (Proxy :: Proxy GithubUser)

fetchGithubUser ::
  Has (OAuth GithubUser) sig m =>
  OAuth.OAuth2Token ->
  m (Either BSL.ByteString GithubUser)
fetchGithubUser = send . FetchUser
