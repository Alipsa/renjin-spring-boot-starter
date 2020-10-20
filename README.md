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

For more comprehensive examples see https://github.com/perNyfelt/renjin-spring-boot-starter-examples

# Configuration
TODO: add config params and descriptions here