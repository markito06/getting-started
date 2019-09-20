package br.ufsc.ine.security;

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

import org.jboss.resteasy.annotations.jaxrs.PathParam;
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
	@Path("/send/{to}/{key}")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public String sendEmailWithAttachment(MultipartFormDataInput request, @PathParam("to") String to,
			@PathParam("key") String key) {
		try {
			Map<String, List<InputPart>> uploadForm = request.getFormDataMap();
	
			List<InputPart> inputPartFile = uploadForm.get("file");
			String fileName = helper.getFileName(inputPartFile);
			byte[] attachedFile = helper.getAttachedFile(inputPartFile);

			byte[] encripted = cryptoService.encryptData(key, attachedFile);
		
			mailService.send(to, encripted, fileName);

		} catch (InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException
				| InvalidAlgorithmParameterException | BadPaddingException | IllegalBlockSizeException
				| InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return "sucesso ao enviar e-mail";
	}

	@POST
	@Path("/receive/{key}")
	@Produces(MediaType.MULTIPART_FORM_DATA)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response receive(MultipartFormDataInput request, @PathParam("key") String key) {
		byte[] decrypted = {};
		try {
			Map<String, List<InputPart>> uploadForm = request.getFormDataMap();
	
			List<InputPart> inputParts = uploadForm.get("file");
			
			byte[] attachedFile = helper.getAttachedFile(inputParts);
			decrypted = cryptoService.decryptData(key, attachedFile);
		} catch (InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException
				| InvalidAlgorithmParameterException | BadPaddingException | IllegalBlockSizeException
				| InvalidKeySpecException e) {
			e.printStackTrace();
		}

		return Response.status(201).entity(decrypted).build();
	}

}