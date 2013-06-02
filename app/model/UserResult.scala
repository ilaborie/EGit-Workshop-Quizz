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

  /**
   * Check name
   * @param name the name
   * @return true if the name exists in the DB
   */
  def exists(name: String) = DB.withConnection {
    implicit conn =>
      val count = SQL("SELECT count(*) AS count FROM scores WHERE name={name}")
        .on("name" -> name)
        .apply()
        .head[Long]("count")

      count > 0
  }

  /**
   * Find uuid for name
   * @param name the name
   * @return the uuid if exist
   */
  def findUUID(name: String): String = DB.withConnection {
    implicit conn =>
      SQL("SELECT uuid FROM scores WHERE name={name}")
        .on("name" -> name)
        .apply()
        .head[String]("uuid")
  }

  /**
   * List all users
   * @return a list of users
   */
  def list = DB.withConnection {
    implicit conn =>
      val sqlQuery = SQL("SELECT uuid, name, score FROM scores ORDER BY score DESC")
      sqlQuery()
        .map(row => UserResult(row[String]("uuid"), row[String]("name"), row[Int]("score")))
        .toList
  }

  def clear = DB.withConnection {
    implicit conn =>
      SQL("DELETE FROM scores").executeUpdate()
  }

  /**
   * Store the user
   * @param user the user
   */
  def store(user: UserResult) {
    DB.withConnection {
      implicit conn =>
      // Try Update
        val res = SQL("UPDATE scores SET score={score}, name={name} WHERE uuid={uuid}")
          .on("score" -> user.score, "uuid" -> user.uuid, "name" -> user.name)
          .executeUpdate()

        if (res == 0) {
          // Insert new User
          SQL("INSERT INTO scores(uuid, name, score) VALUES ({uuid}, {name}, {score})")
            .on("score" -> user.score, "uuid" -> user.uuid, "name" -> user.name)
            .executeUpdate()
        }
    }
  }
}
