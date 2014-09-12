package com.pract.plugin.util;

public class InvalidProjectException extends Exception{


	public InvalidProjectException(){
		super();
	}

	 public InvalidProjectException(String message) {
	        super(message);
	 }

	 public InvalidProjectException(String message, Throwable cause) {
	        super(message, cause);
	 }

	 public InvalidProjectException(Throwable cause) {
			super(cause);
		}


}
