{-# LANGUAGE OverloadedStrings #-}

module Reviews.Views.Nav where

import Reviews.Types.User
import Text.Blaze.Html5 hiding (contents)
import Text.Blaze.Html5.Attributes hiding (form, span, title)
import Prelude hiding (div, head, id, span)

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
