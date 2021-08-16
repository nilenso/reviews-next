{-# LANGUAGE RankNTypes #-}

module Reviews.Types.Common where

import Reviews.Types.Config ( Config )
import Control.Effect.Reader (Has, Reader)

type Handler a = forall sig m. (Has (Reader Config) sig m) => m a
