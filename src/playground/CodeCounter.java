package playground;

import java.io.*;
import java.nio.file.*;
import java.util.stream.*;

public class CodeCounter {

  private long totalLines = 0;
  private long nonEmptyLines = 0;
  private long codeLines = 0;

  public long getTotalLines() {return totalLines;}
  public long getNonEmptyLines() {return nonEmptyLines;}
  public long getCodeLines() {return codeLines;}

  public void Count (String srcPath) throws IOException {
    Path projectDir = Paths.get(srcPath); // Change to your VS project folder

    try (Stream<Path> paths = Files.walk(projectDir)) {
      for (Path file : paths.filter(Files::isRegularFile).filter(p -> p.toString().endsWith(".java")).toList()) {
        try (BufferedReader reader = Files.newBufferedReader(file)) {
          String line;
          while ((line = reader.readLine()) != null) {
            totalLines++;
            String trimmed = line.trim();
            if (!trimmed.isEmpty()) {
              nonEmptyLines++;
              if (!trimmed.startsWith("//") && !trimmed.startsWith("/*") && !trimmed.startsWith("*")) {
                codeLines++;
              }
            }
          }
        }
      }
    }
    // System.out.println("Total Lines: " + totalLines);
    // System.out.println("Non-empty Lines: " + nonEmptyLines);
    // System.out.println("Code Lines (excluding comments): " + codeLines);
  }
}
