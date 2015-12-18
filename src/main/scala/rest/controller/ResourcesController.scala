package src.main.scala.rest.controller

import java.io.{InputStream, FileInputStream}
import javax.ws.rs._
import javax.ws.rs.core.{MediaType, Response}

import main.scala.rest.model.{EnrollEvents, Events, UserLogin, UserRegistration}

import scala.collection.immutable.HashMap

@Path("/")
class ResourcesController {

  val userRegistration = new UserRegistration()
  val userLogin = new UserLogin()
  val events = new Events()
  val enrollEvents = new EnrollEvents()
  private val userType = HashMap[String, String]("student" -> "studentUsers", "admin" -> "adminUsers", "ngo" -> "ngoUsers")


  @GET
  @Path("/")
  @Produces(Array("text/html"))
  def indexPage(): InputStream = {
    new FileInputStream("src/main/scala/rest/view/index.html")
  }

  @GET
  @Path("/home")
  @Produces(Array("text/html"))
  def homePage(): InputStream = {
    new FileInputStream("src/main/scala/rest/view/home.html")
  }


  @POST
  @Path("/register")
  @Consumes(Array("application/json"))
  @Produces(Array("application/json"))
  def registerUser(request: String): Response = {

    userRegistration.setMap(convertToMap(request))

    val responseMessage = userRegistration.register()

    if (responseMessage.matches("[a-zA-z ]* Registered")) {
      return Response.status(Response.Status.OK).entity("{\"message\" :\"" + responseMessage + "\" }").build()
    }

    Response.status(Response.Status.BAD_REQUEST).entity("{\"message\" : \"This username/email has already been registered or is invalid, please choose correct credentials, or try to log in\" }").build()
  }

  @POST
  @Path("/login")
  @Consumes(Array("application/json"))
  @Produces(Array("application/json"))
  def loginUser(request: String): Response = {

    userLogin.setMap(convertToMap(request))

    val responseMessage = userLogin.login()

    if (responseMessage.matches("[a-zA-z ]* Successful")) {
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

    if (responseMessage.matches("[a-zA-z ]* Successful")) {
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

    if (userRegistration.mongodbObject.update(searchMap, convertedMap, collection))
      return Response.status(Response.Status.OK).entity("{\"message\" :\"" + "Password Successfully Changed" + "\" }").build()

    Response.status(Response.Status.BAD_REQUEST).entity("{\"message\" :\"" + "No such user found with your combination of data, please check your fields again" + "\" }").build()
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
      return Response.status(Response.Status.OK).entity(" { \"message\" :" + userData.head + "}").build()

    Response.status(Response.Status.BAD_REQUEST).entity("{\"message\" :\"" + "There was no matching registered user, please check your credentials" + "\" }").build()
  }

  @POST
  @Path("/attendEvent")
  @Consumes(Array("application/json"))
  @Produces(Array("application/json"))
  def attendEvent(request: String): Response = {

    val convertedMap = convertToMap(request)

    var event: Map[String, String] = Map[String, String]()
    event += "organization" -> convertedMap.get("organization").get
    event += "eventName" -> convertedMap.get("eventName").get


    if (enrollEvents.enrollAsParticipant(event)) {
      return Response.status(Response.Status.OK).entity(" { \"message\" : " + "\"You have successfully enrolled for this event\"" + "}").build()
    }

    Response.status(Response.Status.BAD_REQUEST).entity("{\"message\" : " + "\"There was a problem during enrollment, Please try again later\"" + " }").build()
  }

  @POST
  @Path("/volunteerForEvent")
  @Consumes(Array("application/json"))
  @Produces(Array("application/json"))
  def volunteerForEvent(request: String): Response = {

    val convertedMap = convertToMap(request)

    var event: Map[String, String] = Map[String, String]()
    event += "organization" -> convertedMap.get("organization").get
    event += "eventName" -> convertedMap.get("eventName").get

    var user: Map[String, String] = Map[String, String]()

    user += "username" -> convertedMap.get("username").get
    user += "emailId" -> convertedMap.get("emailId").get

    if (enrollEvents.enrollAsVolunteer(event, user)) {
      return Response.status(Response.Status.OK).entity(" { \"message\" : " + "\"We have conveyed your request to volunteer, to the organizer\"" + "}").build()
    }

    Response.status(Response.Status.BAD_REQUEST).entity("{\"message\" :\"" + "\"There was problem joining as a volunteer, please try again later\"" + "\" }").build()
  }

  @POST
  @Path("/approveEvent")
  @Consumes(Array("application/json"))
  @Produces(Array("application/json"))
  def approveEvent(request: String): Response = {

    val convertedMap = convertToMap(request)

    if (convertedMap.get("userType").get == "admin") {

      if (enrollEvents.approveEvent(convertedMap - "userType")) {

        return Response.status(Response.Status.OK).entity(" { \"message\" : " + "\"Event approved\"" + "}").build()

      }
      return Response.status(Response.Status.OK).entity(" { \"message\" : " + "\"There was a problem approving the request, Please try again later\"" + "}").build()
    }
    Response.status(Response.Status.BAD_REQUEST).entity("{\"message\" :\"" + "\"You are not authorized for this action\"" + "\" }").build()
  }

  @POST
  @Path("/denyEvent")
  @Consumes(Array("application/json"))
  @Produces(Array("application/json"))
  def denyEvent(request: String): Response = {

    val convertedMap = convertToMap(request)

    if (convertedMap.get("userType").get == "admin") {

      if (enrollEvents.denyEvent(convertedMap - "userType")) {

        return Response.status(Response.Status.OK).entity(" { \"message\" : " + "\"Event Denied\"" + "}").build()

      }
      return Response.status(Response.Status.OK).entity(" { \"message\" : " + "\"There was a problem approving the request, Please try again later\"" + "}").build()
    }
    Response.status(Response.Status.BAD_REQUEST).entity("{\"message\" :\"" + "\"You are not authorized for this action\"" + "\" }").build()
  }

  @POST
  @Path("/searchEvent")
  @Consumes(Array("text/plain"))
  @Produces(Array("application/json"))
  def searchEvent(request: String): Response = {

    val responseData = events.searchKeywords(request)

    if (responseData.nonEmpty) {
      val prettyResponseBuilder = new StringBuilder()

      responseData.foreach(data => prettyResponseBuilder.append(data.toString).append(","))

      val prettyResponseString = prettyResponseBuilder.toString()

      return Response.status(Response.Status.OK).entity(" { \"message\" : [" + prettyResponseString.substring(0, prettyResponseString.length - 1) + "]}").build()
    }

    Response.status(Response.Status.BAD_REQUEST).entity("{\"message\" : " + "\"No Data Found, Please Narrow Down Your Search\"" + " }").build()
  }

  private def convertToMap(request: String): Map[String, String] = {

    var tidyMap: Map[String, String] = Map[String, String]()

    val req = request.filter(_ >= ' ').replaceAll(" ", "")

    val untidyMap = req.substring(1, req.length - 1)
      .split(",")
      .map(_.split(":"))
      .map { case Array(k, v) => (k.substring(1, k.length - 1), v.substring(1, v.length - 1)) }
      .toMap

    for (x <- untidyMap) {
      tidyMap += x._1 -> x._2
    }

    tidyMap
  }

}

