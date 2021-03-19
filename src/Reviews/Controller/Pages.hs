{-# LANGUAGE OverloadedStrings #-}

module Reviews.Controller.Pages where

import Prelude hiding (head)
import Text.Blaze.Html5
import Servant

index :: Handler Markup
index = return . html $ do
  head $ do
    title "Reviews Next"
  body $ do
    h1 "Hello World!"
