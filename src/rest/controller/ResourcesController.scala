package rest.controller

import javax.ws.rs._
import javax.ws.rs.core.Response

import rest.model.{UserLogin, UserRegistration}


@Path("/")
class ResourcesController {

  val userRegistration = new UserRegistration()
  val userLogin = new UserLogin()

  @POST
  @Path("/register")
  @Consumes(Array("application/json"))
  @Produces(Array("application/json"))
  def registerUser(request: String): Response = {

    userRegistration.setMap(convertToMap(request))

    val responseMessage = userRegistration.register()

    if (responseMessage.matches("[a-zA-z]* Registered")) {
      return Response.status(Response.Status.OK).entity("{\"message\" :\"" + responseMessage + "\" }").build()
    }

    Response.status(Response.Status.BAD_REQUEST).entity("{\"message\" : \"Registration Unsuccessful\" }").build()
  }


  @POST
  @Path("/login")
  @Consumes(Array("application/json"))
  @Produces(Array("application/json"))
  def loginUser(request: String): Response = {

    userLogin.setMap(convertToMap(request))

    val responseMessage = userLogin.login()

    if (responseMessage.matches("Student Login Successful")) {
      return Response.status(Response.Status.OK).entity("{\"message\" :\"" + responseMessage + "\" }").build()
    }

    Response.status(Response.Status.BAD_REQUEST).entity("{\"message\" :\"" + responseMessage + "\" }").build()

  }


  private def convertToMap(request: String): Map[String, String] = {

    var convertedMap: Map[String, String] = Map[String, String]()

    val req = request.filter(_ >= ' ').replaceAll(" ", "")

    val untidyMap = req.substring(1, req.length - 1)
      .split(",")
      .map(_.split(":"))
      .map { case Array(k, v) => (k.substring(1, k.length - 1), v.substring(1, v.length - 1)) }
      .toMap

    for (x <- untidyMap) {

      convertedMap += x._1 -> x._2

    }

    convertedMap
  }

}

