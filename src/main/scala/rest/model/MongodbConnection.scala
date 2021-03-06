package main.scala.rest.model

import java.util.Date

import com.mongodb.casbah.Imports._


class MongodbConnection() {

  val mongoCollection = MongoConnection("localhost", 27017)("helpingHandsDb")

  def insert(obj: Map[String, Any], collection: String) {

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

  def updateLastLogin(map: Map[String, String], collection: String) {

    val update = MongoDBObject(
      "$set" -> MongoDBObject("lastLogin" -> new Date().toString)
    )

    mongoCollection(collection).findAndModify(map.asDBObject, update)

  }

  def update(entry: Map[String, String], updateFields: Map[String, String], collection: String): Boolean = {

    val update = MongoDBObject(
      "$set" -> MongoDBObject(updateFields.toList)
    )

    val returnedCollection = mongoCollection(collection) findAndModify(entry asDBObject, update)

    if (returnedCollection.nonEmpty) return true

    false
  }

  def updateEvent(event: Map[String, String], updateFields: (String,Map[String, String]), collection: String): Boolean = {

    val update = MongoDBObject(
      "$addToSet" -> MongoDBObject(updateFields)
    )

    val returnedCollection = mongoCollection(collection) findAndModify(event asDBObject, update)

    if (returnedCollection.nonEmpty) return true

    false
  }

  def getValue(obj: Map[String, String], searchValue: String, collection: String): Any = {

    val resultsCursor = mongoCollection(collection).find(obj.asDBObject)

    resultsCursor.foreach(x =>
      if (x.contains(searchValue))
        return x(searchValue))

    "Value not found"
  }


  def searchEvents(searchString: String, collection: String): List[String] = {

    val search = MongoDBObject("description" -> searchString.r)

    var results = List[String]()

    mongoCollection(collection).find(search).foreach( result =>

      results = result.toString :: results

    )

    results

  }

  def dropAllDataFrom(collection: String) {
    mongoCollection(collection).drop()
  }

}
