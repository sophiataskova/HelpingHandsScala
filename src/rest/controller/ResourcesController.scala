package rest

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/hello")
class  ResourcesController {


  // This method is called if TEXT_PLAIN is request


  @GET
  @Produces(Array(MediaType.TEXT_PLAIN))
  def sayPlainTextHello() :String= {
    "Hello Jersey"
  }

  // This method is called if XML is request
  @GET
  @Produces(Array(MediaType.TEXT_XML))
  def sayXMLHello(): String = {
    "<?xml version=\"1.0\"?>" + "<hello> Hello Jersey" + "</hello>"
  }

  // This method is called if HTML is request
  @GET
  @Produces(Array(MediaType.TEXT_HTML))
  def sayHtmlHello():String= {
    "<html> " + "<title>" + "Hello Jersey" + "</title>" + "<body><h1>" + "Hello Jersey" + "</body></h1>" + "</html> "
  }
}

