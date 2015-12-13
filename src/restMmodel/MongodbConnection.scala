package rest.model

import com.mongodb.casbah.Imports._

import scala.collection.mutable.HashMap


class MongodbConnection() {

 val mongoCollection= MongoConnection("localhost", 27017)("helpingHandsDb")

 def insert(obj:HashMap[String,String], collection: String){

   mongoCollection(collection) += obj.asDBObject

 }

  def get(obj: HashMap[String, String], collection: String): List[String] = {

    val resultsCursor = mongoCollection(collection).find(obj).toList
    var results= List[String]()

    resultsCursor.foreach(result=>
    results = result.toString :: results
    )
    results
  }




}
