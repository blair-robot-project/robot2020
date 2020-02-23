package org.usfirst.frc.team449.robot.other.reflection;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Based on http://web.archive.org/web/20080229103411/http://snippets.dzone.com/posts/show/4831
 */
public class ReflectionUtil {
  /**
   * Scans all classes accessible from the context class loader which belong to the given package
   * and subpackages.
   *
   * @param packageName The base package
   * @return The classes
   */
  public static Stream<Class<?>> getClasses(final String packageName) {
    final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    assert classLoader != null;

    final Iterator<URL> resources;
    try {
      resources = classLoader.getResources(packageName.replace('.', '/')).asIterator();
    } catch (final IOException ex) {
      throw new UncheckedIOException(ex);
    }

    return StreamSupport.stream(Spliterators.spliteratorUnknownSize(resources, Spliterator.DISTINCT), true).flatMap(dir -> findClasses(Path.of(dir.getFile()), packageName));
  }

  /**
   * Finds all classes in a given directory and subdirectories.
   *
   * @param directory The base directory
   * @param packageName The package name for classes found inside the base directory
   * @return The classes
   */
  public static Stream<Class<?>> findClasses(final Path directory, final String packageName) {
    if (Files.notExists(directory)) return Stream.empty();

    final List<Path> files;
    try (final Stream<Path> list = Files.list(directory)) {
      files = list.collect(Collectors.toList());
    } catch (final IOException ex) {
      throw new UncheckedIOException(ex);
    }

    return files.stream().flatMap(file -> {
      final String dirOrFileName = file.getFileName().toString();

      if (Files.isDirectory(file)) {
        assert !dirOrFileName.contains(".");
        return findClasses(file, packageName + "." + dirOrFileName);

      } else if (dirOrFileName.endsWith(".class")) {
        try {
          return Stream.of(Class.forName(packageName + '.' + dirOrFileName.substring(0, dirOrFileName.length() - 6)));
        } catch (final ClassNotFoundException ex) {
          throw new RuntimeException(ex);
        }
      }

      return Stream.empty();
    });
  }
}
