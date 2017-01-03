import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import ro.visva.micro.config.ConfigManager;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
   public void test1_getParamValue() {
      
      log("test1ReadConfigParam", String.format("app.release.date=%s",
            configManager.getParamValue("String", "app", "version")));
   }
   
   @Test
   public void test2_putParam_saveToFile() {
      
      try {
         String params[] = new String[]{"app", "release", "build", "20161129"};
         log("test2_putParam_saveToFile", "Putting " + Arrays.toString(params) + " into config.");
         configManager.putParam(params);
         configManager.saveToFile();
      } catch (Exception e) {
         Assert.fail("test2_putParam_saveToFile > Error saving the config to file: " + e.getMessage());
      }
      try {
         log("test2_putParam_saveToFile", "configJsonString = " + configManager.getConfigAsJson());
      } catch (IOException e) {
         Assert.fail("test2_putParam_saveToFile > Error getting the config as JSON: " + e.getMessage());
      }
   }
   
   @Test
   /* Put a param in the config and save the config to file. */
   public void test3_putParam_saveToFile() {
      
      try {
         String params[] = new String[]{"app", "version", "0.5"};
         log("test3_putParam_saveToFile", "Putting " + Arrays.toString(params) + " into config.");
         configManager.putParam(params);
         configManager.saveToFile();
      } catch (Exception e) {
         Assert.fail("test3_putParam_saveToFile > Error saving the config to file: " + e.getMessage());
      }
      try {
         log("test3_putParam_saveToFile", "configJsonString = " + configManager.getConfigAsJson());
      } catch (IOException e) {
         Assert.fail("test3_putParam_saveToFile > Error getting the config as JSON: " + e.getMessage());
      }
   }
   
   @Test
   /* Remove a config param and save the config to file. */
   public void test4_removeParam_saveToFile() {
      
      String params[] = new String[]{"app", "version"};
      log("test4_removeParam_saveToFile", "Removing " + Arrays.toString(params) + " from config.");
      configManager.removeParam(params);
      try {
         log("test4_removeParam_saveToFile", "configJson = " + configManager.getConfigAsJson());
      } catch (IOException e) {
         Assert.fail("test4_removeParam_saveToFile > Error getting the config as JSON: " + e.getMessage());
      }
      try {
         configManager.saveToFile();
         log("test4_removeParam_saveToFile", "Config saved to file.");
      } catch (IOException e) {
         Assert.fail("test4_removeParam_saveToFile > Error saving the config to file: " + e.getMessage());
      }
   }
   
   @Test
   public void test5_writeList() {
   
      Map<String,String> releaseHistory = new HashMap<>(3);
      releaseHistory.put("2016-12-29", "1st steps");
      releaseHistory.put("2016-12-30", "v0.5 | Partially enough.");
      releaseHistory.put("2017-01-03", "v0.6 | At the time of this test.");
      releaseHistory.forEach(
            (date, description) -> {
               try {
                  configManager.putParam("app", "release", "history", date, description);
               } catch (Exception e) {
                  Assert.fail(String.format("test5_writeList > Error putting param '%s' into 'app.release.history' config: %s",
                        date, e.getMessage()));
               }
            }
      );
      try {
         log("test5_writeList", "configJson = " + configManager.getConfigAsJson());
      } catch (IOException e) {
         Assert.fail("test5_writeList > Error getting the config as JSON: " + e.getMessage());
      }
   }
   
   
   /** Utility logging. */
   private void log(String prefix, String message) {
      System.out.println(String.format("ConfigManagerTests > %s > %s", prefix, message));
   }
   
}
