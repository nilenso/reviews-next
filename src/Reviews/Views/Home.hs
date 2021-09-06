{-# LANGUAGE OverloadedStrings #-}

module Reviews.Views.Home where

import Reviews.Types.User
import Reviews.Views.Common
import Reviews.Views.Nav
import Text.Blaze.Html5
import Text.Blaze.Html5.Attributes
import Prelude hiding (div)

userHome :: User -> Markup
userHome user = do
  navbar user
  div ! class_ "row" $ do
    div ! class_ "col col-2" $ linksMenu
    div ! class_ "col col-10" $ turboFrame $ text "Hello World"
