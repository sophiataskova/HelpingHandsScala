package main.scala.rest.model

class EnrollEvents {

  val mongoConnection = new MongodbConnection()

  def enrollAsParticipant(event: Map[String, String]): Boolean = {

    val valueInDbForUsersParticipating = mongoConnection
      .getValue(event, "usersParticipating", "createEvents")
      .toString
      .toInt

    var updateFields = Map[String, String]()

    updateFields += "usersParticipating" -> (valueInDbForUsersParticipating + 1).toString

    mongoConnection.update(event, updateFields, "createEvents")

  }

  def enrollAsVolunteer(event: Map[String, String], user: Map[String, String]): Boolean = {

    //    val valueInDbForVolunteersRequired = mongoConnection.getValue(event,"volunteersRequired","createEvents").toString.toInt

    val valueInDbForVolunteersInterested = mongoConnection.getValue(event, "volunteersInterested", "createEvents").toString.toInt

    var updateFields = Map[String, String]()

    updateFields += "volunteersInterested" -> (valueInDbForVolunteersInterested + 1).toString

    //    updateFields += "usersParticipating" -> (valueInDbForVolunteersRequired - 1).toString()

    //    var addContactToVolunteers:(String, Map[String,String]

    val addContactToVolunteers = "volunteersContact" -> user

    mongoConnection.updateEvent(event, addContactToVolunteers, "createEvents")

    mongoConnection.update(event, updateFields, "createEvents")

  }

  def approveEvent(event: Map[String, String]): Boolean = {

    var updateFields = Map[String, String]()

    updateFields += "expired" -> "false"
    updateFields += "isActive" -> "true"
    updateFields += "isAwaitingApproval" -> "false"
    updateFields += "isDenied" -> "false"

    mongoConnection.update(event, updateFields, "createEvents")
  }

  def denyEvent(event: Map[String, String]): Boolean = {
    var updateFields = Map[String, String]()

    updateFields += "expired" -> "true"
    updateFields += "isActive" -> "false"
    updateFields += "isAwaitingApproval" -> "false"
    updateFields += "isDenied" -> "true"

    mongoConnection.update(event, updateFields, "createEvents")
  }

  def expireEvent(event: Map[String, String]): Boolean = {
    var updateFields = Map[String, String]()

    updateFields += "expired" -> "true"
    updateFields += "isActive" -> "false"
    updateFields += "isAwaitingApproval" -> "false"
    updateFields += "isDenied" -> "false"

    mongoConnection.update(event, updateFields, "createEvents")
  }

}
