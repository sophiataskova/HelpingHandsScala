package main.scala.rest.model

import scala.collection.mutable.HashMap

class UserRegistration() {

  private val userType = HashMap[String, String]("student" -> "studentUsers", "admin" -> "adminUsers", "ngo" -> "ngoUsers")

  private var map = Map[String, String]()

  val mongodbObject = new MongodbConnection()

  def register(): String = {

    if (isStudent() && verify() && checkIfUserIsNotRegistered()) {

      mongodbObject.insert(map, "studentUsers")

      return "Student Registered"
    }

    if (isAdmin() && verify() && checkIfUserIsNotRegistered()) {

      mongodbObject.insert(map, "adminUsers")

      return "Admin Registered"
    }

    if (isNgo() && verify() && checkIfUserIsNotRegistered()) {

      mongodbObject.insert(map, "ngoUsers")

      return "Ngo Registered"

    }

    "No User Type found"
  }

  def verify(): Boolean = {

    if (isStudent() && map.get("emailId").get.matches("^[a-zA-Z0-9_.+-]+@students.itu.edu")) return true

    if (isAdmin() && map.get("emailId").get.matches("^[a-zA-Z0-9_.+-]+@itu.edu")) return true

    if (isNgo() && map.get("emailId").get.matches("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")) return true

    false
  }

  def checkIfUserIsNotRegistered(): Boolean = {

    var searchUser = Map[String, String]()

    searchUser += "username" -> map.get("username").get
    searchUser += "emailId" -> map.get("emailId").get

    val checkForUsername = mongodbObject.get(searchUser - "emailId", userType.get(map.get("userType").get).get)

    val checkForEmail = mongodbObject.get(searchUser - "username", userType.get(map.get("userType").get).get)

    checkForUsername.isEmpty  && checkForEmail.isEmpty
  }


  def isStudent(): Boolean = {
    if (map.get("userType").get == "student") return true

    false
  }

  def isAdmin(): Boolean = {
    if (map.get("userType").get == "admin") return true

    false
  }

  def isNgo(): Boolean = {
    if (map.get("userType").get == "ngo") return true

    false
  }

  def setMap(map: Map[String, String]) {
    this.map = map
  }

}
