package src.main.scala.rest

import java.io.IOException
import java.net.URI
import javax.ws.rs.Path

import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory
import org.glassfish.jersey.server.ResourceConfig
import controller.ResourcesController


@throws(classOf[IOException])
@Path("/")
object HelpingHands{

  def main(args: Array[String]) {


    val BASE_URI: String = "http://localhost:8000/"

    val endpoint: URI = new URI(BASE_URI)

    val rc: ResourceConfig = new ResourceConfig(classOf[ResourcesController])

    val server = JdkHttpServerFactory.createHttpServer(endpoint, rc, true)

    println("console v2.0 : Press Enter to stop the server. ")

    System.in.read()

    server.stop(0)
  }
}
