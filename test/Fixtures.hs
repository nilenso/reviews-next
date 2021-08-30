{-# LANGUAGE DataKinds #-}
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
import Servant
import qualified Servant.Auth.Server.Internal.ConfigTypes as SA
import Test.Tasty

withTestApplication :: (Application -> TestTree) -> TestTree
withTestApplication toTestTree =
  withResource
    (createContext "./test.config.dhall")
    (\_ -> return ())
    (toTestTree . testApp . unsafePerformIO)

type TestControllerC =
  ErrorC
    ServerError
    ( GithubOAuthC
        ( ReaderC
            SA.JWTSettings
            ( ReaderC
                SA.CookieSettings
                (ReaderC (TMVar Cache) (ReaderC Config (LiftC IO)))
            )
        )
    )

fromTestControllerC :: AppContext -> TestControllerC a -> Handler a
fromTestControllerC AppContext {..} = do
  Handler
    . ExceptT
    . runM
    . runReader config
    . runReader cache
    . runReader cookieSettings
    . runReader jwtSettings
    . runGithubOAuth httpManager githubProvider
    . runError

testApp :: AppContext -> Application
testApp ctx = mkApp (fromTestControllerC ctx) ctx
