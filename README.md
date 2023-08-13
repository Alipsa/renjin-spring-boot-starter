# renjin-spring-boot-starter
A spring boot starter to run Renjin R code in a spring boot app

To use it, add the following dependency to your pom.xml
```xml
<dependency>
    <groupId>se.alipsa</groupId>
    <artifactId>renjin-spring-boot-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

Then, in your spring boot application, you can wire in the ScriptEngine
to execute R code. E.g. (very simplified)

Import the RenjinStarterAutoConfig in one of your configuration beans:
```java
package my.springapp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import se.alipsa.renjin.starter.RenjinStarterAutoConfig;

@Configuration
@Import(RenjinStarterAutoConfig.class)
public class BeanConfig {
  // Your other beans gos here...
}
```

Now you can just autowire a RenjinScriptEngine in your
service(s), e.g:

```java
package my.springapp;

import org.renjin.script.RenjinScriptEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.script.ScriptException;
import java.io.IOException;

@Service
public class AnalysisEngine {

  private final RenjinScriptEngine scriptEngine;

  @Autowired
  public AnalysisEngine(RenjinScriptEngine scriptEngine) {
    this.scriptEngine = scriptEngine;
  }
  
  public SEXP runScript(String script) throws ScriptException {
    return (SEXP)scriptEngine.eval(script);
  }
}
```
Since any executions in the same session share data (e.g. variables in the global environment),
in some cases you need to have a new Session for every script invocation. 
As session creation is a bit expensive, there is an Object pool of Script Engines 
with a unique session for each that you can use as follows:

```java
import org.renjin.script.RenjinScriptEngine;
import org.renjin.sexp.SEXP;
import org.springframework.beans.factory.annotation.Autowired;;
import org.springframework.stereotype.Service;
import se.alipsa.renjin.starter.RenjinSessionEnginePool;
import java.util.Map;

@Service
public class ReportEngine {
  private final RenjinSessionEnginePool renjinSessionEnginePool;

  @Autowired
  public ReportEngine(RenjinSessionEnginePool renjinSessionEnginePool) {
    this.renjinSessionEnginePool = renjinSessionEnginePool;
  }

  private SEXP runScript(String script, Map<String, Object> params) throws Exception {
    RenjinScriptEngine scriptEngine = null;
    try {
      scriptEngine = renjinSessionEnginePool.borrowObject();
      params.forEach(scriptEngine::put);
      return (SEXP) scriptEngine.eval(script);
    } finally {
      if (scriptEngine != null) {
        renjinSessionEnginePool.returnObject(scriptEngine);
      }
    }
  }
}
```
Note: When the engine is returned to the pool, all top level variables are removed with
`rm(list = ls(all.names = TRUE))` so that the next script will start with a clean 
environment.


For more comprehensive examples see https://github.com/perNyfelt/renjin-spring-boot-starter-examples

# Configuration
Default value in bold
* se.alipsa.renjin.starter.excludeDefaultPackages=true / __false__
* se.alipsa.renjin.starter.packageLoader=AetherPackageLoader / __ClasspathPackageLoader__

# Release History

### v1.0.1 in progress
- Set compile version to java 11 (was 1.8)
- Upgrade dependencies for commons pool, slf4j, junit
- Add spotbugs annotations to exclude some false positives

### v 1.0.0, 2020-12-20