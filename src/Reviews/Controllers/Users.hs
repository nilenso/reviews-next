module Reviews.Controllers.Users where

import Control.Effect.Reader

import Reviews.Types.Common (Handler)
import Reviews.Types.Config
import Reviews.Types.User ( User(..) )

listUsers :: Handler [User]
listUsers = do
  Config { port = p } <- ask
  return [ User { _name = "User 1" } ]
