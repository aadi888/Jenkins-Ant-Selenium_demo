import java.nio.file.Path;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

public class AccessCounter {
	public static AccessCounter accessCounter = new AccessCounter();

	private AccessCounter() {
	}

	public static AccessCounter getInstance() {
		return accessCounter;
	}

	public static HashMap<Path, Integer> hmap = new HashMap<Path, Integer>();
	public static ReentrantLock lock = new ReentrantLock();

	public void increment(Path path) {

		lock.lock();
		System.out.println("AccessCounter lock obtained at increment...");
		try {
			Integer count = hmap.get(path);
			if (count == null)
				hmap.put(path, 1);
			else {

				hmap.put(path, ++count);
			}

		} finally {
			System.out.println("AccessCounter lock released at increment...");
			lock.unlock();

		}

	}

	public Integer getCount(Path path) {

		lock.lock();
		System.out.println("AccessCounter lock obtained at getCount...");
		try {
			Integer count = hmap.get(path);
			if (count == 0)
				return 0;
			else {

				return count;
			}

		} finally {
			System.out.println("AccessCounter lock released at getCount...");
			lock.unlock();

		}

	}

}
