package model;

import java.util.concurrent.ExecutionException;

/**
 * @author Nir Leibovitch
 * <h1>Representation of a task</h1>
 * Specifying how to do the task, handle its result &amp; exceptions
 * @param <T> Type of the expected result of the task
 */
public interface Task<T> {
	/**
	 * Do the task and return the result of type T
	 * <br>May throw any exception
	 * @return T Result of the task
	 * @throws Exception When the task fails
	 * @see Task#handleResult(Object)
	 * @see Task#handleExecutionException(ExecutionException)
	 */
	T doTask() throws Exception;
	/**
	 * Handle the result from {@link Task#doTask()}
	 * @param result Result of the task
	 */
	void handleResult(T result);
	/**
	 * Handle any exception thrown by {@link Task#doTask()}
	 * @param e ExecutionException containing the exception thrown by {@link Task#doTask()}.
	 * <br>Use {@link ExecutionException#getCause()}
	 * @see Task#doTask()
	 * @see ExecutionException
	 * @see ExecutionException#getCause()
	 */
	void handleExecutionException(ExecutionException e);
}
