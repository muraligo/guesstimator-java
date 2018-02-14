package m3.guesstimator.internal;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import m3.guesstimator.service.ApplicationContext;

public class GuesstimatorContext implements ApplicationContext {
	private static Logger logger = LoggerFactory.getLogger(ApplicationContext.class);

    private Map<String, String> web;
    private Map<String, String> gitbase;
    private String appHome = "";

    @Override
    public Map<String, String> getWeb() {
        return web;
    }
    public void setWeb(Map<String, String> web) {
        this.web = web;
    }

    @Override
    public String getAppHome() {
        return appHome;
    }
    public void setAppHome(String appHome) {
        this.appHome = appHome;
    }
 
    @Override
    public String toString() {
        return "Configuration [web=" + web + ", gitbase=" + gitbase + "]";
    }
 
    public static ApplicationContext loadConfig(String path, String appHome) throws IOException {
        Yaml yaml = new Yaml();
        GuesstimatorContext config = null;
        try (InputStream in = Files.newInputStream(Paths.get(path))) {
            config = yaml.loadAs(in, GuesstimatorContext.class);
            config.setAppHome(appHome);
            logger.info(config.toString());
        }
        return config;
    }
}
