package main.scala.rest.model

import org.junit.{Before, Test}

class EnrollEventsTest {

  var map = Map[String, String]()

  @Before
  def init() {
//    {
//      "eventName": "myFirstEvent",
//      "organization": "amazinglyAwesomeOrganization",
//      "from": "",
//      "to": "",
//      "location": "someLocation",
//      "volunteersRequired": "5",
//      "usersParticipating": "10",
//      "commentsPosted": "",
//      "comments": "",
//      "expired": "false"
//
//    }
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
    map += "expired" -> "false"
    map += "isActive" -> "true"
    map += "isApproved" -> "true"
    map += "isAwaitingAcceptance" -> "false"


  }

  @Test
  def itShouldIncreaseTheCountOfAttendingPersonsForAnEvent() {

    //given

    //when

    //then
    assert(true)
  }

}
