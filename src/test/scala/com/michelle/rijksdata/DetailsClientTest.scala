package com.michelle.rijksdata

import cats.effect.IO
import com.michelle.rijksdata.DetailsClient.ObjectNumber
import io.circe.Json
import org.http4s.circe.CirceEntityEncoder._
import org.http4s.client.Client
import org.http4s.dsl.io._
import org.http4s.implicits._
import org.http4s.{HttpRoutes, Response, Status, Uri}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class ProductCatalogueServiceClientTest extends AnyWordSpec with Matchers {
  type PCSEncodedProdId = String

  val detailsResponse: Json = Json.obj(("title", Json.fromString("Test Title")))

  val detailsClient: DetailsClient = {
    val service = HttpRoutes.of[IO] {
      case r @ GET -> Root / "details" / _ => Ok(detailsResponse)
    }
    DetailsClient(Client.fromHttpApp[IO](service.orNotFound))
  }

  private val details400Client = {
    val service = HttpRoutes.pure[IO](Response[IO](status = Status.BadRequest))
    DetailsClient(Client.fromHttpApp[IO](service.orNotFound))
  }

  private val details404Client = {
    val service = HttpRoutes.pure[IO](Response[IO](status = Status.NotFound))
    DetailsClient(Client.fromHttpApp[IO](service.orNotFound))
  }

  "Details" should {

    "return item details type if present in response" in {
      detailsClient.get(ObjectNumber("123-456")).unsafeRunSync() shouldBe Some(detailsResponse)
    }

    "return None if we receive 400 from PCS" in {
      details400Client.get(ObjectNumber("123-456")).unsafeRunSync() shouldBe None
    }

    "return None if we receive 404 from PCS" in {
      details404Client.get(ObjectNumber("123-456")).unsafeRunSync() shouldBe None
    }
  }
}
