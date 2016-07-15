import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class FileCacheLFURW extends FileCache {

	static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

	static Lock readLock = lock.readLock();
	static Lock writeLock = lock.writeLock();

	public static String replace(Path path) {

		writeLock.lock();
		System.out
				.println("FileCacheLFURW  writer lock obtained at replace...");
		try {

			Collection<Integer> values = AccessCounter.hmap.values();
			Integer[] a = values.toArray(new Integer[0]);

			Arrays.sort(a);

			Path replace = null;
			for (Map.Entry<Path, Integer> e : AccessCounter.hmap.entrySet()) {
				Path key = e.getKey();
				Integer counter = e.getValue();
				if (counter == a[0])
					replace = key;
			}
			if (replace != null) {
				System.out
						.println(" \n ************************************************** \n Replaces the least frequently requested file with a new file. \n");
				FileCache.hmap.remove(replace);
				String data = new Date().toString();
				FileCache.hmap.put(path, data);
				return data;

			}

		} finally {
			System.out
					.println("FileCacheLFURW writer lock released at replace...");
			writeLock.unlock();
		}

		return null;
	}
}
