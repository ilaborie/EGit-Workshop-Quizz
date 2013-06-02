package model

import anorm._
import play.api.db.DB
import play.api.Play.current

/**
 * A user result class
 * @param name the user name
 * @param score the score
 */
case class UserResult(uuid: String, name: String, score: Int)

object UserResult {

  def exists(uuid: String, name: String) = {
    DB.withConnection {
      implicit conn =>
        val query = SQL("SELECT count(*) AS count FROM scores WHERE uuid={uuid} AND name={name}")
          .on("uuid" -> uuid, "name" -> name)
        query.apply().head[Long]("count") > 0
    }
  }

  /**
   * List all users
   * @return a list of users
   */
  def list = DB.withConnection {
    implicit conn =>
      val sqlQuery = SQL("SELECT uuid, name, score FROM scores ORDER BY score DESC")
      sqlQuery().map(
        row => UserResult(row[String]("uuid"), row[String]("name"), row[Int]("score"))
      ).toList
  }

  /**
   * Store the user
   * @param user the user
   */
  def store(user: UserResult) = DB.withConnection {
    implicit conn =>
      val res = SQL("UPDATE scores SET score={score}, name={name} WHERE uuid={uuid}")
        .on("score" -> user.score, "uuid" -> user.uuid, "name" -> user.name)
        .executeUpdate()

      if (res == 0) {
        SQL("INSERT INTO scores(uuid, name, score) VALUES ({uuid}, {name}, {score})")
          .on("score" -> user.score, "uuid" -> user.uuid, "name" -> user.name)
          .executeUpdate()
      }
  }
}
