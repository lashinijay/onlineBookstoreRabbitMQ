import com.rabbitmq.client._

object Publisher {

  private val EXCHANGE_NAME = "direct_exchange"

  def publisher(method: String, uri: String, body: String): Unit = {
    val factory = new ConnectionFactory()
    factory.setHost("localhost")
    val connection = factory.newConnection()
    val channel = connection.createChannel()

    channel.exchangeDeclare(EXCHANGE_NAME, "direct")

    val content = Controller.getMessage(method, uri, body)
    Controller.checkMessage(content)
    val routingKey = content(0)
    val message = content(1)

    channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes("UTF-8"))
    println(" [x] Sent '" + routingKey + "':'" + message + "'")

    channel.close()
    connection.close()
  }
}

