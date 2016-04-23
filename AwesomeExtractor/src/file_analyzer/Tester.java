package file_analyzer;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by JLyc on 4/23/2016.
 */
public class Tester {
    public static void main(String[] args) {

//        new ExtractEarFiles(Paths.get("C:\\Java\\Projects\\SunntClazz\\AwesomeExtractor\\resources"));

        Path fs = Paths.get("C:\\Java\\Projects\\SunntClazz\\AwesomeExtractor\\resources\\OPDB.ear");
//        Path fs = Paths.get("C:\\Java\\Projects\\SunntClazz\\AwesomeExtractor\\resources\\OPDB1.zip");
//        Path fs = Paths.get("C:\\Java\\Projects\\SunntClazz\\AwesomeExtractor\\resources\\OPDB2.jar");
        fs.toFile().exists();

//        URI uri = URI.create("jar:file:" + fs.toUri().getPath());
        URI uri = URI.create(fs.toUri().getPath());
        Map<String, String> env = new HashMap<>();
        env.put("create", "true");

//        URI uri = URI.create("jar:file:/codeSamples/zipfs/zipfstest.zip");
//        FileSystem fs = FileSystems.newFileSystem(uri, env);

        Path zipfile = Paths.get("/codeSamples/zipfs/zipfstest.zip");
//        FileSystem fs = FileSystems.newFileSystem(zipfile, null);


//        try(FileSystem earfs = FileSystems.newFileSystem(uri,env, null)){
        try (FileSystem earfs = createZipFileSystem(fs, false)) {
            Path root = earfs.getPath("/");

            Files.walkFileTree(root, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {

                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    System.out.println(file);
                    return FileVisitResult.CONTINUE;
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    static PathMatcher archPattern = FileSystems.getDefault().getPathMatcher("glob:**.{ear,sar}");

    static private FileSystem createZipFileSystem(Path archFilename,boolean create)throws IOException {
        // convert the filename to a URI
        if(archPattern.matches(archFilename)){
            String fileName = archFilename.getFileName().toString();
            Path zipFileName = Paths.get(archFilename.getParent().toString(),fileName.substring(0, fileName.length() - 3),"zip");

            Files.move(archFilename, zipFileName, StandardCopyOption.ATOMIC_MOVE);
        }

//        final Path path = Paths.get(zipFilename);
//        final URI uri = URI.create("jar:file:" + path.toUri().getPath());
//
//        final Map<String, String> env = new HashMap<>();
//        if (create) {
//            env.put("create", "true");
//        }
        return FileSystems.newFileSystem(archFilename, null);
    }
}
