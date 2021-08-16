{-# LANGUAGE NamedFieldPuns #-}
{-# LANGUAGE TemplateHaskell #-}
{-# LANGUAGE TypeFamilies #-}

module Reviews.Types.Timestamps where

import Control.Lens
import Data.Time

data Timestamps = Timestamps
  { _createdAt  :: UTCTime
  , _updatedAt  :: UTCTime
  , _archivedAt :: UTCTime
  }

$(makeLenses ''Timestamps)
