package rest.model

import org.junit.{After, Before, Test}


class MongodbConnectionTest {
  
  val mongodb= new MongodbConnection()

  var newObj:Map[String,String] = Map[String, String]()
  
  @Before
  def init() {
    newObj += "name" -> "apurv"
    newObj += "username" -> "apurvnerlekar"
    newObj += "password" -> "password"
  }
    @Test
    def itShouldInsertDataIntoMongoDb() {
      //given   
      //when
      mongodb.insert(newObj,"adminUsers")
      //then
      assert(true)
    }
  
    @Test
    def itShouldGetDataFromMongoDb() {
      //given
//      val expected ="{ \"_id\" : { \"$oid\" : \"566a9061c22871f0ac11308a\"} , \"username\" : \"apurvnerlekar\" , \"name\" : \"apurv\" , \"password\" : \"password\"}"
      mongodb.insert(newObj,"adminUsers")
      //when
      val list = mongodb.get(newObj,"adminUsers")
      
      //then
      assert(list(0).contains("\"name\" : \"apurv\""))
    }

  @After
  def clearDataFromDb() {
    mongodb.dropAllDataFrom("adminUsers")
    mongodb.dropAllDataFrom("studentUsers")
    mongodb.dropAllDataFrom("ngoUsers")

  }

}
