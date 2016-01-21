package workhourscontrol.client.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.concurrent.Task;

public class TaskExecutorService {

	private static ExecutorService executorService = Executors.newCachedThreadPool(r -> {
		Thread th = new Thread(r);
		th.setDaemon(true);
		return th;
	});

	public static <T> void  executeTask(Task<T> task) {
		executorService.execute(task);
	}
}
