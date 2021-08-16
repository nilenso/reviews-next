{-# LANGUAGE DeriveGeneric #-}

module Reviews.Types.Config where

import Dhall
import Data.Text as T

data Config = Config
  { port :: Natural
  , assetsDir :: FilePath
  , dataDir :: FilePath
  } deriving (Generic, Show)

instance FromDhall Config

readConfig :: T.Text -> IO Config
readConfig = input auto
