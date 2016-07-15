import java.nio.file.Path;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale.Category;
import java.util.Scanner;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class FileCache {
	public static HashMap<Path, String> hmap = new HashMap<Path, String>();
	public static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

	public static Lock readLock = lock.readLock();
	public static Lock writeLock = lock.writeLock();

	public static String fetch(Path path) {
		writeLock.lock();
		System.out.println("FileCache writer lock obtained at fetch...");
		String data = hmap.get(path);

		try {

			if (data == null) {
				return cacheFile(path);

			} else {
				readLock.lock();
				System.out
						.println("FileCache reader lock obtained at fetch...");
				System.out
						.println("FileCache writer lock released at replace...");
				writeLock.unlock();
				return data;

			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			/*
			 * System.out.println("FileCache reader lock released at fetch...");
			 * readLock.unlock();
			 */
			/* writeLock.unlock(); */
			/* writeLock.unlock(); */
			if (data == null) {
				System.out
						.println("FileCache writer lock released at finally...");
				writeLock.unlock();

			} else {
				System.out
						.println("FileCache reader lock released at replace...");
				readLock.unlock();
			}
		}

	}

	public static String cacheFile(Path path) {

		writeLock.lock();

		try {

			if (hmap.size() > 2) {

				return replace(path);
			} else {
				String data = new Date().toString();
				hmap.put(path, data);
				return data;
			}

		} finally {
			// System.out.println("FileCache writer lock released at cacheFile...");
			// writeLock.unlock();
			writeLock.unlock();

		}

	}

	public static String replace(Path path) {

		writeLock.lock();

		try {
			int tmp = (int) (Math.random() * 2 + 1);
			if (tmp == 1)
				return new FileCacheLFURW().replace(path);
			if (tmp == 2)
				return new FileCacheLRURW().replace(path);

			return null;

		} finally {
			/*
			 * System.out.println("FileCache writer lock released at replace...")
			 * ;
			 */
			writeLock.unlock();

		}

	}

}
