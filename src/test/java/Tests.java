import org.junit.Assert;
import org.junit.Test;
import org.usfirst.frc.team449.robot.Robot;

import java.util.ArrayList;
import java.util.function.Consumer;

public final class Tests {
  @org.junit.Before
  public void before() {
    Robot.notifyTesting();
  }

  @Test
  public void deserializeMap() {
    Assert.assertNotNull(Robot.loadMap());
    System.out.println("*******************************************\nMAP DESERIALIZATION SUCCESSFUL\n*******************************************");
  }

  @Test
  public void arrayListBug() {
    final ArrayList<A> list = new ArrayList<>();
    list.add(null);

    final Consumer<A> method = A::foo;

    list.forEach(method);
  }

  @Test
  public void test(){
    final Consumer<A> method = A::foo;
    method.accept(null);
  }
}

class A {
  void foo() { }
}
