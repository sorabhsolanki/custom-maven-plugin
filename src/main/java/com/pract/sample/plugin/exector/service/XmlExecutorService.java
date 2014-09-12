package com.pract.sample.plugin.exector.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.pract.sample.java.callable.CommonFileCallable;

public class XmlExecutorService {
	private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(10);
	private static final List<Future<Long>> futures = new ArrayList<Future<Long>>();

	private static Long counter = 0L;

	public static void submitNewTask(File file){
		futures.add(EXECUTOR_SERVICE.submit(new CommonFileCallable(file)));
	}

	private static void computeLoc(){
		Iterator<Future<Long>> iterator = futures.iterator();
		while(iterator.hasNext()){
			try {
				counter += iterator.next().get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}

		//finally shutting down the executor service
		EXECUTOR_SERVICE.shutdown();
	}

	public static Long returnLoc(){
		computeLoc();
		return counter;
	}
}
