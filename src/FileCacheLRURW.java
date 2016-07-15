import java.awt.image.ReplicateScaleFilter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class FileCacheLRURW extends FileCache {

	static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

	static Lock readLock = lock.readLock();
	static Lock writeLock = lock.writeLock();

	public static String replace(Path path) {

		writeLock.lock();
		System.out.println("FileCacheLRURW writer lock obtained at replace...");
		try {

			Collection<String> values = FileCache.hmap.values();
			String[] a = values.toArray(new String[0]);

			String temp[] = new String[a.length];

			for (int i = 0; i < a.length; i++) {
				temp[i] = a[i].split(" ")[3];
			}

			Arrays.sort(temp);

			Path replace = null;
			for (Map.Entry<Path, String> e : FileCache.hmap.entrySet()) {
				Path key = e.getKey();
				String value = e.getValue();
				if (value.contains(temp[0].toString()))
					replace = key;
			}
			if (replace != null) {
				System.out
						.println("\n ************************************************** \n Replaces the least recently requested file with a new file. \n");
				FileCache.hmap.remove(replace);
				String data = new Date().toString();
				FileCache.hmap.put(path, data);
				return data;

			}

		} finally {
			System.out
					.println("FileCacheLRURW writer lock released at replace...");
			writeLock.unlock();
		}

		return null;
	}

}
