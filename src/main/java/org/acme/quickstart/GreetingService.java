package org.acme.quickstart;

import java.io.File;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GreetingService {

	 public String greeting(String name) {
	        return "hello " + name + "\n";
	  }

	public void toEncrypt(File file) {
	
		
		
	}
}
