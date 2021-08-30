{-# LANGUAGE DataKinds #-}
{-# LANGUAGE NamedFieldPuns #-}
{-# LANGUAGE OverloadedStrings #-}
{-# LANGUAGE RankNTypes #-}
{-# LANGUAGE RecordWildCards #-}

module Reviews.Controllers.Auth
  ( signInWithGithub,
    oauthCallbackForGithub,
  )
where

import qualified Control.Carrier.Lift as L
import qualified Control.Carrier.Reader as R
import Control.Concurrent.STM
import qualified Control.Effect.Error as E
import Control.Monad.IO.Class
import qualified Data.Map as M
import Data.Maybe (fromJust, isJust)
import qualified Data.Text as T
import qualified Data.Text.Encoding as TE
import qualified Network.OAuth.OAuth2 as OAuth
import qualified Reviews.Effects.OAuth as OAuth
import Reviews.Types.Cache
import Reviews.Types.Common
import Reviews.Types.User
import qualified Reviews.Utils as Utils
import qualified Servant as S
import qualified Servant.Auth.Server as SA

signInWithGithub :: Controller ()
signInWithGithub = do
  Config {secrets} <- R.ask
  let params = [("state", TE.encodeUtf8 . keyBase $ secrets)]
  OAuth.redirectToGithubLogin params >>= Utils.redirect302

findOrCreateGithubUser :: OAuth.OAuth2Token -> OAuth.GithubUser -> Controller User
findOrCreateGithubUser oauthToken githubUser@OAuth.GithubUser {..} = do
  cacheTMVar <- R.ask
  cache <- liftIO . atomically . readTMVar $ cacheTMVar
  let maybeUser = M.lookup github_id (githubIdToUserIndex cache) >>= flip M.lookup (usersMap cache)
  L.sendIO $ case maybeUser of
    Just user -> return user
    Nothing -> do
      user <- createFromGithubUser oauthToken githubUser
      _ <-
        atomically . swapTMVar cacheTMVar $
          cache
            { usersMap = M.insert (userId user) user (usersMap cache),
              githubIdToUserIndex = M.insert github_id (userId user) (githubIdToUserIndex cache)
            }
      return user

startSession :: User -> Controller ()
startSession user = do
  cookieSettings <- R.ask
  jwtSettings <- R.ask
  session <- L.sendIO $ newSession user
  mApplyCookies <- L.sendIO $ SA.acceptLogin cookieSettings jwtSettings session
  case mApplyCookies of
    Nothing -> E.throwError S.err401
    Just applyCookies -> E.throwError S.err302 {S.errHeaders = S.getHeaders (applyCookies baseResponse)}
  where
    baseResponse :: S.Headers '[S.Header "Location" T.Text] S.NoContent
    baseResponse = S.addHeader "/" S.NoContent

oauthCallbackForGithub :: Maybe T.Text -> Maybe T.Text -> Controller ()
oauthCallbackForGithub code state = do
  validateRequest
  token <- unauthorizedOnLeft $ OAuth.fetchGithubToken (fromJust code)
  githubUser <- unauthorizedOnLeft $ OAuth.fetchGithubUser token
  user <- findOrCreateGithubUser token githubUser
  startSession user
  where
    unauthorizedOnLeft :: Show err => Controller (Either err a) -> Controller a
    unauthorizedOnLeft action = do
      result <- action
      case result of
        Left _ -> Utils.redirect302 "/unauthorized"
        Right val -> return val

    validateRequest :: Controller ()
    validateRequest = do
      keyBase <- R.asks (keyBase . secrets)
      if all isJust [code, state] && state == Just keyBase
        then return ()
        else E.throwError S.err400
