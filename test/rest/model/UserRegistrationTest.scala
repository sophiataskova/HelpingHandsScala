package rest.model

import org.junit.{After, Before, Test}

class UserRegistrationTest {

  val userRegistration = new UserRegistration()

  var map: Map[String, String] = Map[String, String]()

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
  def itShouldTakeMapOfUserDetailsForRegistrationAndCheckIfTheUserIsStudent() {
    //given
    map += "emailId" -> "apurvnerlekar@students.itu.edu"
    map.apply("userType") -> "student"

    userRegistration.setMap(map)
    val expected = true

    //when
    val actual = userRegistration.isStudent()

    //then
    assert(actual, expected)
  }

  @Test
  def itShouldTakeMapOfUserDetailsForRegistrationAndCheckIfTheUserIsAdmin() {
    //given
    map += "emailId" -> "apurvnerlekar@itu.edu"
    map += "userType" -> "admin"

    userRegistration.setMap(map)
    val expected = true

    //when
    val actual = userRegistration.isAdmin()

    //then
    assert(actual == expected)
  }

  @Test
  def itShouldTakeMapOfUserDetailsForRegistrationAndCheckIfTheUserIsNgo() {
    //given
    map += "emailId" -> "apurvnerlekar@gmail.com"
    map += "userType" -> "ngo"

    userRegistration.setMap(map)

    val expected = true

    //when

    val actual = userRegistration.isNgo()

    //then
    assert(actual == expected)
  }

  @Test
  def itShouldTakeMapOfUserDetailsForRegistrationAndRegisterStudent() {
    //given
    map += "emailId" -> "apurvnerlekar@students.itu.edu"
    map += "userType" -> "student"

    userRegistration.setMap(map)

    val expected = "Student Registered"

    //when

    val actual = userRegistration.register()

    //then
    assert(actual == expected)
  }

  @Test
  def itShouldTakeMapOfUserDetailsForRegistrationAndRegisterAdmin() {
    //given
    map += "emailId" -> "apurvnerlekar@itu.edu"
    map += "userType" -> "admin"

    userRegistration.setMap(map)

    val expected = "Admin Registered"

    //when
    val actual = userRegistration.register()

    //then
    assert(actual == expected)
  }

  @Test
  def itShouldTakeMapOfUserDetailsForRegistrationAndRegisterNgo() {
    //given
    map += "emailId" -> "apurvnerlekar@gmail.com"
    map += "userType" -> "ngo"

    userRegistration.setMap(map)

    val expected = "Ngo Registered"

    //when
    val actual = userRegistration.register()

    //then
    assert(actual == expected)
  }

  @Test
  def itShouldTakeMapOfUserDetailsForRegistrationAndVerifyStudentDetails() {
    //given
    map += "emailId" -> "apurvnerlekar@students.itu.edu"
    map += "userType" -> "student"

    userRegistration.setMap(map)

    val expected = true

    //when
    val actual = userRegistration.verify()

    //then
    assert(actual == expected)
  }

  @Test
  def itShouldTakeMapOfUserDetailsForRegistrationAndCheckIfUserIsAlreadyRegistered() {
    //given
    map += "emailId" -> "apurvnerlekar@students.itu.edu"
    map += "userType" -> "student"

    userRegistration.setMap(map)

    userRegistration.register()
    val expected = false

    //when
    val actual = userRegistration.checkIfUserIsNotRegistered()

    //then
    assert(actual == expected)
  }

  @After
  def clearAllRegisteredData() {
    userRegistration.mongodbObject.dropAllDataFrom("studentUsers")
    userRegistration.mongodbObject.dropAllDataFrom("adminUsers")
    userRegistration.mongodbObject.dropAllDataFrom("ngoUsers")
  }

}
