import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * A simple library class which helps with loading dynamic libraries stored in the
 * JAR archive. These libraries usually contain implementation of some methods in
 * native code (using JNI - Java Native Interface).
 */
public class NativeUtils
{

    public static final String NATIVE_FOLDER_PATH_PREFIX = "nativeutils";
    /**
     * The minimum length a prefix for a file has to have according to {@link File#createTempFile(String, String)}}.
     */
    private static final int MIN_PREFIX_LENGTH = 3;
    /**
     * Temporary directory which will contain the DLLs.
     */
    private static File temporaryDir;

    /**
     * Private constructor - this class will never be instanced
     */
    private NativeUtils()
    {
    }

    /**
     * Loads library from current JAR archive
     * <p>
     * The file from JAR is copied into system temporary directory and then loaded. The temporary file is deleted after
     * exiting.
     * Method uses String as filename because the pathname is "abstract", not system-dependent.
     *
     * @param path The path of file inside JAR as absolute path (beginning with '/'), e.g. /package/File.ext
     * @throws IOException If temporary file creation or read/write operation fails
     * @throws IllegalArgumentException If source file (param path) does not exist
     * @throws IllegalArgumentException If the path is not absolute or if the filename is shorter than three characters
     * (restriction of {@link File#createTempFile(String, String)}).
     * @throws FileNotFoundException If the file could not be found inside the JAR.
     */
    public static synchronized void loadLibraryFromJar(String path)
            throws IOException
    {

        if (null == path || !path.startsWith("/")) {
            throw new IllegalArgumentException("The path has to be absolute (start with '/').");
        }

        // Obtain filename from path
        String[] parts = path.split("/");
        String filename = (parts.length > 1) ? parts[parts.length - 1] : null;

        // Check if the filename is okay
        if (filename == null || filename.length() < MIN_PREFIX_LENGTH) {
            throw new IllegalArgumentException("The filename has to be at least 3 characters long.");
        }

        // Prepare temporary file
        if (temporaryDir == null) {
            temporaryDir = createTempDirectory(NATIVE_FOLDER_PATH_PREFIX);
            temporaryDir.deleteOnExit();
        }

        File temp = new File(temporaryDir, filename);

        try (InputStream is = NativeUtils.class.getResourceAsStream(path)) {
            Files.copy(is, temp.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
        catch (IOException e) {
            temp.delete();
            throw e;
        }
        catch (NullPointerException e) {
            temp.delete();
            throw new FileNotFoundException("File " + path + " was not found inside JAR.");
        }

        try {
            System.load(temp.getAbsolutePath());
        }
        finally {
            // Assume non-POSIX, and don't delete until last file descriptor closed
            temp.deleteOnExit();
        }
    }

    private static File createTempDirectory(String prefix)
            throws IOException
    {

        Files.walkFileTree(Paths.get(prefix), new FileVisitor<Path>()
        {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
                    throws IOException
            {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                    throws IOException
            {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc)
                    throws IOException
            {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc)
                    throws IOException
            {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });

        Path path = Files.createDirectories(Paths.get(prefix, ManagementFactory.getRuntimeMXBean().getName()));
        return path.toFile();
    }
}
