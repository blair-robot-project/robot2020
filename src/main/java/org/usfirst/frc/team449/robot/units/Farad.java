package org.usfirst.frc.team449.robot.units;

/**
 * SI electrical capacitance unit farad (F, s<sup>4</sup>·A<sup>2</sup>·m<sup>-2</sup>·kg<sup>-1</sup>).
 */
public class Farad extends Product<Product<Product<Product<Product<Product<Second, Second>, Second>, Second>, Product<Ampere, Ampere>>, Reciprocal<Product<Meter, Meter>>>, Reciprocal<Kilogram>> {
  public Farad(final double value) {
    super(
        new Product<>(
            new Product<>(
                new Quad<>(
                    new Second(
                        value)),
                new Squared<>(
                    new Ampere(1))),
            new Reciprocal<>(
                new Squared<>(
                    new Meter(1)))),
        new Reciprocal<>(
            new Kilogram(1)));
  }
}
