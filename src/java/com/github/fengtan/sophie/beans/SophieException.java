package com.github.fengtan.sophie.beans;

public class SophieException extends Exception {

	public SophieException(String msg) {
		super(msg);
	}
	
	public SophieException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
}
