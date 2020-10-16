package test.alipsa.renjin.starter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import se.alipsa.renjin.starter.RenjinStarterAutoConfig;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RenjinStarterAutoConfig.class)
public class SpringContextTest {

  @Test
  public void whenSpringContextIsBootstrapped_thenNoExceptions() {
  }
}
