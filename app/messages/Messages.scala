package messages

/**
 * Created by rahul on 18/10/15.
 */


case class Signup(name: String,username: String,emailid: String,password: String)
case class User(_id: String,emailid: String,password: String)

object JsonFormats {

  import play.api.libs.json.Json
  import play.api.data._
  import play.api.data.Forms._

  // Generates Writes and Reads for Feed and User thanks to Json Macros
  implicit val signupFormat = Json.format[Signup]
  implicit val userFormat = Json.format[User]
}