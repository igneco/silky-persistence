package silky.elasticsearch

import com.sksamuel.elastic4s.ElasticClient
import org.elasticsearch.common.settings.{ImmutableSettings, Settings}
import org.elasticsearch.node.NodeBuilder.nodeBuilder

import scala.reflect.io.Directory

class EmbeddedElasticsearch(settings: Settings) {

  def this(dataDir: String, nodeName: String, httpEnabled: Boolean = false, httpPort: Int = 9200) = this(
    ImmutableSettings.settingsBuilder()
      .put("http.enabled", httpEnabled.toString)
      .put("http.port", httpPort.toString)
      .put("path.data", dataDir)
      .put("node.name", nodeName)
      .build()
  )

  private val node = nodeBuilder().local(true).settings(settings).build()

  def client: ElasticClient = new ElasticClient(node.client())

  def start(): this.type = { node.start(); this }
  def stop():  this.type = { node.close(); this }
  def clean(): this.type = { Directory(settings.get("path.data")).deleteRecursively(); this }
}
