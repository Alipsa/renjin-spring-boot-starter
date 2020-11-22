package se.alipsa.renjin.starter;

import org.renjin.aether.AetherPackageLoader;
import org.renjin.eval.Session;
import org.renjin.eval.SessionBuilder;
import org.renjin.primitives.packaging.ClasspathPackageLoader;
import org.renjin.primitives.packaging.PackageLoader;
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

  @Bean("renjinClassLoader")
  @ConditionalOnMissingBean
  public ClassLoader renjinClassLoader() {
    // TODO: add options for classloader from config
    return Thread.currentThread().getContextClassLoader();
  }

  @Bean
  @ConditionalOnMissingBean
  public Session renjinSession(SessionBuilder builder, ClassLoader parentClassLoader) {
    if (!renjinStarterProperties.isExcludeDefaultPackages()) {
      builder = builder.withDefaultPackages();
    }
    PackageLoader packageLoader;
    ClassLoader packageClassLoader;
    if (AetherPackageLoader.class.getSimpleName().equals(renjinStarterProperties.packageLoader)) {
      // TODO: add config option to add additional remote repos
      //  must be initialized on construction: new AetherPackageLoader(parentClassLoader, remoteRepositories);
      packageLoader = new AetherPackageLoader();
      packageClassLoader = ((AetherPackageLoader)packageLoader).getClassLoader();
    } else {
      packageLoader = new ClasspathPackageLoader(parentClassLoader);
      packageClassLoader = parentClassLoader;
    }
    return
      builder
          .setPackageLoader(packageLoader)
          .setClassLoader(packageClassLoader) //allows imports in r code to work
          .build();
  }

  @Bean
  @ConditionalOnMissingBean
  public RenjinScriptEngine renjinScriptEngine(RenjinScriptEngineFactory factory, Session session) {
    return factory.getScriptEngine(session);
  }

  @Bean
  @ConditionalOnMissingBean
  public RenjinSessionEnginePoolFactory renjinSessionEnginePoolFactory(RenjinStarterProperties renjinStarterProperties,
                                                                       RenjinScriptEngineFactory renjinScriptEngineFactory,
                                                                       ClassLoader renjinClassLoader) {
    return new RenjinSessionEnginePoolFactory(renjinStarterProperties, renjinScriptEngineFactory, renjinClassLoader);
  }

  @Bean
  @ConditionalOnMissingBean
  public RenjinSessionEnginePool renjinSessionEnginePool(RenjinSessionEnginePoolFactory poolFactory) {
    return new RenjinSessionEnginePool(poolFactory);
  }
}
