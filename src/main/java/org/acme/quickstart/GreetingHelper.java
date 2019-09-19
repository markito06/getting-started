package org.acme.quickstart;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;

@ApplicationScoped
@SuppressWarnings("unused")
public class GreetingHelper {

	public static boolean checkEmail(String adress) {
		boolean isOk = true;

		return isOk;
	}


	public static boolean checkFileSize(File file) {

		boolean isOk = true;

		return isOk;
	}

	public byte [] getAttachedFile(List<InputPart> inputParts) {
		byte[] bytes = {};
		for (InputPart inputPart : inputParts) {
			try {
				MultivaluedMap<String, String> header = inputPart.getHeaders();
				// convert the uploaded file to inputstream
				InputStream inputStream = inputPart.getBody(InputStream.class, null);

				bytes = IOUtils.toByteArray(inputStream);

				System.out.println("Get file done.");

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return bytes;
	}



	public String getFileName(List<InputPart> inputParts) {
		String original = "";
		for (InputPart inputPart : inputParts) {
			try {
				MultivaluedMap<String, String> headers = inputPart.getHeaders();
				  String[] contentDispositionHeader = headers.getFirst("Content-Disposition").split(";");
			      for (String name : contentDispositionHeader) {
			        if ((name.trim().startsWith("filename"))) {
			          String[] tmp = name.split("=");
			          original = tmp[1].trim().replaceAll("\"","");          
			        }
			      }
			  	System.out.println("Get name done.");

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return original;
	}


}
