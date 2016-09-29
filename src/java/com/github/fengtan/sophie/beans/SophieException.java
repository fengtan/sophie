package com.github.fengtan.sophie.beans;

public class SophieException extends Exception {

	private static final long serialVersionUID = 1L;

	public SophieException(String msg) {
		super(msg);
	}
	
	public SophieException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
}
