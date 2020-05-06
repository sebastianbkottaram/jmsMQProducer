import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.activemq.broker.BrokerPlugin;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.security.AuthenticationUser;
import org.apache.activemq.security.SimpleAuthenticationPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

// JMS Provider

public class MqBroker {

    static ObjectMapper mapper;
          public static void main(String[] args) throws Exception {

              doinit();
              AuthConf authConf = loadConnectionDetails(args[0]);

            BrokerService broker = new BrokerService();
            broker.setUseJmx(true);
            broker.addConnector(authConf.getBrokerURL().trim());

              // enable authentication
              List<AuthenticationUser> users = new ArrayList<AuthenticationUser>();
              // username and password to use to connect to the broker.
              // This user has users privilege (able to browse, consume, produce, list destinations)
              users.add(new AuthenticationUser(authConf.getUserName().trim(), authConf.getPassword().trim(), "users"));
              SimpleAuthenticationPlugin plugin = new SimpleAuthenticationPlugin(users);
              BrokerPlugin[] plugins = new BrokerPlugin[]{ plugin };
              broker.setPlugins(plugins);

            broker.start();
            System.out.println("Broker Started!!!");
            // now lets wait forever to avoid the JVM terminating immediately
            Object lock = new Object();
            synchronized (lock) {
                lock.wait();
            }
        }

    private static void doinit() {
        mapper = new ObjectMapper(new YAMLFactory());
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.setVisibilityChecker(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
    }

    private static AuthConf loadConnectionDetails(String yamlPath) throws Exception {
        File file = new File(yamlPath);
        return mapper.readValue(file, AuthConf.class);
    }
 }


