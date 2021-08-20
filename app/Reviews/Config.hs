{-# LANGUAGE DeriveGeneric #-}

module Reviews.Config where

import Data.Text as T
import Dhall

data Config = Config
  { port :: Natural,
    assetsDir :: FilePath,
    dataDir :: FilePath
  }
  deriving (Generic, Show)

instance FromDhall Config

readConfig :: T.Text -> IO Config
readConfig = input auto
