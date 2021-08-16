{-# LANGUAGE FlexibleInstances #-}
{-# LANGUAGE NamedFieldPuns #-}
{-# LANGUAGE TemplateHaskell #-}
{-# LANGUAGE TypeFamilies #-}
{-# LANGUAGE OverloadedStrings #-}

module Reviews.Types.User where

import Control.Lens ( makeLenses, (^.) )
import Data.Time ()
import Data.Aeson ( ToJSON(toJSON), object, KeyValue((.=)) )
import Text.Blaze.Html
import Text.Blaze.Html5 hiding (map, object)

import Reviews.Types.Timestamps ( Timestamps )

data User = User
  { _name :: String
  -- , _timestamps :: Timestamps
  }

$(makeLenses ''User)

instance ToJSON User where
  toJSON user =
    object ["name" .= (user ^. name) ]

instance ToMarkup User where
  toMarkup user = li $ toHtml $ user ^. name

instance ToMarkup [User] where
  toMarkup = ul . sequence_ . map toMarkup 
