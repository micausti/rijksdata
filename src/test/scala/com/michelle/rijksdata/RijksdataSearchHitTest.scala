package com.michelle.rijksdata

import com.michelle.rijksdata.Models.CollectionResponse.{ArtObject, Count, WebImage}
import com.michelle.rijksdata.Models._
import org.scalatest.freespec.AnyFreeSpecLike
import org.scalatest.matchers.should.Matchers
import io.circe.literal._

class RijksdataSearchResponseTest extends AnyFreeSpecLike with Matchers {
  import RijksdataSearchResponseTest._

  "RijksdataSearchResponse decoder" - {
    "should be able to decode json returned from the rijksdata endpoint" in {
      CollectionResponse.decoder.decodeJson(testJson) shouldBe Right(
        expectedValue)
    }
  }

}

object RijksdataSearchResponseTest {
//  val firstArtObject = ArtObject(
//    "Clear Weather with a Southerly Wind, Katsushika Hokusai, 1829 - 1833",
//    "Katsushika Hokusai",
//    WebImage(
//      "https://lh5.ggpht.com/tjcPcIxRAZA7Ufl8sUCmfvjoHJ7t2VIykyCIGZ6eCvxnu53L5IMiCR1La8B9BKL7q4CjkW6z4BYhP7kU1Es9RoanHDMl=s0"),
//    "Japan"
//  )
  val firstArtObject = ArtObject(
    "Clear Weather with a Southerly Wind, Katsushika Hokusai, 1829 - 1833",
    "Katsushika Hokusai",
    "Japan"
  )
//  val secondArtObject = ArtObject(
//    "Zilverreiger in de regen, Ohara Koson, 1925 - 1936",
//    "Ohara Koson",
//    WebImage(
//      "https://lh4.ggpht.com/zk1MLRvb74WIYAUPNiz8xNPUVCRwDn5WUwFOJeCv1y7Nx0ddaf9uEJtRnI8SO4EvYwOucGQ-kb_l_vcv7gJt2Or7rKI=s0"),
//    "Japan"
//  )
  val secondArtObject = ArtObject(
    "Zilverreiger in de regen, Ohara Koson, 1925 - 1936",
    "Ohara Koson",
    "Japan"
  )
  val expectedValue =
    CollectionResponse(List(firstArtObject, secondArtObject))

  val testJson = json"""{
    "elapsedMilliseconds": 0,
    "count": 4337,
    "countFacets": {
      "hasimage": 3378,
      "ondisplay": 6
    },
    "artObjects": [
    {
      "links": {
        "self": "http://www.rijksmuseum.nl/api/en/collection/RP-P-1952-183",
        "web": "http://www.rijksmuseum.nl/en/collection/RP-P-1952-183"
      },
      "id": "en-RP-P-1952-183",
      "objectNumber": "RP-P-1952-183",
      "title": "Clear Weather with a Southerly Wind",
      "hasImage": true,
      "principalOrFirstMaker": "Katsushika Hokusai",
      "longTitle": "Clear Weather with a Southerly Wind, Katsushika Hokusai, 1829 - 1833",
      "showImage": true,
      "permitDownload": true,
      "webImage": {
        "guid": "52787c3d-64a3-4810-9b21-8a3cbd07351b",
        "offsetPercentageX": 0,
        "offsetPercentageY": 0,
        "width": 2783,
        "height": 2027,
        "url": "https://lh5.ggpht.com/tjcPcIxRAZA7Ufl8sUCmfvjoHJ7t2VIykyCIGZ6eCvxnu53L5IMiCR1La8B9BKL7q4CjkW6z4BYhP7kU1Es9RoanHDMl=s0"
      },
      "headerImage": {
        "guid": "8aa7575a-3c13-4d40-9944-e2d00d50ec1f",
        "offsetPercentageX": 0,
        "offsetPercentageY": 0,
        "width": 1920,
        "height": 460,
        "url": "https://lh5.ggpht.com/3OYtD5bNwe5pVX4XV28tlD2-6ZoKzOu2DnBa5j2E4jgIkIwyaBZrxmaWE6qOJYkwAMoV61dpTCR2ZabFuTmR2qphKIc=s0"
      },
      "productionPlaces": [
      "Japan"
      ]
    },
    {
      "links": {
        "self": "http://www.rijksmuseum.nl/api/en/collection/RP-P-1999-548",
        "web": "http://www.rijksmuseum.nl/en/collection/RP-P-1999-548"
      },
      "id": "en-RP-P-1999-548",
      "objectNumber": "RP-P-1999-548",
      "title": "Zilverreiger in de regen",
      "hasImage": true,
      "principalOrFirstMaker": "Ohara Koson",
      "longTitle": "Zilverreiger in de regen, Ohara Koson, 1925 - 1936",
      "showImage": true,
      "permitDownload": true,
      "webImage": {
        "guid": "2b7aa9a7-209a-474c-bdb2-ce45d53fe800",
        "offsetPercentageX": 0,
        "offsetPercentageY": 0,
        "width": 2013,
        "height": 3000,
        "url": "https://lh4.ggpht.com/zk1MLRvb74WIYAUPNiz8xNPUVCRwDn5WUwFOJeCv1y7Nx0ddaf9uEJtRnI8SO4EvYwOucGQ-kb_l_vcv7gJt2Or7rKI=s0"
      },
      "headerImage": {
        "guid": "cf07ddfe-2e91-4945-9422-3218c281b916",
        "offsetPercentageX": 0,
        "offsetPercentageY": 0,
        "width": 1923,
        "height": 460,
        "url": "https://lh4.ggpht.com/kFRc_fd6FhfH8xosFOSQbuuVntMPyrma4wVj7tDPfbVUCdbJj23xsEXaxdgEgNtU198fw681WaQDyEhhf8OF4FvHFSs=s0"
      },
      "productionPlaces": [
      "Japan"
      ]
    }
    ]
}"""
}
