{-# LANGUAGE OverloadedStrings #-}

module Reviews.AuthSpec where

import Control.Lens.Getter
import Control.Monad.IO.Class
import qualified Fixtures
import Fixtures.MockOAuth
import qualified Network.HTTP.Types as H
import Network.Wai
import Test.Tasty
import Test.Tasty.HUnit
import Test.Tasty.Wai
import URI.ByteString

tests :: TestTree
tests =
  testGroup
    "Auth Tests"
    [ Fixtures.withTestApplication testSignInWithGithub,
      Fixtures.withTestApplication testOauthCallbackForGithub
    ]

testSignInWithGithub :: Application -> TestTree
testSignInWithGithub testApp =
  testGroup
    "signInWithGithub"
    [ testWai testApp "Redirects to github authorization url" $ do
        res <- get "/oauth/login/github"
        locationHeader <-
          maybe
            (liftIO $ assertFailure "Location header not found")
            return
            (lookup "Location" $ simpleHeaders res)
        assertStatus' H.status302 res
        liftIO $ case parseURI laxURIParserOptions locationHeader of
          Left _ -> assertFailure "Couldn't parse redirect uri"
          Right uri -> do
            assertEqual
              "Path is same as oauth authroize endpoint"
              (uri ^. pathL)
              "/login/oauth/authorize"

            assertEqual
              "State is set"
              (lookup "state" (uri ^. queryL . queryPairsL))
              (Just "TEST_KEY_BASE")

            assertEqual
              "Client Id is set"
              (lookup "client_id" (uri ^. queryL . queryPairsL))
              (Just "TEST_CLIENT_ID")

            assertEqual
              "Response type is set"
              (lookup "response_type" (uri ^. queryL . queryPairsL))
              (Just "code")

            assertEqual
              "Uri to redirect to running service is set"
              (lookup "redirect_uri" (uri ^. queryL . queryPairsL))
              (Just "http://localhost:3000/oauth/callback/github")
    ]

testOauthCallbackForGithub :: Application -> TestTree
testOauthCallbackForGithub testApp =
  withMockGithubApi $
    testGroup
      "Handle callback from github"
      [ testWai testApp "Returns bad request if code is missing from query params" $ do
          res <- get "/oauth/callback/github?state=TEST_KEY_BASE"
          assertStatus' H.status400 res,
        testWai testApp "Returns bad request if state is missing from query params" $ do
          res <- get "/oauth/callback/github?code=xyz"
          assertStatus' H.status400 res,
        testWai testApp "Returns bad request if code and state is missing from query params" $ do
          res <- get "/oauth/callback/github"
          assertStatus' H.status400 res,
        testWai testApp "Returns bad request if state does not match secret key base" $ do
          res <- get "/oauth/callback/github?code=xzy&state=abc"
          assertStatus' H.status400 res,
        testWai testApp "Redirects to / if request is valid" $ do
          res <- get "/oauth/callback/github?code=xzy&state=TEST_KEY_BASE"
          assertStatus' H.status302 res
          locationHeader <-
            maybe
              (liftIO $ assertFailure "Location header not found")
              return
              (lookup "Location" $ simpleHeaders res)
          liftIO $ case parseRelativeRef laxURIParserOptions locationHeader of
            Left _ -> assertFailure "Couldn't parse redirect uri"
            Right uri -> assertEqual "Path is /" (uri ^. pathL) "/"
      ]
