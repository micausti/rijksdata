//package com.michelle.rijksdata
//
//import cats.effect.IO
//import com.michelle.rijksdata.DetailsClient.ObjectNumber
//import io.circe.Json
//import org.http4s.circe.CirceEntityEncoder._
//import org.http4s.client.Client
//import org.http4s.dsl.io._
//import org.http4s.headers.Accept
//import org.http4s.implicits._
//import org.http4s.{HttpRoutes, Response, Status, Uri}
//import org.scalatest.matchers.should.Matchers
//import org.scalatest.wordspec.AnyWordSpec
//
//class ProductCatalogueServiceClientTest extends AnyWordSpec with Matchers {
//  type PCSEncodedProdId = String
//
//  val detailsResponse: Json = Json.obj(("title", Json.fromString("Test Title")))
//
//  val detailsClient: DetailsClient = {
//    val service = HttpRoutes.of[IO] {
//      case r @ GET -> Root / "details" / _ if r.headers.get(Accept).contains(DetailsClient.acceptHeader) =>
//        Ok(detailsResponse)
//      case _ @ GET -> Root / "details" / _ => NotAcceptable()
//    }
//    DetailsClient(Uri(path = "/"), Client.fromHttpApp[IO](service.orNotFound))
//  }
//
//  private val details400Client = {
//    val service = HttpRoutes.pure[IO](Response[IO](status = Status.BadRequest))
//    val client = new DetailsClient {
//      override def get(objectNumber: ObjectNumber): IO[DetailsClient.Detail] = ???
//    }
//    DetailsClient.get()(Uri(path = "/"), Client.fromHttpApp[IO](service.orNotFound))
//  }
//
//  private val details404Client = {
//    val service = HttpRoutes.pure[IO](Response[IO](status = Status.NotFound))
//    DetailsClient(Uri(path = "/"), Client.fromHttpApp[IO](service.orNotFound))
//  }
//
//  "VersionType" should {
//
//    "return version type if present in response" in {
//      detailsClient.get(ObjectNumber("123-456")).unsafeRunSync() shouldBe Some(GEVersionType.INT)
//    }
//
//    "return None if we receive 400 from PCS" in {
//      // Sometimes PCS just breaks trying to lookup a production id and instead of returning 500,
//      // incorrectly returns a 400 response.
//      details400Client.versionTypeFor(ProductionId("1/234/567#001")).unsafeRunSync() shouldBe None
//    }
//
//    "return None if we receive 404 from PCS" in {
//      details404Client.versionTypeFor(ProductionId("1/234/567#001")).unsafeRunSync() shouldBe None
//    }
//  }
//}
