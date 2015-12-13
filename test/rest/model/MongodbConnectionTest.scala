package rest.model

import org.junit.{Before, Test}

import scala.collection.mutable.HashMap

class MongodbConnectionTest {
  
  val mongodb= new MongodbConnection()

  val newObj = new HashMap[String, String]()
  
  @Before
  def init {  
    newObj += "name" -> "apurv"
    newObj += "username" -> "apurvnerlekar"
    newObj += "password" -> "password"
  }
    @Test
    def itShouldInsertDataIntoMongoDb{
      //given   
      //when
      mongodb.insert(newObj,"adminUsers")
      //then
      assert(true)
    }
  
    @Test
    def itShouldGetDataFromMongoDb {
      //given
      val expected ="{ \"_id\" : { \"$oid\" : \"566a9061c22871f0ac11308a\"} , \"username\" : \"apurvnerlekar\" , \"name\" : \"apurv\" , \"password\" : \"password\"}"

      //when
      val list = mongodb.get(newObj,"adminUsers")
      
      //then
      assert(list(0).contains("\"name\" : \"apurv\""))
    }
}
