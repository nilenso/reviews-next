{-# LANGUAGE OverloadedStrings #-}

module Reviews.Views.Common where

import Text.Blaze.Html5 hiding (contents)
import Text.Blaze.Html5.Attributes hiding (form, span, title)
import Text.Blaze.Internal
import Prelude hiding (div, head, id, span)

turboFrame :: Markup -> Markup
turboFrame = Parent "turbo-frame" "<turbo-frame" "</turbo-frame>"

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

page :: Markup -> Markup
page view = do
  docType
  html $ do
    head $ do
      title "Nilenso Reviews"
      bootstrapCSS
      bootstrapIcons
      hotwiredTurboJS
      bootstrapJS
    body $ do
      div ! class_ "container-fluid" $ view
