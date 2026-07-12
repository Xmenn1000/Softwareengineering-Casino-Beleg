package casino.slots.machine;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "input")
public class SlotConfig {

    private HashMap<String, Integer> somedata;

    public HashMap<String, Integer> getSomedata() {
        return somedata;
    }

    public void setSomedata(HashMap<String, Integer> somedata) {
        this.somedata = somedata;
    }
}
