package com.proper.main;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.SynchronousQueue;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

public class AwesomeExtractor {

	private static Set<File> extractedFiles = new HashSet<>();
	private static Set<String> extractedDirs = new HashSet<>();

	static String path = "C:\\Java\\Projects\\SunntClazz\\AwesomeExtractor\\resources";

	static PathMatcher pattern = FileSystems.getDefault().getPathMatcher("glob:**.{ear,sar}");

	public static void main(String[] args) throws ZipException, IOException {
		for(int i=0; i< 5; i++) {
			List<File> zipFiles = getAllZipFiles(path);

			for (File zipFile : zipFiles) {
				int length = zipFile.getPath().length();
				extractedDirs.add(zipFile.getPath().substring(0, length - 4));
				extractFolder(zipFile.getAbsolutePath());
			}
			cleanUp();
		}
	}

	private static List<File> getAllZipFiles(String path) {
		try {
			for (Path path1 : Files.walk(Paths.get(path)).filter(new Predicate<Path>() {
                @Override
                public boolean test(Path path) {
					System.out.println(pattern.matches(path));
					boolean result = path.endsWith(".ear") || path.endsWith(".sar");
//					System.out.println(result);
					return result;
                }
            }).collect(Collectors.<Path>toList())) {
                System.out.println(path1);
            }
		} catch (IOException e) {
			e.printStackTrace();
		}

		File dir = new File(path);
		File[] files = dir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String filename) {
				return filename.endsWith(".ear") || filename.endsWith(".sar");
			}
		});
		return Arrays.asList(files);
	}

	static public void extractFolder(String zipFile) throws ZipException, IOException {
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

	private static void writeFile(int BUFFER, ZipFile zip, ZipEntry entry, File destFile) throws IOException {
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

	private static long sum;
	private static void cleanUp() {
		Long currentTime = System.currentTimeMillis();
		walkTreeDelete(extractedDirs);
		long time = System.currentTimeMillis()-currentTime;
		sum += time;
		System.out.println(time);
	}

	private static void walkTreeDelete(Set<String> paths) {
		for (String p : paths) {
			Path path = Paths.get(p);
			try {
				Files.walkFileTree(path, new SimpleFileVisitor<Path>() {

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

}
