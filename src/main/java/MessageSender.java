import javax.jms.*;

import org.apache.activemq.ActiveMQConnectionFactory;

public class MessageSender {

    //URL of the JMS server. DEFAULT_BROKER_URL will just mean that JMS server is on localhost
    private static String url = "tcp://localhost:61619";

    private MessageProducer producer;
    private Session session;
    private Connection con;

    String userName;
    String passwd;
    String topicName;
    String port;
    String brokerURL;

    public MessageSender () throws Exception {
        authConfigDetails();
        //ConnectionFactory factory =  new ActiveMQConnectionFactory("vm://localhost");
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61619");

        connectionFactory.setPassword("abc123");
        connectionFactory.setUserName("seb");
        this.con = connectionFactory.createConnection();
        con.start();

        //Creating a non transactional session to send/receive JMS message.
        this.session = con.createSession(false,
                Session.AUTO_ACKNOWLEDGE);

         Topic prodtopic = session.createTopic(topicName.trim());
        this.producer = session.createProducer(prodtopic);
    }


    private void authConfigDetails() {
        userName = JMSProducer.prop.getProperty("userName");
        passwd = JMSProducer.prop.getProperty("password");
        topicName = JMSProducer.prop.getProperty("topicName");
        url = JMSProducer.prop.getProperty("IPAddress");
        port = JMSProducer.prop.getProperty("PortNo");
        brokerURL = JMSProducer.prop.getProperty("BrokerURL");
    }

    public void sendMessage (String message) throws JMSException {
        System.out.println("Sending message: %s"+ message);
        TextMessage textMessage = session.createTextMessage(message);
       this.producer.send(textMessage);
    }

    public void destroy () throws JMSException {
        this.con.close();
    }



}