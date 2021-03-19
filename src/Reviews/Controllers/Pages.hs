{-# LANGUAGE OverloadedStrings #-}

module Reviews.Controllers.Pages where

import Control.Monad
import Prelude hiding (head, div)
import Text.Blaze.Html5
import Text.Blaze.Html5.Attributes hiding (title)
import Reviews.Types.Common

turbojs :: AttributeValue
turbojs = "/assets/js/turbo.es5-umd.js" 

bulma :: AttributeValue
bulma = "/assets/css/bulma.min.css"

fontawesome :: AttributeValue
fontawesome = "/assets/css/fontawesome.min.css"

turboFrame :: Tag
turboFrame = textTag "turbo-frame"

index :: Handler Markup
index = return . html $ do
  head $ do
    title "Reviews Next"
    script ! type_ "text/javascript" ! src turbojs $ ""
    link ! rel "stylesheet" ! type_ "text/css" ! href bulma
    link ! rel "stylesheet" ! type_ "text/css" ! href fontawesome
  body $ do
    div ! class_ "container is-fullhd" $ do
      navbar
      div ! class_ "columns mt-2" $ do
        menu
        contents
  
  where
  navbar :: Markup
  navbar = nav ! class_ "navbar" ! role "navigation" $ do
    div ! class_ "navbar-brand" $ do
      a ! class_ "navbar-item" $ "Nilenso"
    div ! class_ "navbar-end" $ do
      div ! class_ "navbar-menu" $ do
        div ! class_ "navbar-item" $ do
          a ! class_ "" $ "Add review event"
        div ! class_ "navbar-item" $ do
          a ! class_ "" $ "Profile"
        div ! class_ "navbar-item" $ do
          button ! class_ "button is-warning is-light" $ "Log out"

  menu :: Markup
  menu = div ! class_ "column is-one-quarter" $
    aside ! class_ "menu" $ do
      ul ! class_ "menu-list" $ 
        menuListItem "Users" ["User 1", "User 2"]

  menuListItem :: Html -> [Html] -> Markup
  menuListItem title elements = li $ do
    a title
    ul $ sequence_ $ li . a <$> elements
  
  contents :: Markup
  contents = div ! class_ "column" $
    p "Contents here"

landing :: Handler Markup
landing = return . html $ do
  head $ do
    title "Reviews Next"
    link ! rel "stylesheet" ! type_ "text/css" ! href bulma
  body $ do
    div "Landing page"
