package org.usfirst.frc.team449.robot.other;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.usfirst.frc.team449.robot.JavaModule;
import org.usfirst.frc.team449.robot.WPIModule;
import org.yaml.snakeyaml.Yaml;

/**
 * Facade around Jackson API.
 */
public final class Mapping {
  /**
   * Format for the reference chain (place in the map where the error occured) when a map error is
   * printed.
   */
  private static final MapErrorFormat MAP_REF_CHAIN_FORMAT = MapErrorFormat.TABLE;

  // TODO Use Util.throwFromUtilityClassConstructor() after merge
  private Mapping() { throw new AssertionError("Utility class."); }

  /**
   * Formatting for map reference chains in exceptions caused by map errors.
   */
  private enum MapErrorFormat {
    /**
     * The chain is printed as-is on one line.
     */
    NONE,
    /**
     * The chain is split up into one frame per line and left-justified.
     */
    LEFT_ALIGN,
    /**
     * The chain is split up into one frame per line and right-justified.
     */
    RIGHT_ALIGN,
    /**
     * The chain is split up into one frame per line and formatted as a table with locations to the
     * left of class names.
     */
    TABLE
  }

  static final ThreadLocal<Yaml> yaml = ThreadLocal.withInitial(Yaml::new);

  /**
   * Uses Jackson to deserialize the specified type from the specified YAML file. Prints a stack
   * trace and returns an empty optional if deserialization is unsuccessful.
   *
   * @param type a model for {@code T}
   * @param mapPath the path to the YAML file
   * @param <T> the type to deserialize
   * @return a wrapped instance of {@code T} if successful; an empty value otherwise
   */
  @NotNull
  public static <T> Optional<T> loadMap(final Class<T> type, final Path mapPath) {
    try {
      //Read the yaml file with SnakeYaml so we can use anchors and merge syntax.
      final Map<?, ?> normalized = (Map<?, ?>) yaml.get().load(new FileReader(mapPath.toFile()));

      final YAMLMapper mapper = new YAMLMapper();

      //Turn the Map read by SnakeYaml into a String so Jackson can read it.
      final String fixed = mapper.writeValueAsString(normalized);

      //Use a parameter name module so we don't have to specify name for every field.
      mapper.registerModule(new ParameterNamesModule(JsonCreator.Mode.PROPERTIES));

      //Add mix-ins
      mapper.registerModule(new WPIModule());
      mapper.registerModule(new JavaModule());

      //Deserialize the map into an object.
      return Optional.of(mapper.readValue(fixed, type));

    } catch (final IOException ex) {
      //The map file is either absent from the file system or improperly formatted.
      System.err.println("Config file is bad/nonexistent!");

      formatAndPrintJacksonException(ex);

      return Optional.empty();
    }
  }

  /**
   * Formats and prints the stack trace of an exception raised by Jackson due to a problem with the
   * map
   *
   * @param ex the exception to format and print
   */
  private static void formatAndPrintJacksonException(final IOException ex) {
    final var out = new StringWriter();
    ex.printStackTrace(new PrintWriter(out));
    final String[] lines = out.toString().split("[\\r\\n]");

    for (final String line : lines) {
      if (line.length() == 0) continue;

      final String SEP = "->";

      if (MAP_REF_CHAIN_FORMAT == MapErrorFormat.NONE || !line.contains(SEP)) {
        System.err.println(line);
        continue;
      }

      final String[] links = line.split(SEP);

      // Remove the prefix from the first link and print it separately.
      final int prefixEndIndex = links[0].lastIndexOf(':');
      final String prefix = links[0].substring(0, prefixEndIndex + 1);
      links[0] = links[0].substring(prefixEndIndex + 2);
      System.err.println(prefix);

      // Remove the suffix (a closing parenthesis) from the last link.
      final String lastLink = links[links.length - 1];
      links[links.length - 1] = lastLink.substring(0, lastLink.length() - 1);

      switch (MAP_REF_CHAIN_FORMAT) {
        case LEFT_ALIGN:
        case RIGHT_ALIGN:
          final Optional<String> longest = Arrays.stream(links).max(Comparator.comparingInt(String::length));

          final int maxLinkLength = longest.get().length();
          final String linkFormat = "\t\t->%" + (MAP_REF_CHAIN_FORMAT == MapErrorFormat.LEFT_ALIGN ? "" : maxLinkLength) + "s\n";

          for (final String s : links) {
            System.err.format(linkFormat, s);
          }
          break;

        case TABLE:
          final List<List<String>> formattedLinks = new ArrayList<>(links.length);

          for (final String link : links) {
            // Each link is of the format className[location]
            final int locationBegin = link.lastIndexOf('[');

            final String location = link.substring(locationBegin + 1, link.length() - 1);
            final String className = link.substring(0, locationBegin);

            formattedLinks.add(List.of(String.format("\t\t-> %s", location), className));
          }

          System.err.print(formatTable(formattedLinks));
          break;
      }
    }
  }

  /**
   * Converts a {@code String[][]} representation of a table to its {@code String} representation
   * where every column is the same width as the widest cell in that column.
   *
   * @param rows a {@code String[][]} containing the data of the table stored in row-major order
   * @return a {@code String} containing the result of formatting the table
   */
  private static String formatTable(final List<List<String>> rows) {
    final int columnCount = rows.get(0).size();

    final StringBuilder sb = new StringBuilder();

    // Make each column the same width as the widest cell in it.
    final int[] maxColumnWidths = new int[columnCount];
    for (final var row : rows) {
      for (int column = 0; column < columnCount; column++) {
        maxColumnWidths[column] = Math.max(maxColumnWidths[column], row.get(column).length() + 1);
      }
    }

    // Write to the StringBuilder row by row.
    for (final var row : rows) {
      for (int column = 0; column < columnCount; column++) {
        // Use the thin vertical box-drawing character.
        sb.append(String.format("%-" + maxColumnWidths[column] + "s", row.get(column)));
      }
      sb.append("\n");
    }

    return sb.toString();
  }
}
