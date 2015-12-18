package main.scala.rest.model


import org.junit.{After, Before, Test}


class EventsTest {

  val events = new Events()
  var map:Map[String,String] = Map[String,String]()

  @Before
  def init(){

    map += "eventName" -> "myFirstEvent"
    map += "organization" -> "amazinglyAwesomeOrganization"
    map += "from" -> "startDate"
    map += "to" -> "endDate"
    map += "location" -> "address"
    map += "volunteersRequired" -> "5"
    map += "usersParticipating" -> "10"
    map += "commentsPosted" -> ""
    map += "comments" -> ""

    map += "locationNearby" -> "San Francisco"
    map += "category" -> "environment"
    map += "description" -> "some description goes here"


  }

  @Test
  def itShouldCreateEventFromGivenData(){
    //given
    val expected = "New Event Created"

    //when
    val actual: String = events.createEvent(map)

    //then
    assert(actual == expected)
  }

  @Test
  def itShouldDeleteEventFromGivenData(){
    //given
    val expected = 0

    //when
     events.deleteEvent(map)

    val actual=events.mongodbConnection.get(map, "createEvents")

    //then
    assert(actual.size == expected)
    clearDb()
  }

  @Test
  def itShouldSearchEventForKeyWords(){
    //given
    val searchString = "some goes"

    //when
    events.createEvent(map)
     val actualData = events.searchKeywords(searchString)

    //then
    assert(actualData.nonEmpty)
  }

  @After
  def clearDb(){
    events.mongodbConnection.dropAllDataFrom("createEvents")
  }

}
