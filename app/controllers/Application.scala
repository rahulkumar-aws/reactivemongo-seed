package controllers

import java.util.concurrent.TimeoutException
import javax.inject.Inject

import messages.{User, Signup}
import messages.JsonFormats.signupFormat
import messages.JsonFormats.userFormat
import play.api._
import play.api.i18n.MessagesApi
import play.api.libs.json.{JsError, Json, Writes}
import play.api.mvc._
import play.modules.reactivemongo.json.collection.JSONCollection
import play.modules.reactivemongo.{ReactiveMongoComponents, MongoController, ReactiveMongoApi}
import reactivemongo.bson.BSONObjectID
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future
import scala.util.{Success, Failure}

class Application @Inject() (
                              val reactiveMongoApi: ReactiveMongoApi,
                              val messagesApi: MessagesApi) extends Controller
                              with MongoController with ReactiveMongoComponents {


  def collection: JSONCollection = db.collection[JSONCollection]("jungly")

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }


  implicit val userWrites = new Writes[User] {
    def writes(user: User) = Json.obj(
      "_id" -> user._id,
      "emailid" -> user.emailid,
      "password" -> user.password
    )
  }

  def signup = Action.async(parse.json) { request =>

      val reqiestdata  = request.body.validate[Signup]

    reqiestdata.fold(
    error => Future.successful(
    Ok("Bad JSON !!")
    ),
    signup => {

      val user = User(signup.username,signup.emailid,signup.password)
      collection.insert(user).map({
        case _ =>Ok("data Inserted")}).recover{ case _ => Ok("Username exist")}
    }
    )
  }

}
