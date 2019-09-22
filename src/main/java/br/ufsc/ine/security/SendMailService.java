package br.ufsc.ine.security;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;

@ApplicationScoped
public class SendMailService {
	
	Logger logger = Logger.getLogger(SendMailService.class);

	@Inject
	private Mailer mailer;

	public void send(String destination, byte [] attach, String fileName ) {
		mailer.send(Mail.withText(destination, "Importante!",
                "Segue arquivo em anexo.")
                .addAttachment(fileName,
                    attach, "application/octet-stream"));
		logger.info("the message was sent to {" + destination +"}.");
		
	}
	
}
