package com.michelle.rijksdata

import com.michelle.rijksdata.Models.{AICSearchResponse, ApiLink, Pagination}
import org.scalatest.freespec.AnyFreeSpecLike
import org.scalatest.matchers.should.Matchers
import io.circe.literal._

class AICSearchResponseTest extends AnyFreeSpecLike with Matchers {
  import AICSearchResponseTest._
   "ArtInsChiResponse decoder" - {
     "should be able to decode the Search Response json returned from the Art Institute Chicago endpoint" in {
       AICSearchResponse.AICSearchResponseDecoder.decodeJson(testJson) shouldBe expectedResult
     }
   }

  object AICSearchResponseTest {

    val apiLink1 = ApiLink("https://api.artic.edu/api/v1/artworks/4540")
    val apiLink2 = ApiLink("https://api.artic.edu/api/v1/artworks/8390")
    val apiLink3 = ApiLink("https://api.artic.edu/api/v1/artworks/4535")

    val expectedResult = AICSearchResponse(Pagination(2016), List(apiLink1, apiLink2, apiLink3))

    val testJson = json"""
    {
    "preference": null,
    "pagination": {
        "total": 2106,
        "limit": 10,
        "offset": 0,
        "total_pages": 211,
        "current_page": 1
    },
    "data": [
        {
            "_score": 126.79938,
            "thumbnail": {
                "alt_text": "Woodcut of a man's face in profile, his gaze cast toward the lower-right corner of the print, which is darkened.",
                "width": 2285,
                "lqip": "data:image/gif;base64,R0lGODlhBAAFAPQAAHJvY3l1aYeEeouJfvHt4PXy5vf05vr15vr15/v16Pv36fn16vr26vv36v766//77P798v//9P//9QAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACH5BAAAAAAALAAAAAAEAAUAAAURIOI8BzQIkBQAEVMYCrE0SQgAOw==",
                "height": 3000
            },
            "api_model": "artworks",
            "is_boosted": false,
            "api_link": "https://api.artic.edu/api/v1/artworks/4540",
            "id": 4540,
            "title": "Holy Emissary, from Yiddish Motifs",
            "timestamp": "2021-03-13T03:00:38-06:00"
        },
        {
            "_score": 125.796585,
            "thumbnail": {
                "alt_text": "Print in black ink of an altarpiece with five panels framed by decorative columns. A large central panel of the Virgin Mary and Christ Child with cherubs is flanked by two stacked vertical panels of human figures on either side. Three arches top the structure.",
                "width": 2266,
                "lqip": "data:image/gif;base64,R0lGODlhBAAFAPQAAISDcYiHdoyKeI6Neo+OfJKQgJWTgZWVg5aUgpeWhJiXhJiYhZmYhZqYhpqZh52biKCfi6GgjcLAsMPBsAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACH5BAAAAAAALAAAAAAEAAUAAAUR4NQYUjQICcQcyEMsjgIERQgAOw==",
                "height": 3114
            },
            "api_model": "artworks",
            "is_boosted": false,
            "api_link": "https://api.artic.edu/api/v1/artworks/8390",
            "id": 8390,
            "title": "Altarpiece with the Beautiful Virgin of Regensburg and Saints Christopher, Mary Magdalen, Florian and Catherine Standing in Niches, with God the Father Above",
            "timestamp": "2021-03-13T03:01:10-06:00"
        },
        {
            "_score": 123.01956,
            "thumbnail": {
                "alt_text": "Woodcut print of a street scene with storefronts and awnings and a crowd of people gathered in front.",
                "width": 2299,
                "lqip": "data:image/gif;base64,R0lGODlhBAAFAPQAAB8dGSUkHyclHzUyKk9NRVJPR3t3bIR/cYF+dIOAdpeShp2ZjqSgk62om8rFtsrFt/Pv4vfy5Pnz4/jz5AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACH5BAAAAAAALAAAAAAEAAUAAAUR4MM0DhIMRyIAxlIQChRNUggAOw==",
                "height": 3000
            },
            "api_model": "artworks",
            "is_boosted": false,
            "api_link": "https://api.artic.edu/api/v1/artworks/4535",
            "id": 4535,
            "title": "Maxwell Street, Chicago, from Yiddish Motifs",
            "timestamp": "2021-03-13T03:00:38-06:00"
        }
    ],
    "info": {
        "license_text": "The data in this response is licensed under a Creative Commons Zero (CC0) 1.0 designation and the Terms and Conditions of artic.edu.",
        "license_links": [
            "https://creativecommons.org/publicdomain/zero/1.0/",
            "https://www.artic.edu/terms"
        ],
        "version": "1.0"
    }
}"""
  }

}
