package main.scala.rest.model

class Events {

  val mongodbConnection = new MongodbConnection()

  def createEvent(map: Map[String, String]): String = {
    var updateMap = map

    updateMap += "expired" -> "false"
    updateMap += "isActive" -> "false"
    updateMap += "isAwaitingApproval" -> "true"
    updateMap += "isDenied" -> "false"

    mongodbConnection.insert(updateMap, "createEvents")

    "New Event Created"
  }

  def deleteEvent(map: Map[String, String]): String = {
    mongodbConnection.delete(map, "createEvents")

    "Event Deleted"
  }

  def searchKeywords(searchString: String) = {

    var searchTokens:String = ""

    searchString.split(" ").foreach(x => searchTokens = searchTokens.concat("[a-zA-Z0-9 ]*" + x))

    searchTokens=searchTokens.concat("[a-zA-Z0-9 ]*")

    mongodbConnection.searchEvents(searchTokens,"createEvents")

  }

}
