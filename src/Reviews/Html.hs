{-# LANGUAGE OverloadedStrings #-}

module Reviews.Html where

import Reviews.Types.User
import Text.Blaze.Html5 hiding (contents)
import Text.Blaze.Html5.Attributes hiding (form, span, title)
import Text.Blaze.Internal hiding (contents)
import Prelude hiding (div, head, id, span)

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
    $ mempty

bootstrapIcons :: Markup
bootstrapIcons =
  link
    ! rel "stylesheet"
    ! type_ "text/css"
    ! href "https://cdn.jsdelivr.net/npm/bootstrap-icons@1.5.0/font/bootstrap-icons.css"

hotwiredTurboJS :: Markup
hotwiredTurboJS =
  script
    ! type_ "module"
    ! src "https://cdn.skypack.dev/pin/@hotwired/turbo@v7.0.0-rc.3-Ke3RjT96B5Lqzi8XKAG9/mode=imports,min/optimized/@hotwired/turbo.js"
    $ mempty

turboFrame :: Markup -> Markup
turboFrame = Parent "turbo-frame" "<turbo-frame" "</turbo-frame>"

page :: Markup -> Markup
page contents = do
  docType
  html $ do
    head $ do
      title "Nilenso Reviews"
      bootstrapCSS
      bootstrapIcons
      hotwiredTurboJS
      bootstrapJS
    body $ do
      div ! class_ "container-fluid" $ contents

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
      a ! href "/oauth/login/github"
        ! class_ "btn btn-outline-primary"
        ! dataAttribute "turbo" "false"
        $ do
          i ! class_ "bi bi-github" $ ""
          text " "
          text "Sign In with Github"

userHome :: User -> Markup
userHome user = page $ do
  navbar user
  div ! class_ "row" $ do
    div ! class_ "col col-2" $ linksMenu
    div ! class_ "col col-10" $ turboFrame $ text "Hello World"

navbar :: User -> Markup
navbar _user = do
  nav ! class_ "navbar navbar-expand-lg" $ do
    div ! class_ "container-fluid" $ do
      a ! class_ "navbar-brand" ! href "/" $ do
        text "Nilenso Reviews"
      button
        ! class_ "navbar-toggler"
        ! type_ "button"
        ! dataAttribute "bs-toggle" "collapse"
        ! dataAttribute "bs-target" "#navbarSupportedContent"
        ! customAttribute "aria-expanded" "false"
        ! customAttribute "aria-label" "Toggle navigation"
        $ i ! class_ "bi bi-list" $ mempty
      div ! class_ "collapse navbar-collapse" ! id "navbarSupportedContent" $ do
        div ! class_ "w-100 d-flex justify-content-between" $ do
          ul ! class_ "navbar-nav" $ do
            li ! class_ "nav-item" $ a ! class_ "nav-link" ! href "#" $ text "Home"
            li ! class_ "nav-item" $ a ! class_ "nav-link" ! href "#" $ text "Link"
            li ! class_ "nav-item" $ a ! class_ "nav-link" ! href "#" $ text "DropDown"
          div $
            a ! class_ "btn btn-outline-secondary"
              ! href "/logout"
              ! dataAttribute "turbo" "false"
              $ text "Logout"

linksMenu :: Markup
linksMenu = do
  div ! class_ "list-group" $ do
    div ! class_ "list-group-item" $ text "Item 1"
    div ! class_ "list-group-item" $ text "Item 2"
    div ! class_ "list-group-item" $ text "Item 3"
    div ! class_ "list-group-item" $ text "Item 4"
