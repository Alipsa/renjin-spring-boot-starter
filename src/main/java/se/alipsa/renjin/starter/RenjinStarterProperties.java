package se.alipsa.renjin.starter;

import org.renjin.primitives.packaging.ClasspathPackageLoader;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "se.alipsa.renjin.starter")
public class RenjinStarterProperties {

  String packageLoader = ClasspathPackageLoader.class.getSimpleName();
  boolean excludeDefaultPackages;

  public String getPackageLoader() {
    return packageLoader;
  }

  /**
   * Set the package loader to be used by the ScriptEngine
   * Valid values are ClasspathPackageLoader (which uses the available spring boot dependencies classpath)
   * and AetherPackageLoader (which searches for dependencies in maven central and bedatadriven and downloads them
   * dynamically)
   * @param packageLoader the package loader to be used by the ScriptEngine
   */
  public void setPackageLoader(String packageLoader) {
    this.packageLoader = packageLoader;
  }

  public boolean isExcludeDefaultPackages() {
    return excludeDefaultPackages;
  }

  public void setExcludeDefaultPackages(boolean excludeDefaultPackages) {
    this.excludeDefaultPackages = excludeDefaultPackages;
  }
}
