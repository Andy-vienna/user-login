package todolistweb.version;

import org.springframework.stereotype.Component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

@Component
@PropertySource("classpath:version.properties")
public class VersionProvider {

    @Value("${app.version}")
    private String version;

    public String getVersion() {
        return version;
    }
}

