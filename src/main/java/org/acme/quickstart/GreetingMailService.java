package org.acme.quickstart;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;

@ApplicationScoped
public class GreetingMailService {

	@Inject
	private Mailer mailer;

	public void send(String to) {
		mailer.send(Mail.withText(to, "An email from quarkus with attachment",
                "This is my body")
                .addAttachment("my-file.txt",
                    "content of my file".getBytes(), "text/plain"));
		
	}
	
}
