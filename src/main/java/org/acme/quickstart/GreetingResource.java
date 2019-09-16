package org.acme.quickstart;

import java.io.File;
import java.io.InputStream;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

@Path("/app")
public class GreetingResource {

	@Inject
	private GreetingService cryptoService;

	@Inject
	private GreetingMailService mailService;

	@Inject
	private GreetingHelper helper;

	@GET
	@Path("/hello")
	@Produces(MediaType.TEXT_PLAIN)
	public String hello() {
		return "hello";
	}

	@GET
	@Path("/attachment")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public String sendEmailWithAttachment(@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetails) {
		byte[] attach = {};

		String uploadedFileLocation = "/extra/tmp/" + fileDetails.getFileName();
		helper.save(uploadedInputStream, uploadedFileLocation);
//		 
//		File file = null;
//		cryptoService.toEncrypt(file);
//
//		String mail = null;
//		mailService.send(mail);
		return "sucesso ao enviar e-mail";
	}

}