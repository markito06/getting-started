package org.acme.quickstart;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GreetingHelper {

	
	public static boolean checkEmail(String adress) {
		boolean isOk = true;;
		
		
		return isOk;
	}
	
	
	private static boolean checkFileSize(File file) {
		
		boolean isOk = true;;
		
		return isOk;
	}
	
	public void save(InputStream uploadedInputStream, String uploadedFileLocation) {
		 
		try {
			OutputStream out = new FileOutputStream(new File(
		            uploadedFileLocation));
		      int read = 0;
		      byte[] bytes = new byte[1024];

		      out = new FileOutputStream(new File(uploadedFileLocation));
		      while ((read = uploadedInputStream.read(bytes)) != -1) {
		         out.write(bytes, 0, read);
		      }
		      out.flush();
		      out.close();
		   } catch (IOException e) {
		      e.printStackTrace();
		   }
	}
	
	
}
