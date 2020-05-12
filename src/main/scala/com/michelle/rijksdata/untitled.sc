import cats.data.Kleisli
import cats.effect.IO
import org.http4s.{Header, HttpRoutes, Request, Response, Status}

object middle {
  def myMiddle(service: HttpRoutes[IO], header: Header) = Kleisli { (req: Request[IO]) =>
    service(req).map {
      case Status.Successful(resp) =>
      resp.putHeaders(header)
      case resp => resp
    }
  }

}

object MyMiddle{
  def addHeader(resp: Response[IO], header: Header) = {
    resp match {
      case Status.Successful(resp) => resp.putHeaders(header)
      case resp => resp
    }
  }

  def apply(service: HttpRoutes[IO], header: Header) =
    service.map(addHeader(_, header))
}