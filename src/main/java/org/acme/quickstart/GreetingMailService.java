package org.acme.quickstart;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;

@ApplicationScoped
public class GreetingMailService {

	@Inject
	private Mailer mailer;

	public void send(String destination, byte [] attach ) {
		mailer.send(Mail.withText(destination, "An email from quarkus with attachment",
                "This is my body")
                .addAttachment("teste",
                    attach, "application/octet-stream"));
		
	}
	
}
