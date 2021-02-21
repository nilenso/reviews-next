{-# LANGUAGE NamedFieldPuns #-}
{-# LANGUAGE TemplateHaskell #-}
{-# LANGUAGE TypeFamilies #-}

module Reviews.Database where

import Control.Monad.Reader
import Control.Monad.State
import Data.Acid
import Data.SafeCopy
import Data.Time

data Timestamps = Timestamps
  { createdAt  :: UTCTime
  , updatedAt  :: UTCTime
  , archivedAt :: UTCTime
  }

data User = User
  { name :: String
  , timestamps :: Timestamps
  }

data Database = Database
  { users :: [User]
  }

getUsers :: Query Database [User]
getUsers = users <$> ask

addUser :: User -> Update Database ()
addUser user = modify go
  where
    go (Database { users }) = Database $ user:users

$(deriveSafeCopy 0 'base ''Timestamps)
$(deriveSafeCopy 0 'base ''User)
$(deriveSafeCopy 0 'base ''Database)
$(makeAcidic ''Database ['getUsers, 'addUser])
