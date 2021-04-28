//package com.michelle.rijksdata
//
//import com.michelle.rijksdata.MetClientSearchResultTest.{objectResultExpectedResult, objectResultJson, searchResultExpectedResult, searchResultJson}
//import com.michelle.rijksdata.Models.{MetObjectResult, MetSearchResult}
//import io.circe.literal._
//import org.scalatest.freespec.AnyFreeSpecLike
//import org.scalatest.matchers.should.Matchers
//
//class MetClientSearchResultTest extends AnyFreeSpecLike with Matchers {
//  "MetClient Models" - {
//    "should be able to decode the MetClientSearchResult json returned from the Met endpoint" in {
//      MetSearchResult.metSearchResultDecoder.decodeJson(searchResultJson) shouldBe Right(searchResultExpectedResult)
//    }
//
//    "should be able to decode the MetObjectResult json returned from the Met endpoint" in {
//      MetObjectResult.metObjectResultDecoder.decodeJson(objectResultJson) shouldBe Right(objectResultExpectedResult)
//    }
//  }
//}
//
//object MetClientSearchResultTest {
//
//val searchResultExpectedResult = MetSearchResult(List(436098, 551786, 472562, 317877, 544740, 337700, 329077))
//
//  val searchResultJson =
//    json"""
//          {
//          "total": 150,
//          "objectIDs": [
//            436098,
//            551786,
//            472562,
//            317877,
//            544740,
//            337700,
//            329077]
//          }"""
//
//  val objectResultExpectedResult = MetObjectResult("https://images.metmuseum.org/CRDImages/dp/original/DP825455.jpg", "Woodcut with a Horned Head","Paul Gauguin", "French")
//  val objectResultJson =
//    json"""
//      {
//    "objectID": 337857,
//    "isHighlight": false,
//    "accessionNumber": "36.7.3",
//    "accessionYear": "1936",
//    "isPublicDomain": true,
//    "primaryImage": "https://images.metmuseum.org/CRDImages/dp/original/DP825455.jpg",
//    "primaryImageSmall": "https://images.metmuseum.org/CRDImages/dp/web-large/DP825455.jpg",
//    "additionalImages": [],
//    "constituents": [
//        {
//            "constituentID": 161924,
//            "role": "Artist",
//            "name": "Paul Gauguin",
//            "constituentULAN_URL": "http://vocab.getty.edu/page/ulan/500011421",
//            "constituentWikidata_URL": "https://www.wikidata.org/wiki/Q37693",
//            "gender": ""
//        }
//    ],
//    "department": "Drawings and Prints",
//    "objectName": "Print",
//    "title": "Woodcut with a Horned Head",
//    "culture": "",
//    "period": "",
//    "dynasty": "",
//    "reign": "",
//    "portfolio": "",
//    "artistRole": "Artist",
//    "artistPrefix": "",
//    "artistDisplayName": "Paul Gauguin",
//    "artistDisplayBio": "French, Paris 1848–1903 Atuona, Hiva Oa, Marquesas Islands",
//    "artistSuffix": "",
//    "artistAlphaSort": "Gauguin, Paul",
//    "artistNationality": "French",
//    "artistBeginDate": "1848",
//    "artistEndDate": "1903",
//    "artistGender": "",
//    "artistWikidata_URL": "https://www.wikidata.org/wiki/Q37693",
//    "artistULAN_URL": "http://vocab.getty.edu/page/ulan/500011421",
//    "objectDate": "1898–99",
//    "objectBeginDate": 1898,
//    "objectEndDate": 1899,
//    "medium": "Woodcut on transparent laid tissue paper",
//    "dimensions": "5 5/8 x 11 1/4 in. block\r\n6 7/8 x 11 3/8 in. paper",
//    "measurements": null,
//    "creditLine": "Harris Brisbane Dick Fund, 1936",
//    "geographyType": "",
//    "city": "",
//    "state": "",
//    "county": "",
//    "country": "",
//    "region": "",
//    "subregion": "",
//    "locale": "",
//    "locus": "",
//    "excavation": "",
//    "river": "",
//    "classification": "Prints",
//    "rightsAndReproduction": "",
//    "linkResource": "",
//    "metadataDate": "2020-09-16T18:35:19.457Z",
//    "repository": "Metropolitan Museum of Art, New York, NY",
//    "objectURL": "https://www.metmuseum.org/art/collection/search/337857",
//    "tags": [
//        {
//            "term": "Birds",
//            "AAT_URL": "http://vocab.getty.edu/page/aat/300266506",
//            "Wikidata_URL": "https://www.wikidata.org/wiki/Q5113"
//        },
//        {
//            "term": "Faces",
//            "AAT_URL": "http://vocab.getty.edu/page/aat/300251798",
//            "Wikidata_URL": "https://www.wikidata.org/wiki/Q37017"
//        },
//        {
//            "term": "Lizards",
//            "AAT_URL": "http://vocab.getty.edu/page/aat/300250295",
//            "Wikidata_URL": "https://www.wikidata.org/wiki/Q15629245"
//        }
//    ],
//    "objectWikidata_URL": "",
//    "isTimelineWork": false,
//    "GalleryNumber": ""
//}"""
//}
