package main.scala.rest.model

import java.util.Date

import com.mongodb.DBObject

class UserLogin {

  val mongodbConnection = new MongodbConnection()

  private var map = Map[String, String]()


  def login(): String = {

    if ((map.get("userType").get == "student") && mongodbConnection.get(map, "studentUsers").nonEmpty) {

      return "Student Login Successful"
    }

    if ((map.get("userType").get == "admin") && mongodbConnection.get(map, "adminUsers").nonEmpty) {

      return "Admin Login Successful"
    }

    if ((map.get("userType").get == "ngo") && mongodbConnection.get(map, "ngoUsers").nonEmpty) {

      return "Ngo Login Successful"
    }

    "Wrong combination of username or password or user type"
  }

  def logout(): String = {

    if (map.get("userType").get == "student") {

      mongodbConnection.updateLastLogin(map,"studentUsers")
      return "Student Logout Successful"
    }

    if (map.get("userType").get == "admin") {

      mongodbConnection.updateLastLogin(map,"adminUsers")
      return "Admin Logout Successful"
    }

    if (map.get("userType").get == "ngo" ) {
      mongodbConnection.updateLastLogin(map,"ngoUsers")
      return "Ngo Logout Successful"
    }

    "Some error Logging out, Please try again later"
  }

  def setMap(map: Map[String, String]) = {
    this.map = map
  }

}
