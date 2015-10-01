package model;

import java.util.concurrent.ExecutionException;

public interface Task<T> {
	T doTask() throws Exception;
	void handleResult(T result);
	void handleExecutionException(ExecutionException e);
}
