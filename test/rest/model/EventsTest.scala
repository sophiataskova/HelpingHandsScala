package rest.model

import java.util.Date

import org.junit.{Before, Test}


class EventsTest {

  val events = new Events()
  var map:Map[String,String] = Map[String,String]()

  @Before
  def init(){

    map += "eventName" -> "myFirstEvent"
    map += "organization" -> "amazinglyAwesomeOrganization"
    map += "from" -> new Date(2015, 12, 10).toString
    map += "to"  -> new Date(2015,12,11).toString
    map += "location" -> "someLocation"
    map += "volunteersRequired" -> "5"
    map += "volunteersParticipating" -> "10"
    map += "description" -> "hereGoesSomeDiscriptionAboutTheEvent"
    map += "commentsPosted" -> ""
    map += "comments" -> ""
    map += "expired" -> "false"

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
  }

}
