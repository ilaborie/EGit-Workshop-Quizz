package controllers

import play.api.mvc._
import java.util.UUID
import model._

object Application extends Controller {
  /**
   * Answers of exam
   */
  val answers = Map(
    "dvcs" -> "no",
    "jgit" -> " java",
    "offline" -> "no",
    "parent" -> "n",
    "rebase" -> "no",
    "creator" -> "linus",
    "merge" -> "no",
    "license" -> "mixed",
    "amend" -> "yes",
    "gerrit" -> "google",
    "lock" -> "no",
    "feature" -> "bisect",
    "fun" -> "yes")

  /**
   * Provide the index page
   * @return the index page
   */
  def index = Action {
    implicit request =>
      val uuid = session.get("uuid").getOrElse(UUID.randomUUID().toString)
      Ok(views.html.index(uuid))
  }

  /**
   * Compute the score
   * @param map form data
   * @return the score
   */
  private def computeScore(map: Map[String, Seq[String]]): Int =
    map.filter(entry => {
      val expected = answers.get(entry._1).getOrElse("")
      val result = entry._2.head

      expected == result
    }).size

  /**
   * Provide result page with score
   * @return the result page
   */
  def score = Action {
    request =>
      val params = request.body.asFormUrlEncoded
      params match {
        case Some(m) => {
          def param(key: String) = m.getOrElse(key, Seq("")).head

          // Check token
          val score = computeScore(m)
          val user = UserResult(request.remoteAddress, param("name"), score);
          UserResult.store(user)
          Ok(views.html.score(user, UserResult.list))
        }
        case _ => BadRequest("No results found")
      }
  }

  /**
   * Provide result page
   * @return result page
   */
  def result = Action {
    Ok(views.html.result(UserResult.list))
  }
}
