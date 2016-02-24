package com.github.fengtan.solrgui;

public class SolrGUIExceptionHandler implements Thread.UncaughtExceptionHandler {

	@Override
	public void uncaughtException(Thread t, Throwable e) {
System.out.println("bouh"); //TODO 
	}

}
