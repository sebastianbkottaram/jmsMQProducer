import java.io.FileInputStream;
import java.util.Properties;

public class JMSConsumer {

    public static Properties prop ;

    public static void main (String[] args) throws Exception {
        FileInputStream input = new FileInputStream("/home/sebastian/Desktop/JMSProducer/connection.properties");
        prop = new Properties();
        prop.load(input);

        MessageReceiver receiver = new MessageReceiver();
        receiver.startListener();

        Thread.sleep(1000*60*3);
        receiver.destroy();

        System.out.println("Reciever has been deleted");
    }
}
