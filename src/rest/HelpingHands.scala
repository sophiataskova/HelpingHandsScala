package rest

import javax.ws.rs.Path

import org.glassfish.jersey.server.ResourceConfig
import java.net.URI
import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory
import java.io.IOException

@throws(classOf[IOException])
@Path("/")
object HelpingHands extends App{

    val BASE_URI: String = "http://localhost:8000/"

    val endpoint: URI = new URI(BASE_URI)

    val rc: ResourceConfig = new ResourceConfig(classOf[ResourcesController])

    val server = JdkHttpServerFactory.createHttpServer(endpoint, rc,true)

    println("console v2.0 : Press Enter to stop the server. ")

    System.in.read()

    server.stop(0)

}
