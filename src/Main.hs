{-# LANGUAGE DataKinds #-}
{-# LANGUAGE OverloadedStrings #-}
{-# LANGUAGE TypeOperators #-}

module Main where

import Data.Text
import Data.Time (UTCTime)
import Servant.API

import Reviews.Config
import Reviews.Database

main :: IO ()
main = do
  config <- readConfig "./config.dhall"
  print config

