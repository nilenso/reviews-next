{-# LANGUAGE OverloadedStrings #-}

module Reviews.Views.Landing where

import Text.Blaze.Html5
import Text.Blaze.Html5.Attributes
import Prelude hiding (div)

landing :: Markup
landing = do
  div ! class_ "h-100 d-flex flex-row justify-content-center" $ do
    div ! class_ "d-flex flex-column justify-content-center" $ do
      logo
      signInContainer signInWithGithub
  where
    logo = img ! src "/assets/images/logo.svg" ! class_ "img-fluid"

    signInContainer = div ! class_ "d-flex flex-row justify-content-center"

    signInWithGithub =
      a ! href "/oauth/login/github"
        ! class_ "btn btn-outline-primary"
        ! dataAttribute "turbo" "false"
        $ do
          i ! class_ "bi bi-github" $ ""
          text " "
          text "Sign In with Github"
