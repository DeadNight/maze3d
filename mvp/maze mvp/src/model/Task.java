package model;

public interface Task<T> {
	T doTask();
	void handleResult(T result);
	void handleException(Exception e);
}
