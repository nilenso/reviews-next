{-# LANGUAGE DeriveGeneric #-}
{-# LANGUAGE RecordWildCards #-}

module Reviews.Types.User where

import Control.Monad.IO.Class (MonadIO, liftIO)
import Data.Aeson
import qualified Data.Text as T
import Data.Time
import qualified Data.UUID as UUID
import qualified Data.UUID.V4 as UUID
import GHC.Generics (Generic)
import qualified Network.OAuth.OAuth2 as OAuth
import qualified Reviews.Effects.OAuth as OAuth
import Servant.Auth.Server (FromJWT, ToJWT)

type UserId = UUID.UUID

data OAuthProfile
  = GithubProfile OAuth.GithubUser OAuth.OAuth2Token
  deriving (Generic, Show)

data User = User
  { userId :: UserId,
    fullname :: T.Text,
    username :: T.Text,
    oauthProfiles :: [OAuthProfile]
  }
  deriving (Generic, Show)

data Session = Session
  { sessionId :: UUID.UUID,
    sessionUserId :: UserId,
    startedAt :: UTCTime
  }
  deriving (Generic, Show)

instance FromJSON Session

instance ToJSON Session

instance FromJWT Session

instance ToJWT Session

createFromGithubUser ::
  MonadIO m =>
  OAuth.OAuth2Token ->
  OAuth.GithubUser ->
  m User
createFromGithubUser oauthToken gUser@OAuth.GithubUser {..} = do
  uuid <- liftIO UUID.nextRandom
  return $
    User
      { userId = uuid,
        fullname = github_name,
        username = github_login,
        oauthProfiles = [GithubProfile gUser oauthToken]
      }

newSession :: User -> IO Session
newSession User {..} =
  Session
    <$> UUID.nextRandom
    <*> pure userId
    <*> getCurrentTime
