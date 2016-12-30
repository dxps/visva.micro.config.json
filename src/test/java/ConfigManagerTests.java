import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import ro.visva.micro.config.ConfigManager;

import java.io.IOException;
import java.util.Arrays;

/**
 * A set of simple tests to check the ConfigManager behavior.
 * @author visvadw
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ConfigManagerTests {
   
   public ConfigManager configManager;
   
   @Before
   public void beforeAnyTest() {
      // full path of the location from where the app/test started
      String baseDir = System.getProperty("user.dir");
      String fileSep = System.getProperty("file.separator");
      String configFileName = String.format("%s%s%s%s%s", baseDir, fileSep, "conf", fileSep, "config.json");
      log("\nConfigManagerTests > beforeAnyTest", "Using configFileName " + configFileName);
      configManager = ConfigManager.getInstance();
      configManager.init(configFileName);
      org.junit.Assert.assertTrue("ConfigManagerTests > beforeAnyTest > ConfigManager init error: " + configManager.getInitError(), configManager.isInited());
   }
   
   @Test
   public void test1ReadConfigParam() {
      
      log("test1ReadConfigParam", String.format("app.release.date=%s",
            configManager.getConfigParamValue("String", "app", "version")));
   }
   
   @Test
   public void test2WriteConfigParam() {
      
      try {
         String params[] = new String[]{"app", "release", "build", "20161129"};
         log("test2WriteConfigParam", "Putting " + Arrays.toString(params) + " into config.");
         configManager.putConfigParam(params);
         configManager.saveConfigToFile();
      } catch (Exception e) {
         Assert.fail("Error saving the config to file: " + e.getMessage());
      }
      try {
         log("test2WriteConfigParam", "configJsonString = " + configManager.getConfigJsonString());
      } catch (IOException e) {
         Assert.fail("Error getting the config as String: " + e.getMessage());
      }
   }
   
   @Test
   public void test3WriteConfigParam() {
      
      try {
         String params[] = new String[]{"app", "version", "0.5"};
         log("test2WriteConfigParam", "Putting " + Arrays.toString(params) + " into config.");
         configManager.putConfigParam(params);
         configManager.saveConfigToFile();
      } catch (Exception e) {
         Assert.fail("Error saving the config to file: " + e.getMessage());
      }
      try {
         log("test2WriteConfigParam", "configJsonString = " + configManager.getConfigJsonString());
      } catch (IOException e) {
         Assert.fail("Error getting the config as String: " + e.getMessage());
      }
   }
   
   @Test
   public void test4RemoveConfigParam() {
      
      String params[] = new String[]{"app", "version"};
      log("test4RemoveConfigParam", "Removing " + Arrays.toString(params) + " from config.");
      configManager.removeConfigParam(params);
      try {
         log("test4RemoveConfigParam", "configJsonString = " + configManager.getConfigJsonString());
      } catch (IOException e) {
         Assert.fail("Error getting the config as String: " + e.getMessage());
      }
      try {
         configManager.saveConfigToFile();
         log("test4RemoveConfigParam", "Config saved to file.");
      } catch (IOException e) {
         Assert.fail("Error saving the config to file: " + e.getMessage());
      }
   }
   
   /** Utility logging. */
   private void log(String prefix, String message) {
      System.out.println(String.format("ConfigManagerTests > %s > %s", prefix, message));
   }
   
}
