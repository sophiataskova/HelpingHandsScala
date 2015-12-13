package rest.model

import scala.collection.mutable.HashMap

class UserRegistration(val map: HashMap[String, String]) {

  val mongodbObject = new MongodbConnection()

  def register(): String = {

    if (isStudent() && verify()) {

      mongodbObject.insert(map, "studentUsers")
      return "Student Registered"
    }

    if (isAdmin() && verify()) {

      mongodbObject.insert(map, "adminUsers")
      return "Admin Registered"
    }

    if (isNgo() && verify()) {

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

}
