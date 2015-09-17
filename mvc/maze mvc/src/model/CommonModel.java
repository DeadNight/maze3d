package model;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import controller.Controller;

public abstract class CommonModel implements Model {
	Controller controller;
	ExecutorService threadPool;
	
	public CommonModel(Controller controller, int poolSize) {
		this.controller = controller;
		threadPool = Executors.newFixedThreadPool(poolSize);
	}
}
