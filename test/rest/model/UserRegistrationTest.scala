package rest.model

import org.junit.{Before, Test}

import scala.collection.mutable.HashMap

class UserRegistrationTest {

  var map: HashMap[String, String] = new HashMap[String, String]()

  @Before
  def init() {

    map += "firstName" -> "Apurv"
    map += "lastName" -> "Nerlekar"
    map += "username" -> "apurvnerlekar"
    map += "password" -> "password"
    map += "gender" -> "male"
    map += "emailId" -> "apurvnerlekar@students.itu.edu"
    map += "phoneNumber" -> "1234567890"
    map += "securityQuestion" -> "what is your mothers maiden name"
    map += "answer" -> "wontTellYou"
    map += "userType" -> "student"

  }

  @Test
  def itShouldTakeMapOfUserDetailsForRegistrationAndCheckIfTheUserIsStudent {
    //given
    map += "emailId" -> "apurvnerlekar@students.itu.edu"
    map.apply("userType") -> "student"

    val userRegistration = new UserRegistration(map)
    val expected = true

    //when
    val actual = userRegistration.isStudent()

    //then
    assert(actual, expected)
  }

  @Test
  def itShouldTakeMapOfUserDetailsForRegistrationAndCheckIfTheUserIsAdmin {
    //given
    map += "emailId" -> "apurvnerlekar@itu.edu"
    map += "userType" -> "admin"
    val userRegistration = new UserRegistration(map)
    val expected = true

    //when
    val actual = userRegistration.isAdmin()

    //then
    assert(actual == expected)
  }

  @Test
  def itShouldTakeMapOfUserDetailsForRegistrationAndCheckIfTheUserIsNgo {
    //given
    map += "emailId" -> "apurvnerlekar@gmail.com"
    map += "userType" -> "ngo"

    val userRegistration = new UserRegistration(map)

    val expected = true

    //when

    val actual = userRegistration.isNgo()

    //then
    assert(actual == expected)
  }

  @Test
  def itShouldTakeMapOfUserDetailsForRegistrationAndRegisterStudent {
    //given
    map += "emailId" -> "apurvnerlekar@students.itu.edu"
    map += "userType" -> "student"

    val userRegistration = new UserRegistration(map)

    val expected = "Student Registered"

    //when

    val actual = userRegistration.register()

    //then
    assert(actual == expected)
  }

  @Test
  def itShouldTakeMapOfUserDetailsForRegistrationAndRegisterAdmin {
    //given
    map += "emailId" -> "apurvnerlekar@itu.edu"
    map += "userType" -> "admin"

    val userRegistration = new UserRegistration(map)

    val expected = "Admin Registered"

    //when
    val actual = userRegistration.register()

    //then
    assert(actual == expected)
  }
 
  @Test
  def itShouldTakeMapOfUserDetailsForRegistrationAndRegisterNgo {
    //given
    map += "emailId" -> "apurvnerlekar@gmail.com"
    map += "userType" -> "ngo"

    val userRegistration = new UserRegistration(map)

    val expected = "Ngo Registered"

    //when
    val actual = userRegistration.register()

    //then
    assert(actual == expected)
  }

  @Test
  def itShouldTakeMapOfUserDetailsForRegistrationAndVerifyStudentDetails {
    //given
    map += "emailId" -> "apurvnerlekar@students.itu.edu"
    map += "userType" -> "student"

    val userRegistration = new UserRegistration(map)

    val expected = true

    //when
    val actual = userRegistration.verify()

    //then
    assert(actual == expected)
  }
}
