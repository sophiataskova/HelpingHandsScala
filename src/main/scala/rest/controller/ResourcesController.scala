package src.main.scala.rest.controller

import java.io.{InputStream, FileInputStream}
import javax.ws.rs._
import javax.ws.rs.core.Response

import main.scala.rest.model.{Events, UserLogin, UserRegistration}

import scala.collection.immutable.HashMap

@Path("/")
class ResourcesController {

  val userRegistration = new UserRegistration()
  val userLogin = new UserLogin()
  val events = new Events()
  private val userType = HashMap[String, String]("student" -> "studentUsers", "admin" -> "adminUsers", "ngo" -> "ngoUsers")


  @GET
  @Path("/")
  @Produces(Array("text/html"))
  def indexPage(request: String): InputStream = {
    new FileInputStream("src/main/scala/rest/view/index.html")
  }

  @GET
  @Path("/home")
  @Produces(Array("text/html"))
  def homePage(request: String): InputStream = {
    new FileInputStream("src/main/scala/rest/view/home.html")
  }


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

    Response.status(Response.Status.BAD_REQUEST).entity("{\"message\" : \"This username/email has already been registered, please choose another username/email, or try to log in\" }").build()
  }

  @POST
  @Path("/login")
  @Consumes(Array("application/json"))
  @Produces(Array("application/json"))
  def loginUser(request: String): Response = {

    userLogin.setMap(convertToMap(request))

    val responseMessage = userLogin.login()

    if (responseMessage.matches("[a-zA-z]* Successful")) {
      return Response.status(Response.Status.OK).entity("{\"message\" :\"" + responseMessage + "\" }").build()
    }

    Response.status(Response.Status.BAD_REQUEST).entity("{\"message\" :\"" + responseMessage + "\" }").build()
  }

  @POST
  @Path("/logout")
  @Consumes(Array("application/json"))
  @Produces(Array("application/json"))
  def logoutUser(request: String): Response = {

    userLogin.setMap(convertToMap(request))

    val responseMessage = userLogin.logout()

    if (responseMessage.matches("[a-zA-z]* Successful")) {
      return Response.status(Response.Status.OK).entity("{\"message\" :\"" + responseMessage + "\" }").build()
    }

    Response.status(Response.Status.BAD_REQUEST).entity("{\"message\" :\"" + responseMessage + "\" }").build()
  }

  @POST
  @Path("/resetPassword")
  @Consumes(Array("application/json"))
  @Produces(Array("application/json"))
  def resetPassword(request: String): Response = {

    val convertedMap = convertToMap(request)

    var searchMap = Map[String, String]()

    searchMap += "username" -> convertedMap.get("username").get
    searchMap += "securityQuestion" -> convertedMap.get("securityQuestion").get
    searchMap += "answer" -> convertedMap.get("answer").get

    val collection = userType.get(convertedMap.get("userType").get).get

   if(userRegistration.mongodbObject.update(searchMap, convertedMap, collection))
     return Response.status(Response.Status.OK).entity("{\"message\" :\"" + "Password Successfully Changed"  + "\" }").build()

    Response.status(Response.Status.BAD_REQUEST).entity("{\"message\" :\"" + "No such user found" + "\" }").build()
  }

  @POST
  @Path("/createEvent")
  @Consumes(Array("application/json"))
  @Produces(Array("application/json"))
  def createEvent(request: String): Response = {
    val convertedMap = convertToMap(request)
    val responseMessage = events.createEvent(convertedMap)

    if (events.mongodbConnection.get(convertedMap, "createEvents").nonEmpty) {
      return Response.status(Response.Status.OK).entity("{\"message\" :\"" + responseMessage + "\" }").build()
    }

    Response.status(Response.Status.BAD_REQUEST).entity("{\"message\" :\"" + "Event was not created due to some error, please try later" + "\" }").build()
  }

  @POST
  @Path("/deleteEvent")
  @Consumes(Array("application/json"))
  @Produces(Array("application/json"))
  def deleteEvent(request: String): Response = {

    val convertedMap = convertToMap(request)

    val responseMessage = events.deleteEvent(convertedMap)

    if (events.mongodbConnection.get(convertedMap, "createEvents").isEmpty) {
      return Response.status(Response.Status.OK).entity("{\"message\" :\"" + responseMessage + "\" }").build()
    }

    Response.status(Response.Status.BAD_REQUEST).entity("{\"message\" :\"" + "Event was not deleted due to some error, please try later" + "\" }").build()
  }

  @POST
  @Path("/viewEvents")
  @Consumes(Array("application/json"))
  @Produces(Array("application/json"))
  def viewEvents(request: String): Response = {

    val convertedMap = convertToMap(request)

    val responseData = events.mongodbConnection.get(convertedMap, "createEvents")

    if (responseData.nonEmpty) {

      val prettyResponseBuilder = new StringBuilder()

      responseData.foreach(data => prettyResponseBuilder.append(data.toString).append(","))

      val prettyResponseString = prettyResponseBuilder.toString()
      return Response.status(Response.Status.OK).entity(" { \"message\" : [" + prettyResponseString.substring(0, prettyResponseString.length - 1) + "]}").build()
    }

    Response.status(Response.Status.BAD_REQUEST).entity("{\"message\" :\"" + "There are no matching events" + "\" }").build()
  }

  @POST
  @Path("/updateProfile")
  @Consumes(Array("application/json"))
  @Produces(Array("application/json"))
  def updateProfile(request: String): Response = {

    val convertedMap = convertToMap(request)

    var searchMap = Map[String, String]()

    searchMap += "username" -> convertedMap.get("username").get

    searchMap += "emailId" -> convertedMap.get("emailId").get

    val userTypeInRequest = userType.get(convertedMap.get("userType").get).get


    if (userRegistration.mongodbObject.update(searchMap, convertedMap, userTypeInRequest)) {

      return Response.status(Response.Status.OK).entity("{\"message\" :\"" + "Profile updated" + "\" }").build()
    }


    Response.status(Response.Status.BAD_REQUEST).entity("{\"message\" :\"" + "There was no matching registered user, please check your credentials" + "\" }").build()
  }


  @POST
  @Path("/populateEditProfile")
  @Consumes(Array("application/json"))
  @Produces(Array("application/json"))
  def populateEditProfile(request: String): Response = {

    val convertedMap = convertToMap(request)

    val userTypeInRequest = userType.get(convertedMap.get("userType").get).get

    val userData = userRegistration.mongodbObject.get(convertedMap, userTypeInRequest)

    if (userData.nonEmpty)
      return Response.status(Response.Status.OK).entity(" { \"message\" : " + userData.head + "}").build()

    Response.status(Response.Status.BAD_REQUEST).entity("{\"message\" :\"" + "There was no matching registered user, please check your credentials" + "\" }").build()
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

