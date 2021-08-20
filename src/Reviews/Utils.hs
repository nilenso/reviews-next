{-# LANGUAGE OverloadedStrings #-}

module Reviews.Utils where

import Control.Algebra
import qualified Control.Effect.Error as E
import qualified Data.ByteString as BS
import qualified Servant as S

redirect302 :: Has (E.Throw S.ServerError) sig m => BS.ByteString -> m a
redirect302 url =
  E.throwError S.err302 {S.errHeaders = [("Location", url)]}

notFound :: (Monad m, Has (E.Error S.ServerError) sig m) => m a
notFound = E.throwError S.err404 {S.errBody = "Not found"}
