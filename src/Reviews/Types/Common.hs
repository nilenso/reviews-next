{-# LANGUAGE DeriveAnyClass #-}
{-# LANGUAGE DeriveGeneric #-}
{-# LANGUAGE FunctionalDependencies #-}
{-# LANGUAGE MultiParamTypeClasses #-}
{-# LANGUAGE NamedFieldPuns #-}
{-# LANGUAGE OverloadedStrings #-}
{-# LANGUAGE RankNTypes #-}
{-# LANGUAGE RecordWildCards #-}
{-# LANGUAGE StandaloneDeriving #-}

module Reviews.Types.Common where

import Control.Algebra
import Control.Carrier.Error.Either
import Control.Carrier.Lift
import Control.Carrier.Reader
import Control.Concurrent.STM
import Control.Exception
import Control.Monad.IO.Class
import qualified Data.Text as T
import qualified Data.Text.Encoding as TE
import Dhall
import qualified Network.HTTP.Client.Conduit as Http
import qualified Network.OAuth.OAuth2 as OAuth
import Reviews.Effects.OAuth
import Reviews.Types.Cache
import Servant.Auth.Server
import Servant.Server
import URI.ByteString

data OAuthConfig = OAuthConfig
  { clientId :: T.Text,
    clientSecret :: Maybe T.Text,
    authorizeEndpoint :: T.Text,
    accessTokenEndpoint :: T.Text,
    callback :: Maybe T.Text,
    userDetailsUri :: T.Text
  }
  deriving (Generic, Show)

parseOAuthURI :: Text -> URI
parseOAuthURI =
  either (\err -> throw . toException . AssertionFailed $ "Unable to parse: " ++ show err) id
    . parseURI laxURIParserOptions
    . TE.encodeUtf8

toOAuth2 :: OAuthConfig -> OAuth.OAuth2
toOAuth2 OAuthConfig {..} =
  OAuth.OAuth2
    { oauthClientId = clientId,
      oauthClientSecret = clientSecret,
      oauthOAuthorizeEndpoint = parseOAuthURI authorizeEndpoint,
      oauthAccessTokenEndpoint = parseOAuthURI accessTokenEndpoint,
      oauthCallback = parseOAuthURI <$> callback
    }

instance FromDhall OAuthConfig

newtype Secrets = Secrets {keyBase :: T.Text}
  deriving (Generic, Show)

instance FromDhall Secrets

data Config = Config
  { port :: Natural,
    assetsDir :: FilePath,
    dataDir :: FilePath,
    githubOAuthConfig :: OAuthConfig,
    secrets :: Secrets
  }
  deriving (Generic, Show)

instance FromDhall Config

readConfig :: FromDhall a => T.Text -> IO a
readConfig = input auto

data AppContext = AppContext
  { config :: Config,
    githubProvider :: GithubProvider,
    httpManager :: Http.Manager,
    cache :: TMVar Cache,
    jwtSettings :: JWTSettings,
    cookieSettings :: CookieSettings
  }

createContext :: MonadIO m => FilePath -> m AppContext
createContext configFile = do
  cfg <- liftIO . readConfig . T.pack $ configFile
  let githubOAuth2 = toOAuth2 . githubOAuthConfig $ cfg
  manager <- Http.newManagerSettings Http.defaultManagerSettings
  appCache <- liftIO $ atomically initCache
  jwk <- liftIO generateKey
  return
    AppContext
      { config = cfg,
        githubProvider =
          GithubProvider
            { providerName = "Github",
              fetchUserUri = parseOAuthURI . userDetailsUri . githubOAuthConfig $ cfg,
              oauth2Config = githubOAuth2
            },
        httpManager = manager,
        cache = appCache,
        jwtSettings = defaultJWTSettings jwk,
        cookieSettings =
          defaultCookieSettings
            { cookieXsrfSetting = Just $ defaultXsrfCookieSettings {xsrfExcludeGet = True}
            }
      }

type Controller a =
  forall sig m.
  ( MonadIO m,
    Has (Reader Config) sig m,
    Has (Reader (TMVar Cache)) sig m,
    Has (Reader JWTSettings) sig m,
    Has (Reader CookieSettings) sig m,
    Has (Error ServerError) sig m,
    Has (OAuth GithubUser) sig m,
    Has (Lift IO) sig m
  ) =>
  m a
