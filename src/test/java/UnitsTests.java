import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.Nullable;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.usfirst.frc.team449.robot.MappedGenericTest;
import org.usfirst.frc.team449.robot.Robot;
import org.usfirst.frc.team449.robot.other.Mapping;
import org.usfirst.frc.team449.robot.units.Angle;
import org.usfirst.frc.team449.robot.units.Displacement;
import org.usfirst.frc.team449.robot.units.Foot;
import org.usfirst.frc.team449.robot.units.Meter;
import org.usfirst.frc.team449.robot.units.NormalizedUnitValue;
import org.usfirst.frc.team449.robot.units.Product;
import org.usfirst.frc.team449.robot.units.Quotient;
import org.usfirst.frc.team449.robot.units.Reciprocal;
import org.usfirst.frc.team449.robot.units.Time;

import java.util.Arrays;
import java.util.stream.Stream;

/*
final Product<Product<Product<Second, Ampere>, Reciprocal<Meter>>, Reciprocal<Kilogram>> test =
    new Product<>(
        new Product<>(
            new Product<>(
                new Second(1),
                new Ampere(1)),
            new Reciprocal<>(
                new Meter(1))),
        new Reciprocal<>(
            new Kilogram(1)));
 */

public class UnitsTests {
  @RunWith(Parameterized.class)
  public static class Conversions {
    private final NormalizedUnitValue value;
    private final double expectedRawValue;
    private final double expectedNormalizedValue;

    private static final double EPSILON = 0.001;

    public Conversions(final NormalizedUnitValue value, final double expectedRawValue, final double expectedNormalizedValue) {
      this.value = value;
      this.expectedRawValue = expectedRawValue;
      this.expectedNormalizedValue = expectedNormalizedValue;
    }

    @Test
    public void test() {
      System.out.println(value);

      Assert.assertEquals(this.expectedRawValue, this.value.getRawValue(), EPSILON);
      Assert.assertEquals(this.expectedNormalizedValue, this.value.getNormalizedValue(), EPSILON);
    }

    @Parameterized.Parameters(name = "{index}: {0}")
    public static Object[][] parameters() {
      return new Object[][] {
          {new Meter(0), 0, 0},
          {new Meter(7), 7, 7},
          {new Meter(7).getUnit(), 1, 1},
          {new Foot(0), 0, 0},
          {new Foot(3.2808), 3.2808, 1},
          {new Foot(0).getUnit(), 1, 0.3048},
          {new Product<>(new Meter(0), new Meter(0)), 0, 0},
          {new Product<>(new Meter(2), new Meter(3)), 6, 6},
          {new Product<>(new Foot(1), new Foot(1)), 1, 0.0929},
      };
    }
  }

  public static class Deserialization {
    @Test
    public void test() {
      Assert.assertTrue(Mapping.loadMap(UnitsDeserializationMap.class, Robot.RESOURCES_PATH.resolve("unitstest.yml")).isPresent());
    }

    @JsonIgnoreProperties("CONSTANTS")
    public static class UnitsDeserializationMap {
      // TODO Why can't the parameter extractor get parameter names for classes in the tests module?
      @JsonCreator
      public UnitsDeserializationMap(@Nullable @JsonProperty("genericTest") final MappedGenericTest genericTest,
                                     @Nullable @JsonProperty("distances") final Displacement<?>[] distances,
                                     @Nullable @JsonProperty("areas") final Product<? extends Displacement<?>, ? extends Displacement<?>>[] areas,
                                     @Nullable @JsonProperty("speeds") final Product<? extends Displacement<?>, ? extends Reciprocal<? extends Time<?>>>[] speeds,
                                     @Nullable @JsonProperty("angularSpeeds") final Quotient<? extends Angle<?>, ? extends Time<?>>[] angularSpeeds,
                                     @Nullable @JsonProperty("angularAccelerations") final Product<? extends Angle<?>, ? extends Reciprocal<? extends Product<? extends Time<?>, ? extends Time<?>>>>[] angularAccelerations,
                                     @Nullable @JsonProperty("angularAccelerations2") final Quotient<? extends Angle<?>, ? extends Product<? extends Time<?>, ? extends Time<?>>> angularAccelerations2) {
        //                      @Nullable final Quotient<? extends Angle<?>, ? extends Squared<? extends Time<?>>> angularAccelerations2) {

        Stream<NormalizedUnitValue<?>> thingsToPrint = Stream.empty();

        if (distances != null) thingsToPrint = Stream.concat(thingsToPrint, Arrays.stream(distances));
        if (areas != null) thingsToPrint = Stream.concat(thingsToPrint, Arrays.stream(areas));
        if (speeds != null) thingsToPrint = Stream.concat(thingsToPrint, Arrays.stream(speeds));
        if (angularSpeeds != null)
          thingsToPrint = Stream.concat(thingsToPrint, Arrays.stream(angularSpeeds));
        if (angularAccelerations != null)
          thingsToPrint = Stream.concat(thingsToPrint, Arrays.stream(angularAccelerations));

        thingsToPrint.map(u -> u.toString() + ": " + u.getNormalizedValue()).forEach(System.err::println);
      }
    }
  }
}
