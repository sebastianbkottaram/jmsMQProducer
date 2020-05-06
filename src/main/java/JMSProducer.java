import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

public class JMSProducer{

    public static Properties prop ;
    public static void main (String[] args) {

        try {
            FileInputStream input = new FileInputStream("/home/sebastian/Desktop/JMSProducer/connection.properties");
            prop = new Properties();
            prop.load(input);
            List<String> content = Files.readAllLines(Paths.get("/home/sebastian/Desktop/JMSProducer/jmsevent.txt"), StandardCharsets.UTF_8);

            MessageSender sender = new MessageSender();

            for (String jmsMessage : content) {
                sender.sendMessage(jmsMessage);
                Thread.sleep(300 * 2);
            }
            Thread.sleep(1000 * 60);
            sender.destroy();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}