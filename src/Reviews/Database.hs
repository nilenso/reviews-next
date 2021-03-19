{-# LANGUAGE NamedFieldPuns #-}
{-# LANGUAGE TemplateHaskell #-}
{-# LANGUAGE TypeFamilies #-}

module Reviews.Database where

import Control.Lens
import Control.Monad.Reader
import Control.Monad.State
import Data.Time

import Reviews.Types.User

data Database = Database
  { _users :: [User]
  }

$(makeLenses ''Database)
