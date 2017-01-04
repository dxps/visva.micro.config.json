package ro.visva.micro.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.Arrays;

/**
 * Configuration Manager for managing (reading from and writing to) a JSON-based configuration.
 * @author visvadw
 */
public class ConfigManager {
   
   private static final ConfigManager instance = new ConfigManager();
   
   private String configFileName;
   private File configFile;
   
   private ObjectMapper objectMapper;
   private JsonNode configRootNode;
   
   private boolean debugEnabled;
   
   /** Get the instance of the ConfigManager */
   public static ConfigManager getInstance() {
      return instance;
   }
   
   private ConfigManager() {
      // private c'tor, as being a singleton
   }
   
   private String initError;
   private String saveError;
   
   /** Initialize the config manager.
    * @param configFileName The full path to the configuration file.
    * @return true, if init succeeds;<br/>false, otherwise.
    */
   public boolean init(String configFileName) {
      
      this.configFileName = configFileName;
      this.configFile = FileSystems.getDefault().getPath(configFileName).toFile();
      try {
         this.objectMapper = new ObjectMapper();
         configRootNode = objectMapper.readTree(configFile);
         clearInitError();
      } catch (IOException e) {
         initError = e.getMessage();
      }
      return isInited();
   }
   
   /** Tell if the configuration manager is initialized. */
   public boolean isInited() {
      return initError == null;
   }
   
   /** Internal method to clear any init error. */
   private void clearInitError() {
      initError = null;
   }
   
   /** Get the initialization error, if any. */
   public String getInitError() {
      return initError;
   }
   
   /** Get the save error of the last call to saveToFile() method, if any. */
   public String getSaveError() {
      return saveError;
   }
   
   /** Enable debuggging. This will print a couple of debug entries to the standard output. */
   public void enableDebug() {
      this.debugEnabled = true;
   }
   
   /** Disable debugging. */
   public void disableDebug() {
      this.debugEnabled = true;
   }
   
   /** Tell if debugging is enabled. */
   public boolean isDebugEnabled() {
      return debugEnabled;
   }
   
   /** Get the full path to the configuration file (provided during init). */
   public String getConfigFileName() {
      return configFileName;
   }
   
   /**
    * Get a parameter from configuration.
    * @param returnType The returning type of the parameter.
    * @param params The path to the parameter.
    * @return The value of the parameter.
    */
   public <T> T getParamValue(T returnType, String... params) {
      
      JsonNode resultNode = getConfigNodeValue(params);
      //if ((resultNode == null)) || (resultNode.isMissingNode()) return null;
      String returnTypeName = returnType.getClass().getSimpleName();
      switch (returnTypeName) {
         case "String": return (T) ((resultNode == null) ? "" : resultNode.textValue());
         case "Integer":
         case "int": return (T) ((resultNode == null) ? "" : resultNode.intValue());
         default: return (T) null;
      }
   }
   
   /**
    * Insert or update a parameter into configuration.
    * @param params The path to the parameter, the last two entries being the parameter name and value.
    * @return true, if everything went ok;<br/>false, otherwise.
    */
   public boolean putParam(String... params) {
      
      if ((params == null) || (params.length < 2)) return false;
      int argsCount = params.length;
      int currentParamIndex = 0;
      String currentParam;
      JsonNode parentNode, currentNode = configRootNode;
   
      if (debugEnabled) log("putParam", "configRootNode=" + configRootNode);
      
      while (currentParamIndex < argsCount - 1) {
         currentParam = params[currentParamIndex];
         parentNode = currentNode;
         currentNode = currentNode.path(currentParam);
         if (currentNode.isMissingNode()) {
            if (currentParamIndex < argsCount - 2) {
               ((ObjectNode) parentNode).putObject(currentParam);
            } else {
               // here, it can only be the case when currentParamIndex = argsCount - 2
               ((ObjectNode) parentNode).put(params[argsCount - 2], params[argsCount - 1]);
            }
            currentNode = parentNode.path(currentParam);
         } else {
            if (currentParamIndex == argsCount - 2) {
               ((ObjectNode) parentNode).put(params[argsCount - 2], params[argsCount - 1]);
            }
         }
         if (debugEnabled) log("putParam", "parentNode: " + parentNode);
         if (debugEnabled) log("putParam", "currentNode: " + currentNode);
         currentParamIndex++;
      }
      return true;
   }
   
   /** Remove a parameter from configuration.
    * @param params The path to the parameter in the JSON tree.
    */
   public void removeParam(String ... params) {
   
      if ((params == null) || (params.length < 1)) return;
      JsonNode node = getConfigNodeValue(Arrays.copyOf(params, params.length -1));
      if (debugEnabled) log("removeParam", "node = " + node);
      if (!node.isMissingNode()) {
         ((ObjectNode) node).remove(params[params.length - 1]);
      }
   }
   
   /**
    * Persist the configuration to the file (provided during init).
    * @return true, if everything went ok;<br/>false, otherwise.
    */
   public boolean saveToFile() {
   
      if (debugEnabled) log("saveToFile", String.format("Saving the following config to %s file:\n%s", getConfigFileName(), getConfigAsJson()));
      try {
         objectMapper.writerWithDefaultPrettyPrinter().writeValue(configFile, configRootNode);
         return true;
      } catch (IOException e) {
         saveError = e.getMessage();
         return false;
      }
   }
   
   /** Get the configuration as a JSON formatted String. */
   public String getConfigAsJson() {
      try {
         return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(configRootNode);
      } catch (JsonProcessingException e) {
         // (tbd) this should not happen ...
         System.err.println("ConfigManager > getConfigAsJson > error: " + e.getMessage());
         return "";
      }
   }
   
   /** Internal utility method to get the value of a params from the config tree. */
   private JsonNode getConfigNodeValue(String... params) {
   
      if (debugEnabled) log("getConfigNodeValue", "params = " + Arrays.toString(params));
      JsonNode resultNode = null;
      boolean firstNode = true;
      for (String p : params) {
         if (firstNode) {
            resultNode = configRootNode.get(p);
            firstNode = false;
         } else {
            resultNode = resultNode.get(p);
         }
      }
      return resultNode;
   }
   
   /** Internal utility method for logging to stdout if DEBUG flag is true. */
   private void log(String prefix, String message) {
      System.out.println(String.format("%s > %s", prefix, message));
   }
   
}
