package app;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Utility class for zipping (compressing) the game engine for submission to
 * autograding sites.
 * <p>
 * Which portions of the project are compressed can be modified by editing
 * {@link #sources}, but this should generally be avoided. The minimum necessary
 * components are included, and adding other files/directories could
 * dramatically increase the compressed file size.
 */
public class Zipper {

    /**
     * Controls whether each filename is printed as it's compressed. Set to
     * {@code false} to disable.
     */
    private static boolean verbose = true;

    /**
     * List of files and directories that will have their contents compressed.
     */
    private static String[] sources = { "data/levels", "src", "pom.xml", "data/sprites/fallback",
            "data/backgrounds/fallback", "data/icons/default.png" };

    /**
     * Name of the compressed file that will be created. If a file already exists
     * with this name, it will be overwritten.
     */
    private static String zipPath = "CSE116-GameEngine.zip";

    /**
     * Prevent instantiation, as this class is intended to be static.
     */
    private Zipper() {
    }

    public static void main(String[] args) throws IOException {
        System.out.printf("\n*** Zipping project to \"%s\" ***\n", zipPath);

        if (Files.exists(Paths.get(zipPath))) {
            System.out.printf("* Output file \"%s\" already exists. Overwriting *\n", zipPath);
        }

        try (ZipOutputStream zipFile = new ZipOutputStream(new FileOutputStream(zipPath))) {
            for (String src : sources) {
                if (!new File(src).exists()) {
                    System.out.printf("* Source \"%s\" does not exist. Skipping * \n", src);
                    continue;
                }
                for (Path path : Files.walk(Paths.get(src)).filter(f -> !Files.isDirectory(f)).toList()) {
                    if (verbose) {
                        System.out.println(path);
                    }
                    ZipEntry zipEntry = new ZipEntry(path.toString().replace("\\", "/"));
                    zipFile.putNextEntry(zipEntry);
                    Files.copy(path, zipFile);
                    zipFile.closeEntry();
                }
            }
        } catch (IOException e) {
            System.err.println("\n*** Project unable to be compressed ***\n");
            throw e;
        }

        long size = Files.size(Paths.get(zipPath));
        System.out.printf("\n*** Project compressed successfully (%.2f MiB) ***\n", size / 1024.0 / 1024.0);
    }

}
