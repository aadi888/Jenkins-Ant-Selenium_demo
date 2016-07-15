import java.nio.file.Path;
import java.util.concurrent.locks.ReentrantLock;

public class CachingCounterMethods {

	Path path;

	public CachingCounterMethods(Path path) {

		this.path = path;

	}

	public void applyFilters() {

		System.out.println("DATA \t \t " + FileCache.fetch(path));
		AccessCounter.getInstance().increment(path);
		System.out.println(Thread.currentThread().getName()
				+ " \t requested file " + path.toString() + " \t  for \t \t"
				+ AccessCounter.getInstance().getCount(path) + "times .");

	}

}
