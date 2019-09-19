package org.acme.quickstart;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;

@ApplicationScoped
public class GreetingMailService {

	@Inject
	private Mailer mailer;

	public void send(String destination, byte [] attach, String fileName ) {
		mailer.send(Mail.withText(destination, "Importante!",
                "Mensagem em anexo.")
                .addAttachment(fileName,
                    attach, "application/octet-stream"));
		
	}
	
}
