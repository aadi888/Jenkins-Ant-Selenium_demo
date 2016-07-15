import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.locks.*;

public final class StaticThreadPool {
	private boolean debug = false;
	private WaitingRunnableQueue queue = null;
	public static Vector<ThreadPoolThread> availableThreads = null;
	private static StaticThreadPool pool;
	private static ReentrantLock lock = new ReentrantLock();

	private StaticThreadPool(int maxThreadNum, boolean debug) {
		this.debug = debug;
		queue = new WaitingRunnableQueue(this);
		availableThreads = new Vector<ThreadPoolThread>();
		for (int i = 0; i < maxThreadNum; i++) {
			ThreadPoolThread th = new ThreadPoolThread(this, queue, i);
			availableThreads.add(th);
			th.start();
		}

	}

	public static void shutDown() {
		lock.lock();
		try {
			System.out.println("Turning on Shut Down Feature ...");
			ThreadPoolThread.stopped = true;
			StaticThreadPool.availableThreads.forEach((ThreadPoolThread t) -> {
				t.interrupt();
			});

		} finally {
			lock.unlock();
		}
	}

	public static StaticThreadPool getInstance(int maxThreadNum, boolean debug) {
		lock.lock();
		try {
			pool = new StaticThreadPool(maxThreadNum, debug);
			return pool;
		} finally {
			lock.unlock();
		}

	}

	public void execute(Runnable runnable) {
		queue.put(runnable);
	}

	public int getWaitingRunnableQueueSize() {
		WaitingRunnableQueue.queueLock.lock();
		try {
			return queue.size();
		} finally {
			WaitingRunnableQueue.queueLock.unlock();
		}
	}

	public int getThreadPoolSize() {
		lock.lock();
		try {
			return availableThreads.size();
		} finally {
			lock.unlock();
		}
	}

	public static class WaitingRunnableQueue {
		private ArrayList<Runnable> runnables = new ArrayList<Runnable>();
		private StaticThreadPool pool;
		public static ReentrantLock queueLock;
		private Condition runnablesAvailable;

		public WaitingRunnableQueue(StaticThreadPool pool) {
			this.pool = pool;
			queueLock = new ReentrantLock();
			runnablesAvailable = queueLock.newCondition();
		}

		public int size() {
			queueLock.lock();
			try {
				return runnables.size();
			} finally {
				queueLock.unlock();
			}
		}

		public void put(Runnable obj) {
			queueLock.lock();
			try {
				runnables.add(obj);
				if (pool.debug == true)
					System.out.println("A runnable queued.");
				runnablesAvailable.signalAll();
			} finally {
				queueLock.unlock();
			}
		}

		public Runnable get() {
			queueLock.lock();
			try {
				while (runnables.isEmpty()) {
					if (pool.debug == true)
						System.out.println("Waiting for a runnable...");
					runnablesAvailable.await();
				}
				if (pool.debug == true)
					System.out.println("A runnable dequeued.");
				return runnables.remove(0);
			} catch (InterruptedException ex) {
				return null;
			} finally {
				queueLock.unlock();
			}
		}
	}

	private static class ThreadPoolThread extends Thread {
		private StaticThreadPool pool;
		private WaitingRunnableQueue queue;
		private int id;
		public static boolean stopped = false;

		public ThreadPoolThread(StaticThreadPool pool,
				WaitingRunnableQueue queue, int id) {
			this.pool = pool;
			this.queue = queue;
			this.id = id;
		}

		public void run() {

			System.out
					.println("*************THREAD POOL THREADS ***************************");
			if (pool.debug == true)
				System.out.println("Thread " + id + " starts.");
			while (stopped == false) {
				Runnable runnable = queue.get();
				if (runnable == null) {
					if (pool.debug == true)
						System.out
								.println("Thread "
										+ this.id
										+ " is being stopped due to an InterruptedException.");
					continue;
				} else {
					if (pool.debug == true)
						System.out.println("Thread " + id
								+ " executes a runnable.");
					runnable.run();
					if (pool.debug == true)
						System.out.println("ThreadPoolThread " + id
								+ " finishes executing a runnable.");
				}
			}
			System.out
					.println("*************THREAD POOL ALL THREADS STARTED ***************************");

			// if(pool.debug==true) System.out.println("Thread " + id +
			// " stops.");
		}
	}

}
