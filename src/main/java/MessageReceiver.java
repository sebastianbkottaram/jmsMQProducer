import javax.jms.*;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class MessageReceiver implements MessageListener {

    private MessageConsumer consumer;
    private Session session;
    private Connection con;

    String userName;
    String passwd;
    String topicName;
    String port;
    String brokerURL;
    String url;

    public void startListener () throws JMSException {
        authConfigDetails() ;
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61619");
        connectionFactory.setPassword("abc123");
        connectionFactory.setUserName("seb");
        this.con = connectionFactory.createConnection();
        con.start();

        // Creating session for seding messages
        //Creating a non transactional session to send/receive JMS message.
        this.session = con.createSession(false,
                Session.AUTO_ACKNOWLEDGE);

        Topic prodtopic = session.createTopic(topicName.trim());

        // MessageConsumer is used for receiving (consuming) messages
        this.consumer = session.createConsumer(prodtopic);
        consumer.setMessageListener(this);
    }

    private void authConfigDetails() {
        userName = JMSConsumer.prop.getProperty("userName");
        passwd = JMSConsumer.prop.getProperty("password");
        topicName = JMSConsumer.prop.getProperty("topicName");
        url = JMSConsumer.prop.getProperty("IPAddress");
        port = JMSConsumer.prop.getProperty("PortNo");
        brokerURL = JMSConsumer.prop.getProperty("BrokerURL");
    }

    @Override
    public void onMessage (Message message) {
        if (message instanceof TextMessage) {
            TextMessage tm = (TextMessage) message;
            try {

                System.out.printf("Message received: %s, Thread: %s%n",
                        tm.getText(),
                        Thread.currentThread().getName());
            } catch (JMSException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void destroy () throws JMSException {
        con.close();
    }
}
