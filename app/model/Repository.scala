package model

import com.gitblit.utils.RpcUtils
import com.gitblit.models._

import java.util.Date

import org.eclipse.jgit.lib.{Repository => JGitRepository}

/**
 * A user Repository
 */
case class Repository(user: String) {
  lazy val key = user.replace(' ', '_')
  lazy val url = s"${Repository.serverUrl}/git/$user.git"
}

object Repository {
  val serverUrl = "http://192.168.42.1/gitblit"

  private val login = "admin"
  private val password = "linus".toCharArray

  def findRepository(user: String): Option[Repository] = {
    val repo = Repository(user)
    val repoModel: RepositoryModel = RpcUtils.getRepositories(serverUrl, login, password).get(repo.key)

    if (repoModel == null) None
    else Some(repo)
  }

  def listRepositories = {
    import scala.collection.JavaConversions._
    mapAsScalaMap(RpcUtils.getRepositories(serverUrl, login, password)).keys
  }

  def createRepository(user: String) = {
    val repo = Repository(user)
    // FIXME normalize userName

    createUser(user) && createEmptyRepository(repo) && initRepository(repo)
  }

  private def createUser(user: String) = {
    val userModel = new UserModel(user)
    RpcUtils.createUser(userModel, serverUrl, login, password)
  }

  private def createEmptyRepository(repo: Repository) = {
    val model: RepositoryModel = new RepositoryModel(repo.key, "This is a test repository", repo.user, new Date(0))
    RpcUtils.createRepository(model, serverUrl, login, password)
  }

  private def initRepository(repo: Repository) = {
    val process = Runtime.getRuntime.exec(Array("/home/pi/script/setNewRepo.sh", repo.user, repo.url))
    val result = process.waitFor()
    process.destroy()
    result == 0
  }
}
