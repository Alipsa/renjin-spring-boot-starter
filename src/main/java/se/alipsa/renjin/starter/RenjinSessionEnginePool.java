package se.alipsa.renjin.starter;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.renjin.script.RenjinScriptEngine;


public class RenjinSessionEnginePool extends GenericObjectPool<RenjinScriptEngine>{

  public RenjinSessionEnginePool(RenjinSessionEnginePoolFactory poolFactory) {
    super(poolFactory);
  }


}
