package se.alipsa.renjin.starter;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.renjin.aether.AetherPackageLoader;
import org.renjin.eval.Session;
import org.renjin.eval.SessionBuilder;
import org.renjin.primitives.packaging.ClasspathPackageLoader;
import org.renjin.primitives.packaging.PackageLoader;
import org.renjin.script.RenjinScriptEngine;
import org.renjin.script.RenjinScriptEngineFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.script.ScriptException;

/** creates pool of RenjinScriptEngine's, each with a separate Session */
public class RenjinSessionEnginePoolFactory extends BasePooledObjectFactory<RenjinScriptEngine> {

  private final RenjinStarterProperties renjinStarterProperties;
  private final RenjinScriptEngineFactory renjinScriptEngineFactory;
  private final ClassLoader renjinClassLoader;

  public RenjinSessionEnginePoolFactory(RenjinStarterProperties renjinStarterProperties,
                                        RenjinScriptEngineFactory renjinScriptEngineFactory,
                                        ClassLoader renjinClassLoader) {
    this.renjinStarterProperties = renjinStarterProperties;
    this.renjinScriptEngineFactory = renjinScriptEngineFactory;
    this.renjinClassLoader = renjinClassLoader;
  }


  @Override
  public RenjinScriptEngine create() {
    Session session = renjinSession(new SessionBuilder(), renjinClassLoader);
    return renjinScriptEngineFactory.getScriptEngine(session);
  }

  @Override
  public PooledObject<RenjinScriptEngine> wrap(RenjinScriptEngine renjinScriptEngine) {
    return new DefaultPooledObject<>(renjinScriptEngine);
  }

  @Override
  public void passivateObject(PooledObject<RenjinScriptEngine> pooledEngine) throws ScriptException {
    pooledEngine.getObject().eval("rm(list = ls(all.names = TRUE))");
  }

  private Session renjinSession(SessionBuilder builder, ClassLoader parentClassLoader) {
    if (!renjinStarterProperties.isExcludeDefaultPackages()) {
      builder = builder.withDefaultPackages();
    }
    PackageLoader packageLoader;
    ClassLoader packageClassLoader;
    if (AetherPackageLoader.class.getSimpleName().equals(renjinStarterProperties.getPackageLoader())) {
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

}
