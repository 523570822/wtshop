package com.wtshop.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Utils - 压缩/解压缩
 * 
 * 
 */
public final class CompressUtils {

	/**
	 * 不可实例化
	 */
	private CompressUtils() {
	}

	/**
	 * 压缩
	 * 
	 * @param srcFiles
	 *            源文件
	 * @param destFile
	 *            目标文件
	 * @param archiverName
	 *            压缩方式
	 */
	public static void archive(File[] srcFiles, File destFile, String archiverName) {
		Assert.notNull(destFile);
		Assert.state(!destFile.exists() || destFile.isFile());
		Assert.hasText(archiverName);

		File parentFile = destFile.getParentFile();
		if (parentFile != null) {
			parentFile.mkdirs();
		}
		ArchiveOutputStream archiveOutputStream = null;
		try {
			archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(archiverName, new BufferedOutputStream(new FileOutputStream(destFile)));
			if (ArrayUtils.isNotEmpty(srcFiles)) {
				for (File srcFile : srcFiles) {
					if (srcFile == null || !srcFile.exists()) {
						continue;
					}
					Set<File> files = new HashSet<File>();
					if (srcFile.isFile()) {
						files.add(srcFile);
					}
					if (srcFile.isDirectory()) {
						files.addAll(FileUtils.listFilesAndDirs(srcFile, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE));
					}
					String basePath = FilenameUtils.getFullPath(srcFile.getCanonicalPath());
					for (File file : files) {
						try {
							String entryName = FilenameUtils.separatorsToUnix(StringUtils.substring(file.getCanonicalPath(), basePath.length()));
							ArchiveEntry archiveEntry = archiveOutputStream.createArchiveEntry(file, entryName);
							archiveOutputStream.putArchiveEntry(archiveEntry);
							if (file.isFile()) {
								InputStream inputStream = null;
								try {
									inputStream = new BufferedInputStream(new FileInputStream(file));
									IOUtils.copy(inputStream, archiveOutputStream);
								} catch (FileNotFoundException e) {
									throw new RuntimeException(e.getMessage(), e);
								} catch (IOException e) {
									throw new RuntimeException(e.getMessage(), e);
								} finally {
									IOUtils.closeQuietly(inputStream);
								}
							}
						} catch (IOException e) {
							throw new RuntimeException(e.getMessage(), e);
						} finally {
							archiveOutputStream.closeArchiveEntry();
						}
					}
				}
			}
		} catch (ArchiveException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			IOUtils.closeQuietly(archiveOutputStream);
		}
	}

	/**
	 * 压缩
	 * 
	 * @param srcFile
	 *            源文件
	 * @param destFile
	 *            目标文件
	 * @param archiverName
	 *            压缩方式
	 */
	public static void archive(File srcFile, File destFile, String archiverName) {
		archive(new File[] { srcFile }, destFile, archiverName);
	}

	/**
	 * 解压缩
	 * 
	 * @param srcFile
	 *            源文件
	 * @param destDir
	 *            目标目录
	 */
	public static void extract(File srcFile, File destDir) {
		Assert.notNull(srcFile);
		Assert.state(srcFile.exists());
		Assert.state(srcFile.isFile());
		Assert.notNull(destDir);
		Assert.state(!destDir.exists() || destDir.isDirectory());

		destDir.mkdirs();
		ArchiveInputStream archiveInputStream = null;
		try {
			archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(new BufferedInputStream(new FileInputStream(srcFile)));
			ArchiveEntry archiveEntry;
			while ((archiveEntry = archiveInputStream.getNextEntry()) != null) {
				if (archiveEntry.isDirectory()) {
					new File(destDir, archiveEntry.getName()).mkdirs();
				} else {
					OutputStream outputStream = null;
					try {
						outputStream = new BufferedOutputStream(new FileOutputStream(new File(destDir, archiveEntry.getName())));
						IOUtils.copy(archiveInputStream, outputStream);
					} catch (FileNotFoundException e) {
						throw new RuntimeException(e.getMessage(), e);
					} catch (IOException e) {
						throw new RuntimeException(e.getMessage(), e);
					} finally {
						IOUtils.closeQuietly(outputStream);
					}
				}
			}
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (ArchiveException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			IOUtils.closeQuietly(archiveInputStream);
		}
	}

}