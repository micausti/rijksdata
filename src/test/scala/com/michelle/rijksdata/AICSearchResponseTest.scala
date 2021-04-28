//package com.michelle.rijksdata
//
//import com.michelle.rijksdata.Models._
//import org.scalatest.freespec.AnyFreeSpecLike
//import org.scalatest.matchers.should.Matchers
//import io.circe.literal._
//
//class AICModelsTest extends AnyFreeSpecLike with Matchers {
//  import AICModelsTest._
//  "AIC Models" - {
//    "should be able to decode the AICSearchResponse json returned from the Art Institute Chicago endpoint" in {
//      AICSearchResponse.AICSearchResponseDecoder.decodeJson(
//        searchResponseTestJson) shouldBe Right(searchResponseExpectedResult)
//    }
//
//    "should be able to decode the AICItemResult json returned from the Art Institute Chicago endpoint" in {
//      AICItemResult.AICItemResultDecoder.decodeJson(itemResultTestJson) shouldBe Right(itemResultExpectedResult)
//    }
//
//  }
//
//  object AICModelsTest {
//
//    val apiLink1 = ApiLink("https://api.artic.edu/api/v1/artworks/4540")
//    val apiLink2 = ApiLink("https://api.artic.edu/api/v1/artworks/8390")
//    val apiLink3 = ApiLink("https://api.artic.edu/api/v1/artworks/4535")
//
//    val searchResponseExpectedResult = AICSearchResponse(
//      List(apiLink1, apiLink2, apiLink3))
//
//    val searchResponseTestJson = json"""
//    {
//    "preference": null,
//    "pagination": {
//        "total": 2106,
//        "limit": 10,
//        "offset": 0,
//        "total_pages": 211,
//        "current_page": 1
//    },
//    "data": [
//        {
//            "_score": 126.79938,
//            "thumbnail": {
//                "alt_text": "Woodcut of a man's face in profile, his gaze cast toward the lower-right corner of the print, which is darkened.",
//                "width": 2285,
//                "lqip": "data:image/gif;base64,R0lGODlhBAAFAPQAAHJvY3l1aYeEeouJfvHt4PXy5vf05vr15vr15/v16Pv36fn16vr26vv36v766//77P798v//9P//9QAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACH5BAAAAAAALAAAAAAEAAUAAAURIOI8BzQIkBQAEVMYCrE0SQgAOw==",
//                "height": 3000
//            },
//            "api_model": "artworks",
//            "is_boosted": false,
//            "api_link": "https://api.artic.edu/api/v1/artworks/4540",
//            "id": 4540,
//            "title": "Holy Emissary, from Yiddish Motifs",
//            "timestamp": "2021-03-13T03:00:38-06:00"
//        },
//        {
//            "_score": 125.796585,
//            "thumbnail": {
//                "alt_text": "Print in black ink of an altarpiece with five panels framed by decorative columns. A large central panel of the Virgin Mary and Christ Child with cherubs is flanked by two stacked vertical panels of human figures on either side. Three arches top the structure.",
//                "width": 2266,
//                "lqip": "data:image/gif;base64,R0lGODlhBAAFAPQAAISDcYiHdoyKeI6Neo+OfJKQgJWTgZWVg5aUgpeWhJiXhJiYhZmYhZqYhpqZh52biKCfi6GgjcLAsMPBsAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACH5BAAAAAAALAAAAAAEAAUAAAUR4NQYUjQICcQcyEMsjgIERQgAOw==",
//                "height": 3114
//            },
//            "api_model": "artworks",
//            "is_boosted": false,
//            "api_link": "https://api.artic.edu/api/v1/artworks/8390",
//            "id": 8390,
//            "title": "Altarpiece with the Beautiful Virgin of Regensburg and Saints Christopher, Mary Magdalen, Florian and Catherine Standing in Niches, with God the Father Above",
//            "timestamp": "2021-03-13T03:01:10-06:00"
//        },
//        {
//            "_score": 123.01956,
//            "thumbnail": {
//                "alt_text": "Woodcut print of a street scene with storefronts and awnings and a crowd of people gathered in front.",
//                "width": 2299,
//                "lqip": "data:image/gif;base64,R0lGODlhBAAFAPQAAB8dGSUkHyclHzUyKk9NRVJPR3t3bIR/cYF+dIOAdpeShp2ZjqSgk62om8rFtsrFt/Pv4vfy5Pnz4/jz5AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACH5BAAAAAAALAAAAAAEAAUAAAUR4MM0DhIMRyIAxlIQChRNUggAOw==",
//                "height": 3000
//            },
//            "api_model": "artworks",
//            "is_boosted": false,
//            "api_link": "https://api.artic.edu/api/v1/artworks/4535",
//            "id": 4535,
//            "title": "Maxwell Street, Chicago, from Yiddish Motifs",
//            "timestamp": "2021-03-13T03:00:38-06:00"
//        }
//    ],
//    "info": {
//        "license_text": "The data in this response is licensed under a Creative Commons Zero (CC0) 1.0 designation and the Terms and Conditions of artic.edu.",
//        "license_links": [
//            "https://creativecommons.org/publicdomain/zero/1.0/",
//            "https://www.artic.edu/terms"
//        ],
//        "version": "1.0"
//    }
//}"""
//  }
//
//  val itemData = ItemData(
//    "Holy Emissary, from Yiddish Motifs",
//    "Todros Geller\nAmerican, born Russia (now Ukraine), 1889-1949",
//    "United States",
//    "5d3e12e4-bdf9-2dd6-6c6b-d3e1b7920edd"
//  )
//  val itemConfig = ItemConfig("https://www.artic.edu/iiif/2")
//  val itemResultExpectedResult = AICItemResult(itemData, itemConfig)
//
//  val itemResultTestJson =
//    json"""
//      {
//    "data": {
//        "id": 4540,
//        "api_model": "artworks",
//        "api_link": "https://api.artic.edu/api/v1/artworks/4540",
//        "is_boosted": false,
//        "title": "Holy Emissary, from Yiddish Motifs",
//        "alt_titles": null,
//        "thumbnail": {
//            "lqip": "data:image/gif;base64,R0lGODlhBAAFAPQAAHJvY3l1aYeEeouJfvHt4PXy5vf05vr15vr15/v16Pv36fn16vr26vv36v766//77P798v//9P//9QAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACH5BAAAAAAALAAAAAAEAAUAAAURIOI8BzQIkBQAEVMYCrE0SQgAOw==",
//            "width": 2285,
//            "height": 3000,
//            "alt_text": "Woodcut of a man's face in profile, his gaze cast toward the lower-right corner of the print, which is darkened."
//        },
//        "main_reference_number": "1930.115",
//        "has_not_been_viewed_much": false,
//        "boost_rank": null,
//        "date_start": 1926,
//        "date_end": 1926,
//        "date_display": "1926",
//        "date_qualifier_title": "",
//        "date_qualifier_id": null,
//        "artist_display": "Todros Geller\nAmerican, born Russia (now Ukraine), 1889-1949",
//        "place_of_origin": "United States",
//        "dimensions": "105 × 90 mm (image); 158 × 120 mm (wood veneer paper); 430 × 325 mm (sheet)",
//        "medium_display": "Woodcut on Japanese wood veneer paper mounted on paper",
//        "inscriptions": null,
//        "credit_line": "Gift of the Artist",
//        "publication_history": null,
//        "exhibition_history": null,
//        "provenance_text": null,
//        "publishing_verification_level": "Web Basic",
//        "internal_department_id": 3,
//        "fiscal_year": 1931,
//        "fiscal_year_deaccession": null,
//        "is_public_domain": false,
//        "is_zoomable": false,
//        "max_zoom_window_size": 843,
//        "copyright_notice": null,
//        "has_multimedia_resources": false,
//        "has_educational_resources": false,
//        "colorfulness": 11.7894,
//        "color": {
//            "h": 40,
//            "l": 51,
//            "s": 10,
//            "percentage": 0.0024646351643429444,
//            "population": 23
//        },
//        "latitude": null,
//        "longitude": null,
//        "latlon": null,
//        "is_on_view": false,
//        "on_loan_display": null,
//        "gallery_title": null,
//        "gallery_id": null,
//        "artwork_type_title": null,
//        "artwork_type_id": null,
//        "department_title": "Prints and Drawings",
//        "department_id": "PC-13",
//        "artist_id": 34621,
//        "artist_title": "Todros Geller",
//        "alt_artist_ids": [],
//        "artist_ids": [
//            34621
//        ],
//        "artist_titles": [
//            "Todros Geller"
//        ],
//        "category_ids": [
//            "PC-154",
//            "PC-13",
//            "PC-827"
//        ],
//        "category_titles": [
//            "Chicago Artists",
//            "Prints and Drawings",
//            "SAIC Alumni and Faculty"
//        ],
//        "artwork_catalogue_ids": [],
//        "term_titles": [
//            "woodcut",
//            "paper (fiber product)",
//            "woodcut (process)",
//            "book",
//            "prints and drawing",
//            "print"
//        ],
//        "style_id": null,
//        "style_title": null,
//        "alt_style_ids": [],
//        "style_ids": [],
//        "style_titles": [],
//        "classification_id": "TM-148",
//        "classification_title": "woodcut",
//        "alt_classification_ids": [
//            "TM-11",
//            "TM-4",
//            "TM-17"
//        ],
//        "classification_ids": [
//            "TM-148",
//            "TM-11",
//            "TM-4",
//            "TM-17"
//        ],
//        "classification_titles": [
//            "woodcut",
//            "book",
//            "prints and drawing",
//            "print"
//        ],
//        "subject_id": null,
//        "alt_subject_ids": [],
//        "subject_ids": [],
//        "subject_titles": [],
//        "material_id": "TM-2982",
//        "alt_material_ids": [],
//        "material_ids": [
//            "TM-2982"
//        ],
//        "material_titles": [
//            "paper (fiber product)"
//        ],
//        "technique_id": "TM-3862",
//        "alt_technique_ids": [],
//        "technique_ids": [
//            "TM-3862"
//        ],
//        "technique_titles": [
//            "woodcut (process)"
//        ],
//        "theme_titles": [
//            "Chicago Artists",
//            "SAIC Alumni and Faculty"
//        ],
//        "image_id": "5d3e12e4-bdf9-2dd6-6c6b-d3e1b7920edd",
//        "alt_image_ids": [],
//        "document_ids": [],
//        "sound_ids": [],
//        "video_ids": [],
//        "text_ids": [],
//        "section_ids": [],
//        "section_titles": [],
//        "site_ids": [],
//        "suggest_autocomplete_all": [
//            {
//                "input": [
//                    "1930.115"
//                ],
//                "contexts": {
//                    "groupings": [
//                        "accession"
//                    ]
//                }
//            },
//            {
//                "input": [
//                    "Holy Emissary, from Yiddish Motifs"
//                ],
//                "weight": 66574,
//                "contexts": {
//                    "groupings": [
//                        "title"
//                    ]
//                }
//            }
//        ],
//        "last_updated_source": "2020-10-15T18:44:31-05:00",
//        "last_updated": "2021-02-09T18:09:07-06:00",
//        "timestamp": "2021-03-14T01:52:05-06:00"
//    },
//    "info": {
//        "license_text": "The data in this response is licensed under a Creative Commons Zero (CC0) 1.0 designation and the Terms and Conditions of artic.edu.",
//        "license_links": [
//            "https://creativecommons.org/publicdomain/zero/1.0/",
//            "https://www.artic.edu/terms"
//        ],
//        "version": "1.0"
//    },
//    "config": {
//        "iiif_url": "https://www.artic.edu/iiif/2",
//        "shop_image_url": "https://shop-images.imgix.net",
//        "shop_product_url": "https://shop.artic.edu/item.aspx?productId=",
//        "shop_category_url": "https://shop.artic.edu/item.aspx?productId=",
//        "website_url": "http://www.artic.edu"
//    }
//}"""
//}
