{-# LANGUAGE OverloadedStrings #-}

module Reviews.Html where

import Text.Blaze.Html5
import Text.Blaze.Html5.Attributes hiding (title)
import Prelude hiding (div, head)

bootstrapCSS :: Markup
bootstrapCSS =
  link
    ! rel "stylesheet"
    ! type_ "text/css"
    ! href "https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/css/bootstrap.min.css"

bootstrapJS :: Markup
bootstrapJS =
  script
    ! type_ "text/javascript"
    ! src "https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/js/bootstrap.bundle.min.js"
    $ ""

bootstrapIcons :: Markup
bootstrapIcons =
  link
    ! rel "stylesheet"
    ! type_ "text/css"
    ! href "https://cdn.jsdelivr.net/npm/bootstrap-icons@1.5.0/font/bootstrap-icons.css"

page :: Markup -> Markup
page contents = html $ do
  head $ do
    title "Nilenso Reviews"
    bootstrapCSS
    bootstrapIcons
  body $ do
    div ! class_ "container-fluid" $ contents
    bootstrapJS

landingPage :: Markup
landingPage = page $ do
  div ! class_ "h-100 d-flex flex-row justify-content-center" $ do
    div ! class_ "d-flex flex-column justify-content-center" $ do
      logo
      signInContainer signInWithGithub
  where
    logo = img ! src "/assets/images/logo.svg" ! class_ "img-fluid"

    signInContainer = div ! class_ "d-flex flex-row justify-content-center"

    signInWithGithub =
      a ! href "/sign-in" ! class_ "btn btn-outline-primary" $ do
        i ! class_ "bi bi-github" $ ""
        text " "
        text "Sign In with Github"
