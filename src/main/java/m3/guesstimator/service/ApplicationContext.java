package m3.guesstimator.service;

import java.util.Map;

public interface ApplicationContext {
    String getAppHome();
    Map<String, String> getWeb();
}
