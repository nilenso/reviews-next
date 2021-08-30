{-# LANGUAGE FlexibleInstances #-}
{-# LANGUAGE GADTs #-}
{-# LANGUAGE GeneralizedNewtypeDeriving #-}
{-# LANGUAGE MultiParamTypeClasses #-}
{-# LANGUAGE OverloadedStrings #-}
{-# LANGUAGE RecordWildCards #-}
{-# LANGUAGE TypeOperators #-}
{-# LANGUAGE UndecidableInstances #-}

module Fixtures where

import Control.Carrier.Error.Either
import Control.Carrier.Lift
import Control.Carrier.Reader
import Control.Concurrent.STM
import Control.Monad.Except
import GHC.IO.Unsafe (unsafePerformIO)
import Reviews
import Reviews.Effects.OAuth
import Reviews.Types.Cache
import Reviews.Types.Common
import qualified Servant as S
import Test.Tasty

withTestApplication :: (S.Application -> TestTree) -> TestTree
withTestApplication toTestTree =
  withResource
    (createContext "./test.config.dhall")
    (\_ -> return ())
    (toTestTree . testApp . unsafePerformIO)

type TestControllerC =
  ErrorC
    S.ServerError
    ( GithubOAuthC
        ( ReaderC
            (TMVar Cache)
            (ReaderC Config (LiftC IO))
        )
    )

toTestHandler :: AppContext -> TestControllerC a -> S.Handler a
toTestHandler AppContext {..} = do
  S.Handler
    . ExceptT
    . runM
    . runReader config
    . runReader cache
    . runGithubOAuth httpManager githubProvider
    . runError

testApp :: AppContext -> S.Application
testApp ctx =
  S.serve api $
    S.hoistServer
      api
      (toTestHandler ctx)
      server
