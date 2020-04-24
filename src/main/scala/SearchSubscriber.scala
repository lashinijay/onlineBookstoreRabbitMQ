import com.rabbitmq.client._

object SearchSubscriber {

  private val EXCHANGE_NAME = "direct_exchange"
  private val QUEUE_NAME2 = "search_author_title"

  def main(argv: Array[String]) {
    val factory = new ConnectionFactory()
    factory.setHost("localhost")
    val connection = factory.newConnection()
    val channel = connection.createChannel()
    channel.exchangeDeclare(EXCHANGE_NAME, "direct")
    channel.queueDeclare(QUEUE_NAME2, false, false, false, null)

    channel.queueBind(QUEUE_NAME2, EXCHANGE_NAME, "term")

    println(" [*] Waiting for messages. To exit press CTRL+C")

    val deliverCallback: DeliverCallback = (_, delivery) => {
      val message = new String(delivery.getBody, "UTF-8")
      println(" [x] Received '" + delivery.getEnvelope.getRoutingKey + "':'" + message + "'")
      val result = Services.searchByName(message)
      Controller.checkResult(result)
    }

    val cancelCallback: CancelCallback = _ => {}
    channel.basicConsume(QUEUE_NAME2, true, deliverCallback, cancelCallback)
  }
}
