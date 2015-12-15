package rest.model

import java.util.Date

import org.junit.{After, Before, Test}


class UserLoginTest {

  val userLogin= new UserLogin()

  var map: Map[String, String] = Map[String, String]()

  val mongodbConnection= new MongodbConnection()

  @Before
  def init() {

    val mongodbConnection= new MongodbConnection()

    map += "username" -> "apurvnerlekar"
    map += "password" -> "password"
    map += "lastLogin" -> new Date().toString

    map += "userType" -> "student"
    mongodbConnection.insert(map, "studentUsers")

    map += "userType" -> "admin"
    mongodbConnection.insert(map, "adminUsers")

    map += "userType" -> "ngo"
    mongodbConnection.insert(map, "ngoUsers")
  }

  @Test
  def itShouldLoginStudentWithAppropriateDetails() {
    //given
    map += "userType" -> "student"

    userLogin.setMap(map)
    val expected = "Student Login Successful"
    //when
     val actual = userLogin.login()

    //then
    assert(actual == expected)
  }

  @Test
  def itShouldLoginAdminWithAppropriateDetails() {
    //given

    map += "userType" -> "admin"

    userLogin.setMap(map)

    val expected = "Admin Login Successful"
    //when
    val actual = userLogin.login()

    //then
    assert(actual == expected)
  }

  @Test
  def itShouldLoginNGOWithAppropriateDetails() {
    //given
    map += "userType" -> "ngo"

    userLogin.setMap(map)

    val expected = "Ngo Login Successful"
    //when
    val actual = userLogin.login()

    //then
    assert(actual == expected)

  }

  @Test
  def itShouldLogoutNGOWithAppropriateDetails() {
    //given
    map += "userType" -> "ngo"

    userLogin.setMap(map)

    val expected = "Ngo Login Successful"
    //when
    val actual = userLogin.logout()

    //then
    assert(actual == expected)

  }

  @After
  def clearDb(){
    mongodbConnection.dropAllDataFrom("studentUsers")
    mongodbConnection.dropAllDataFrom("adminUsers")
    mongodbConnection.dropAllDataFrom("ngoUsers")
  }
}
