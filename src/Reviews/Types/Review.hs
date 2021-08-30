module Reviews.Types.Review where

import qualified Data.Text as T
import Data.Time
import Reviews.Types.User

data ReviewContents = ReviewContents
  { level :: Float,
    text :: T.Text
  }

data Review = Review
  { reviewer :: User,
    reviewee :: User,
    createdAt :: UTCTime,
    updatedAt :: UTCTime,
    publishedAt :: Maybe UTCTime,
    contents :: ReviewContents
  }
