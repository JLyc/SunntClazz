package file_analyzer;

import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

/**
 * Created by JLyc on 4/23/2016.
 */
public class ExtractEarFiles implements Closeable {

    private Set<File> extractedFiles = new HashSet<>();
    private Set<String> extractedDirs = new HashSet<>();
    private List<Path> zipFiles;
    private List<Path> processes;
    private List<Path> jdbcs;
    private Path path;

    PathMatcher archivesPattern = FileSystems.getDefault().getPathMatcher("glob:**.{ear,sar}");
    PathMatcher processesPattern = FileSystems.getDefault().getPathMatcher("glob:**.{process}");
    PathMatcher jdbcPattern = FileSystems.getDefault().getPathMatcher("glob:**.{sharedjdbc}");

    public ExtractEarFiles(Path path) throws IOException {
        this.path = path;
        zipFiles = getAllFiles(path, archivesPattern);

        for (Path subPath : zipFiles) {
            extractFolder(subPath.toString());
        }
    }

    public List<Path> getZipFiles() {
        return zipFiles;
    }

    public List<Path> getProcesses(Path path) throws IOException {
        if (processes == null) {
            processes = getAllFiles(removeExtension(path), processesPattern);
        }
        return processes;
    }

    public List<Path> getJDBCs(Path path) throws IOException {
        if (jdbcs == null) {
            jdbcs = getAllFiles(removeExtension(path), jdbcPattern);
        }
        return jdbcs;
    }

    private List<Path> getAllFiles(Path path, final PathMatcher matcher) throws IOException {
        return Files.walk(path).filter(new Predicate<Path>() {
            @Override
            public boolean test(Path path) {
                return matcher.matches(path);
            }
        }).collect(Collectors.<Path>toList());
    }

    public void extractFolder(String zipFile) throws ZipException, IOException {
        int BUFFER = 2048;

        ZipFile zip = new ZipFile(new File(zipFile));
        String newPath = zipFile.substring(0, zipFile.length() - 4);

        Files.createDirectories(Paths.get(newPath));

        Enumeration<?> zipFileEntries = zip.entries();
        while (zipFileEntries.hasMoreElements()) {
            ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();
            String currentEntry = entry.getName();
            File destFile = new File(newPath, currentEntry);

            File destinationParent = destFile.getParentFile();
            destinationParent.mkdirs();

            extractedFiles.add(destFile);

            if (!entry.isDirectory()) {
                writeFile(BUFFER, zip, entry, destFile);
            }

            if (currentEntry.endsWith(".par") || currentEntry.endsWith(".sar")) {
                extractFolder(destFile.getAbsolutePath());
            }
        }
        zip.close();
    }

    private void writeFile(int BUFFER, ZipFile zip, ZipEntry entry, File destFile) throws IOException {
        BufferedInputStream is = new BufferedInputStream(zip.getInputStream(entry));

        BufferedOutputStream dest = new BufferedOutputStream(new FileOutputStream(destFile), BUFFER);

        int currentByte;
        byte data[] = new byte[BUFFER];
        while ((currentByte = is.read(data, 0, BUFFER)) != -1) {
            dest.write(data, 0, currentByte);
        }
        dest.flush();
        dest.close();
        is.close();
    }

    private void walkTreeDelete(List<Path> paths) {
        for (Path path : paths) {
            try {
                Files.walkFileTree(removeExtension(path), new SimpleFileVisitor<Path>() {

                    @Override
                    public FileVisitResult postVisitDirectory(Path file, IOException e) throws IOException {
                        Files.delete(file);
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult visitFile(Path dir, BasicFileAttributes e) throws IOException {
                        Files.delete(dir);
                        return FileVisitResult.CONTINUE;
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Path removeExtension(Path path) {
        return Paths.get(FilenameUtils.removeExtension(String.valueOf(path)));
    }

    @Override
    public void close() throws IOException {
        walkTreeDelete(zipFiles);
    }
}
