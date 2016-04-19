package com.proper.main;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.SynchronousQueue;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;

public class AwesomeExtractor {

	private static Set<File> extractedFiles = new HashSet<>();
	private static Set<String> extractedDirs = new HashSet<>();

	static String path = "D:\\SunntClazz\\AwesomeExtractor\\resources";


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
		System.err.println(sum/5);;;;
	}

	private static List<File> getAllZipFiles(String path) {
		File dir = new File(path);
		File[] files = dir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String filename) {
				return filename.endsWith(".zip") || filename.endsWith(".ear") || filename.endsWith(".sar");
			}
		});
		return Arrays.asList(files);
	}

	static public void extractFolder(String zipFile) throws ZipException, IOException {
		int BUFFER = 2048;

		ZipFile zip = new ZipFile(new File(zipFile));
		String newPath = zipFile.substring(0, zipFile.length() - 4);

		new File(newPath).mkdir();
		Enumeration<?> zipFileEntries = zip.entries();

		// Process each entry
		while (zipFileEntries.hasMoreElements()) {
			// grab a zip file entry
			ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();
			String currentEntry = entry.getName();
			File destFile = new File(newPath, currentEntry);
			if (destFile.isDirectory()) {
				System.out.println("DIR======================================");
			}

			// destFile = new File(newPath, destFile.getName());
			File destinationParent = destFile.getParentFile();

			// create the parent directory structure if needed
			destinationParent.mkdirs();

			extractedFiles.add(destFile);

			if (!entry.isDirectory()) {
				BufferedInputStream is = new BufferedInputStream(zip.getInputStream(entry));
				int currentByte;
				// establish buffer for writing file
				byte data[] = new byte[BUFFER];

				// write the current file to disk
				FileOutputStream fos = new FileOutputStream(destFile);
				BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER);

				// read and write until last byte is encountered
				while ((currentByte = is.read(data, 0, BUFFER)) != -1) {
					dest.write(data, 0, currentByte);
				}
				dest.flush();
				dest.close();
				is.close();
			}

			if (currentEntry.endsWith(".zip") || currentEntry.endsWith(".par") || currentEntry.endsWith(".sar")) {
				// found a zip file, try to open
				extractFolder(destFile.getAbsolutePath());
			}
		}
		zip.close();
	}

	private static long sum;
	private static void cleanUp() {
		Long currentTime = System.currentTimeMillis();
//		for (String currentFile : extractedDirs) {
//			delete(new File(currentFile));
//		}
		walkTreeDelete(extractedDirs);
		long time = System.currentTimeMillis()-currentTime;
		sum += time;
		System.out.println(time);
	}

	static void delete(File f) {
		if (f.isDirectory()) {
			for (File c : f.listFiles())
				delete(c);
		}
		if (!f.delete())
			System.out.println("Failed to delete file: " + f);
	}

	public static void walkTreeDelete(Set<String> paths) {
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
