{-# LANGUAGE DeriveGeneric #-}

module Reviews.Config where

import Data.Text as T
import Dhall

data GithubOAuth = GithubOAuth
  { clientID :: String,
    loginURL :: String
  }
  deriving (Generic, Show)

instance FromDhall GithubOAuth

data Config = Config
  { port :: Natural,
    assetsDir :: FilePath,
    dataDir :: FilePath,
    github :: GithubOAuth
  }
  deriving (Generic, Show)

instance FromDhall Config

readConfig :: T.Text -> IO Config
readConfig = input auto
