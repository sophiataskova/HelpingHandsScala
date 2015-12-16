package main.scala.rest.model

import java.util.Date

import com.mongodb.casbah.Imports._


class MongodbConnection() {


  val mongoCollection = MongoConnection("localhost", 27017)("helpingHandsDb")

  def insert(obj: Map[String, String], collection: String) {

    mongoCollection(collection) += obj.asDBObject

  }

  def get(obj: Map[String, String], collection: String): List[String] = {

    val resultsCursor = mongoCollection(collection).find(obj).toList
    var results = List[String]()

    resultsCursor.foreach(result =>
      results = result.toString :: results
    )
    results
  }

  def delete(map: Map[String, String], collection: String) {

    mongoCollection(collection).findAndRemove(map)

  }

  def update(map: Map[String, String], collection: String) {
//     var updateFields:Map[String,String]= Map[String,String]()

    val update = MongoDBObject(
      "$set" -> MongoDBObject("lastLogin" -> new Date().toString)
    )
//    updateFields += "lastLogin" -> new Date().toString

    mongoCollection(collection).findAndModify(map.asDBObject,update)

  }

  def dropAllDataFrom(collection: String) {
    mongoCollection(collection).drop()
  }

}
