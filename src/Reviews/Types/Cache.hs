module Reviews.Types.Cache where

import Control.Concurrent.STM
import qualified Data.Map as M
import Reviews.Types.User

data Cache = Cache
  { usersMap :: M.Map UserId User,
    githubIdToUserIndex :: M.Map Integer UserId
  }

initCache :: STM (TMVar Cache)
initCache =
  newTMVar
    Cache
      { usersMap = M.empty,
        githubIdToUserIndex = M.empty
      }
