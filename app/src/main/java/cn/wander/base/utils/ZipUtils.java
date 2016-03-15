package cn.wander.base.utils;

import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.Adler32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * @author 李建衡：jianheng.li@kuwo.cn
 * 
 */
public class ZipUtils {

	private static String TAG	= "ZipUtils";

	/**
	 * 解压文件
	 * 
	 * @param zipPath
	 * @param toPath
	 * @throws IOException
	 * 注意：压缩包中如果有文件名为中文，会失败
	 */
	public static void unCompress(String zipPath, String toPath) throws IOException {
		if (!toPath.endsWith(File.separator))
			toPath += File.separator;
		
		File destFile = new File(toPath);
		
		if (!destFile.exists())
			destFile.mkdirs();
		
		File zipfile = new File(zipPath);
		ZipInputStream zis = null;
		
		try {
			zis = new ZipInputStream(new FileInputStream(zipfile));
		} catch (FileNotFoundException fnfe) {
			throw new IOException("ZipUtils.unCompress file not found:" + zipPath);
		}
		
		ZipEntry entry = null;
		
		while ((entry = zis.getNextEntry()) != null) {
			if (entry.isDirectory()) {
				File file = new File(toPath + entry.getName() + File.separator);
				file.mkdirs();
			} else {
				File file = new File(toPath + entry.getName());
				if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
				
				FileOutputStream fos = null;
				
				try {
					fos = new FileOutputStream(file);
				} catch (FileNotFoundException fnfe) {
					// 是否抛出个异常更好呢？
					fnfe.printStackTrace();
					continue;
				}
				
				byte[] buf = null;

				try {
					buf = new byte[1024];

					int len = -1;
					while ((len = zis.read(buf, 0, 1024)) != -1) {
						fos.write(buf, 0, len);
					}
				} catch (IOException e) {
					fos.close();
					zis.close();
					throw e;
				} catch (OutOfMemoryError e) {
					fos.close();
					zis.close();
					throw new IOException("ZipUtils.unCompress out fo Memory");
				}
				
				fos.close();
			}
		}

		zis.close();
	}

	public static byte[] unzip(byte[] bytes, int start, int unzipLength) throws DataFormatException {
		ByteArrayOutputStream bos = null;
		
		try {
			bos = new ByteArrayOutputStream(bytes.length);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			return null;
		}		
			
		Inflater decompresser = new Inflater();
		decompresser.setInput(bytes, start, bytes.length - start);
		Log.d(TAG, "length : " + bytes.length + " start : " + start);

		try {
			final byte[] buf = new byte[1024];
			
			while (!decompresser.finished()) {
				int count = decompresser.inflate(buf);
				bos.write(buf, 0, count);
			}
		} catch (Throwable t) {
			return null;
		} finally {
			decompresser.end();
		}
		
		byte[] temp = null;
		
		try {
			temp = bos.toByteArray();
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			temp = null;
		}
		
		try {
			bos.close();
		} catch (IOException e) {
		}
		
		return temp;
	}

	/**
	 * 压缩单个文件
	 * 
	 * @param file
	 */
	public static void compress(File file) {
		try {
			String fileName = file.getName();
			if (fileName.indexOf(".") != -1) fileName = fileName.substring(0, fileName.indexOf("."));
			FileOutputStream f = new FileOutputStream(file.getParent() + "/" + fileName + ".zip");
			CheckedOutputStream cs = new CheckedOutputStream(f, new Adler32());
			ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(cs));
			InputStream in = new FileInputStream(file);
			out.putNextEntry(new ZipEntry(file.getName()));
			int len = -1;
			try {
				byte buf[] = new byte[1024];
				while ((len = in.read(buf, 0, 1024)) != -1)
					out.write(buf, 0, len);
			} catch (OutOfMemoryError e) {
				return;
			} finally {
				out.closeEntry();

				in.close();
				out.close();
			}
		} catch (Exception e) {
			System.err.println(e);
		}
	}

	/**
	 * 压缩文件夹
	 * 
	 * @param file
	 * @throws IOException
	 */
	public static void compressDir(File file) throws IOException {
		FileOutputStream f = new FileOutputStream(file.getParent() + file.getName() + ".zip");
		CheckedOutputStream cs = new CheckedOutputStream(f, new Adler32());
		ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(cs));

		try {
			compressDir(file, out, file.getAbsolutePath());
		} finally {
			out.flush();
			out.close();
			f.close();
		}
	}

	/**
	 * 压缩文件夹递归调用方法
	 * 
	 * @param srcFile
	 * @param out
	 * @throws IOException
	 */
	private static void compressDir(File srcFile, ZipOutputStream out, String destPath) throws IOException {
		if (srcFile.isDirectory()) {
			File subfile[] = srcFile.listFiles();
			for (int i = 0; i < subfile.length; i++) {
				compressDir(subfile[i], out, destPath);
			}
		} else {
			InputStream in = new FileInputStream(srcFile);
			String name = srcFile.getAbsolutePath().replace(destPath, "");
			if (name.startsWith("\\")) name = name.substring(1);
			ZipEntry entry = new ZipEntry(name);
			entry.setSize(srcFile.length());
			entry.setTime(srcFile.lastModified());
			out.putNextEntry(entry);
			int len = -1;

			byte[] buf = null;

			try {
				buf = new byte[1024];
				while ((len = in.read(buf, 0, 1024)) != -1)
					out.write(buf, 0, len);
			} catch (OutOfMemoryError oom) {
				oom.printStackTrace();
			}

			out.closeEntry();
			in.close();
		}
	}
}
