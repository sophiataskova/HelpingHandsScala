package example

import javax.ws.rs.Path

import org.glassfish.jersey.server.ResourceConfig
import java.net.URI
import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory
import java.io.IOException
//import javax.ws.rs.{Produces, GET, Path}

@throws(classOf[IOException])
@Path("/")
object HelloWorld extends App{

    val BASE_URI: String = "http://localhost:8000/"

    val endpoint: URI = new URI(BASE_URI)

    val rc: ResourceConfig = new ResourceConfig(classOf[Resources])

    val server = JdkHttpServerFactory.createHttpServer(endpoint, rc,true)
//    server.start()
    System.out.println("console v2.0 : Press Enter to stop the server. ")
    System.in.read()
    server.stop(0)
//  @GET
//    @Produces(Array("text/plain")) def getClichedMessage: String = {
//      "Hello World"
//    }

  //    server.start
  //    println("Server running")
  //    println("Visit: http://localhost:8080/helloworld")
  //    println("Hit return to stop...")
  //    System.in.read
  //    println("Stopping server")
  //    server.stop(0)
  //    println("Server stopped")
}

//@throws(classOf[IOException])
//@Path("/")
//class HelloWorld {
//
//  @GET
//  @Produces(Array("text/plain")) def getClichedMessage: String = {
//    "Hello World"
//  }
//}
