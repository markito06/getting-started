package br.ufsc.ine.security;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.jaxrs.PathParam;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

@Path("/app")
public class SendMailResource {
	
	Logger logger = Logger.getLogger(SendMailResource.class);
	
	private static final String MSG_SUCESS = "Arquivo enviado com sucesso.";
	private static final String MSG_ERROR = "Erro ao enviar e-mail: ";
	private static final String MSG_DECODE_ERROR = "Erro ao decifrar arquivo: ";

	
	@Inject
	private CryptoService cryptoService;

	@Inject
	private SendMailService mailService;

	@Inject
	private SendMailHelper helper;
	
	@GET
	@Path("/hello")
	@Produces(MediaType.TEXT_PLAIN)
	public String hello() {
	    return "hello";
	}

	@POST
	@Path("/send/{to}/{key}")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response sendEmailWithAttachment(MultipartFormDataInput request, @PathParam("to") String to,
			@PathParam("key") String key) {
		Response response = Response.status(200).entity(MSG_SUCESS).build();
		try {
			if(!helper.checkEmail(to)) {
				throw new Exception("Email inválido.");
			}
			
			Map<String, List<InputPart>> uploadForm = request.getFormDataMap();
			
			List<InputPart> inputPartFile = uploadForm.get("file");
			String fileName = helper.getFileName(inputPartFile);
			byte[] attachedFile = helper.getAttachedFile(inputPartFile);
			
			if(!helper.checkSize(attachedFile)) {
				throw new Exception("Arquivo muito grande. O máximo permitido é 1024k bytes");
			}

			
			byte[] encripted = cryptoService.encryptData(key, attachedFile);
		
			mailService.send(to, encripted, fileName);

		} catch (Exception  e ) {
			logger.error("Error: " , e);
			response = Response.status(500).entity(MSG_ERROR + e.getMessage()).build();
		}
		return response;
	}

	@POST
	@Path("/receive/{key}")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.MULTIPART_FORM_DATA)
	public Response receive(MultipartFormDataInput request, @PathParam("key") String key) {
		Response response = null;
		byte[] decrypted = {};
		try {
			Map<String, List<InputPart>> uploadForm = request.getFormDataMap();
	
			List<InputPart> inputParts = uploadForm.get("file");
			
			byte[] attachedFile = helper.getAttachedFile(inputParts);
			decrypted = cryptoService.decryptData(key, attachedFile);
			
			response = Response.status(200).entity(decrypted).build();
		} catch (Exception e) {
			logger.error("Error: " , e);
			response = Response.status(500).entity(MSG_DECODE_ERROR + e.getMessage()).build();
		}
		return response;
	}

}