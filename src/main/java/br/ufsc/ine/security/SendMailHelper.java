package br.ufsc.ine.security;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.io.IOUtils;
import org.jboss.logging.Logger;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;

@ApplicationScoped
@SuppressWarnings("unused")
public class SendMailHelper {

	Logger logger = Logger.getLogger(SendMailHelper.class);
	private static int MAX_FILE_SIZE = 1024 * 1024 * 1;
	
	public boolean checkEmail(String adress) {
		String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
		return adress.matches(regex);
	}
	
	

	public byte [] getAttachedFile(List<InputPart> inputParts) {
		byte[] bytes = {};
		for (InputPart inputPart : inputParts) {
			try {
				MultivaluedMap<String, String> header = inputPart.getHeaders();
				// convert the uploaded file to inputstream
				InputStream inputStream = inputPart.getBody(InputStream.class, null);

				bytes = IOUtils.toByteArray(inputStream);

				logger.info("Get file is done.");

			} catch (IOException e) {
				logger.error("Get file error: " , e);
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
			      logger.info("Get name is done.");

			} catch (Exception e) {
				logger.error("Get file name error: " , e);
			}
		}
		return original;
	}

	public boolean checkSize(byte[] attachedFile) {
		return attachedFile.length < MAX_FILE_SIZE;
	}


}
