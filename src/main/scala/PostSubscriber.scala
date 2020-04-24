import com.rabbitmq.client._

object PostSubscriber {

  private val EXCHANGE_NAME = "direct_exchange"
  private val QUEUE_NAME1 = "add_books"

  def main(argv: Array[String]) {
    val factory = new ConnectionFactory()
    factory.setHost("localhost")
    val connection = factory.newConnection()
    val channel = connection.createChannel()
    channel.exchangeDeclare(EXCHANGE_NAME, "direct")
    channel.queueDeclare(QUEUE_NAME1, false, false, false, null)

    channel.queueBind(QUEUE_NAME1, EXCHANGE_NAME, "add")
    println(" [*] Waiting for messages. To exit press CTRL+C")

    val deliverCallback: DeliverCallback = (_, delivery) => {
      val message = new String(delivery.getBody, "UTF-8")
      println(" [x] Received '" + delivery.getEnvelope.getRoutingKey + "':'" + message + "'")
      println(Services.add(Parser.fromJson(message)))
    }

    val cancelCallback: CancelCallback = _ => {}
    channel.basicConsume(QUEUE_NAME1, true, deliverCallback, cancelCallback)
  }
}
