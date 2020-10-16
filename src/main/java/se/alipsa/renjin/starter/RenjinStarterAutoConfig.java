package se.alipsa.renjin.starter;

import org.renjin.eval.Session;
import org.renjin.eval.SessionBuilder;
import org.renjin.script.RenjinScriptEngine;
import org.renjin.script.RenjinScriptEngineFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(RenjinScriptEngine.class)
@EnableConfigurationProperties(RenjinStarterProperties.class)
public class RenjinStarterAutoConfig {

  private final RenjinStarterProperties renjinStarterProperties;

  @Autowired
  public RenjinStarterAutoConfig(RenjinStarterProperties props) {
    renjinStarterProperties = props;
  }

  @Bean
  @ConditionalOnMissingBean
  public RenjinStarterConfig renjinStarterConfig() {
    RenjinStarterConfig config = new RenjinStarterConfig();
    // TODO add config stuff
    return config;
  }

  @Bean
  @ConditionalOnMissingBean
  public RenjinScriptEngineFactory renjinScriptEngineFactory() {
    return new RenjinScriptEngineFactory();
  }

  @Bean
  @ConditionalOnMissingBean
  public SessionBuilder sessionBuilder() {
    return new SessionBuilder();
  }

  @Bean
  @ConditionalOnMissingBean
  public ClassLoader renjinClassLoader() {
    // TODO: add options for classloader from config
    return Thread.currentThread().getContextClassLoader();
  }

  @Bean
  @ConditionalOnMissingBean
  public Session renjinSession(SessionBuilder builder, ClassLoader classLoader) {
    // TODO: .withDefaultPackages() should be added per default but omitted if config says so
    return builder
        .withDefaultPackages()
        .setClassLoader(classLoader) //allows imports in r code to work
        .build();
  }

  @Bean
  @ConditionalOnMissingBean
  public RenjinScriptEngine renjinScriptEngine(RenjinScriptEngineFactory factory, Session session) {
    return factory.getScriptEngine(session);
  }
}
