package org.acme.quickstart;

import java.io.File;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

@Path("/app")
public class GreetingResource {

	@Inject
	private GreetingCryptoService cryptoService;

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

	@POST
	@Path("/send")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public String sendEmailWithAttachment(MultipartFormDataInput request) {
		try {
			Map<String, List<InputPart>> uploadForm = request.getFormDataMap();
			String password = "teste";
			List<InputPart> inputParts = uploadForm.get("file");
			byte[] attachedFile = helper.getAttachedFile(inputParts);
			byte[] encripted = cryptoService.encryptData(password, attachedFile);
			String mail = "marcos.avc@gmail.com";
			mailService.send(mail, encripted);

		} catch (InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException
				| InvalidAlgorithmParameterException | BadPaddingException | IllegalBlockSizeException
				| InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return "sucesso ao enviar e-mail";
	}

	@POST
	@Path("/receive")
	@Produces(MediaType.MULTIPART_FORM_DATA)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response receive(MultipartFormDataInput request) {
		byte[] decrypted = {};
		try {
			Map<String, List<InputPart>> uploadForm = request.getFormDataMap();
			String password = "teste";
			List<InputPart> inputParts = uploadForm.get("file");
			byte[] attachedFile = helper.getAttachedFile(inputParts);
			decrypted = cryptoService.decryptData(password, attachedFile);
		} catch (InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException
				| InvalidAlgorithmParameterException | BadPaddingException | IllegalBlockSizeException
				| InvalidKeySpecException e) {
			e.printStackTrace();
		}

		return Response.status(201).entity(decrypted).build();
	}

}