package main.scala.rest.model

class Events {

  val mongodbConnection = new MongodbConnection()

  def createEvent(map: Map[String, String]): String = {

    mongodbConnection.insert(map, "createEvents")

    "New Event Created"
  }

  def deleteEvent(map: Map[String, String]): String = {
    mongodbConnection.delete(map, "createEvents")

    "Event Deleted"
  }

  def enrollForEvent(){

  }


}
