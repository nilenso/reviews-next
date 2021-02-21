{-# LANGUAGE DeriveGeneric #-}

module Reviews.Config where

import Dhall
import Data.Text as T

data Config = Config deriving (Generic, Show)

instance FromDhall Config

readConfig :: T.Text -> IO Config
readConfig filepath = input auto filepath
