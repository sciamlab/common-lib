package com.sciamlab.common.util;

import java.io.File;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.validation.ValidationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.internal.util.collection.MultivaluedStringMap;

public class SciamlabMailUtils {

	private static final Logger logger = Logger.getLogger(SciamlabMailUtils.class);

	public static class GMail extends Mail {

		private static final String server = "smtp.gmail.com";
		private static final int port = 587;
		private static final boolean auth = true;
		private static final boolean starttls = true;

		private GMail(String from, String password) {
			super(from, password, server);
			super.port(port);
			super.auth(auth);
			super.starttls(starttls);
		}

		public Mail auth(boolean auth) {
			throw new RuntimeException(
					"Not allowed to change auth option in GMail message");
		}

		public Mail starttls(boolean starttls) {
			throw new RuntimeException(
					"Not allowed to change starttls option in GMail message");
		}

		public Mail port(int port) {
			throw new RuntimeException(
					"Not allowed to change port option in GMail message");
		}

	}

	public static class MailGun extends Mail {

		private final String mailgunApiKey;
		private final String mailgunHost;

		private MailGun(String mailgunHost, String mailgunApiKey) {
			super();
			this.mailgunApiKey = mailgunApiKey;
			this.mailgunHost = mailgunHost;
		}
		
		public Mail from(String from) {
			this.from = from;
			return this;
		}

		public Mail auth(boolean auth) {
			throw new RuntimeException(
					"Not allowed to change auth option in MailGun message");
		}

		public Mail starttls(boolean starttls) {
			throw new RuntimeException(
					"Not allowed to change starttls option in MailGun message");
		}

		public Mail port(int port) {
			throw new RuntimeException(
					"Not allowed to change port option in MailGun message");
		}

		@Override
		protected void send(Message msg) throws Exception {
			if (from == null)
				throw new ValidationException("sender cannot be null");
			if (msg.to.isEmpty())
				throw new ValidationException("recipient list cannot be empty");
			Client client = ClientBuilder.newClient();
			client.register(HttpAuthenticationFeature.basic("api", mailgunApiKey));

			WebTarget webTarget = client.target("https://api.mailgun.net/v2/" + mailgunHost + "/messages");

			MultivaluedStringMap formData = new MultivaluedStringMap();
			formData.add("from", from);
			StringBuffer tos = new StringBuffer();
			StringBuffer ccs = new StringBuffer();
			StringBuffer bccs = new StringBuffer();
			// to
			boolean first = true;
			for(Address r : msg.to){
				if(first)
					first = false;
				else
					tos.append(",");
				tos.append(r.toString());
			}
			formData.add("to", tos.toString());
			// cc
			first = true;
			for(Address r : msg.cc){
				if(first)
					first = false;
				else
					ccs.append(",");
				ccs.append(r.toString());
			}
			if(!msg.cc.isEmpty())
				formData.add("cc", ccs.toString());
			// bcc
			first = true;
			for(Address r : msg.bcc){
				if(first)
					first = false;
				else
					bccs.append(",");
				bccs.append(r.toString());
			}
			if(!msg.bcc.isEmpty())
				formData.add("bcc", bccs.toString());
						
			if (msg.subject != null)
				formData.add("subject", msg.subject);
			
			if(msg.text!=null)
				formData.add("text", msg.text);
			
			if(msg.html!=null)
				formData.add("html", msg.html);

		    Response response = webTarget.request().post(Entity.form(formData));
			String output = response.readEntity(String.class);
			logger.debug(output);
		}

	}
	
	public static class Message {
		String subject;
		String html;
		String text;
		List<BodyPart> attachments = new ArrayList<BodyPart>();
		List<Address> to = new ArrayList<Address>();
		List<Address> cc = new ArrayList<Address>();
		List<Address> bcc = new ArrayList<Address>();
		private Mail mailer;
		
		private Message(Mail mailer){
			this.mailer = mailer;
		}
		
		public Message subject(String subject) {
			this.subject = subject;
			return this;
		}

		public Message html(String html) {
			this.html = html;
			return this;
		}
		public Message text(String text) {
			this.text = text;
			return this;
		}
		
		public Message to(String address) throws Exception {
			this.to.add(new InternetAddress(address));
			return this;
		}
		public Message to(List<String> address) throws Exception {
			for(String a : address)
				this.to.add(new InternetAddress(a));
			return this;
		}
		public Message cc(String address) throws Exception {
			this.cc.add(new InternetAddress(address));
			return this;
		}
		public Message cc(List<String> address) throws Exception {
			for(String a : address)
				this.cc.add(new InternetAddress(a));
			return this;
		}
		public Message bcc(String address) throws Exception {
			this.bcc.add(new InternetAddress(address));
			return this;
		}
		public Message bcc(List<String> address) throws Exception {
			for(String a : address)
				this.bcc.add(new InternetAddress(a));
			return this;
		}

		public Message attach(File attachment) throws Exception {
			BodyPart attachemnt_part = new MimeBodyPart();
			DataSource source = new FileDataSource(attachment);
			attachemnt_part.setDataHandler(new DataHandler(source));
			attachemnt_part.setFileName(attachment.getName());
			this.attachments.add(attachemnt_part);
			return this;
		}
		
		public void send() throws Exception {
			mailer.send(this);
		}
	}

	public static class Mail {
		String from;
		String password;
//		String subject;
//		String html;
//		String text;
//		List<BodyPart> attachments = new ArrayList<BodyPart>();
//		List<Address> to = new ArrayList<Address>();

		Properties properties = System.getProperties();
		
		private Mail() {
		}

		private Mail(String from, String password, String server) {
			this.from = from;
			this.password = password;
			properties.setProperty("mail.smtp.host", server);
		}

		public Mail auth(boolean auth) {
			this.properties.put("mail.smtp.auth", auth);
			return this;
		}

		public Mail starttls(boolean starttls) {
			this.properties.put("mail.smtp.starttls.enable", starttls);
			return this;
		}

		public Mail port(int port) {
			this.properties.put("mail.smtp.port", port);
			return this;
		}
		
		public Message message(){
			return new Message(this);
		}

		protected void send(Message msg) throws Exception {
			if (from == null)
				throw new ValidationException("sender cannot be null");
			if (password == null)
				throw new ValidationException("password cannot be null");
			if (msg.to.isEmpty())
				throw new ValidationException("recipient list cannot be empty");
			if (!properties.containsKey("mail.smtp.host"))
				throw new ValidationException("server cannot be null");
			Session session = Session.getInstance(properties,
					new javax.mail.Authenticator() {
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(from, password);
						}
					});
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.addRecipients(javax.mail.Message.RecipientType.TO, msg.to.toArray(new Address[msg.to.size()]));
			if (!msg.cc.isEmpty())
				message.addRecipients(javax.mail.Message.RecipientType.CC, msg.cc.toArray(new Address[msg.cc.size()]));
			if (!msg.bcc.isEmpty())
				message.addRecipients(javax.mail.Message.RecipientType.BCC, msg.bcc.toArray(new Address[msg.bcc.size()]));
			if (msg.subject != null)
				message.setSubject(msg.subject);
			
			// Create a multipart message
			Multipart multipart = new MimeMultipart();
			
			if(msg.text!=null){
				// Create the text message part
				MimeBodyPart text_part = new MimeBodyPart();
				text_part.setText(msg.text, "utf-8");
				multipart.addBodyPart(text_part);
			}
			
			if(msg.html!=null){
				// Create the html message part
				MimeBodyPart html_part = new MimeBodyPart();
				html_part.setContent(msg.html, "text/html; charset=utf-8");
				multipart.addBodyPart(html_part);
			}
			//attachments
			for (BodyPart att : msg.attachments) {
				multipart.addBodyPart(att);
			}
			
			// Set the complete message parts
			message.setContent(multipart);
			Transport.send(message);
		}
		
	}

	public static Mail message(String from, String password, String server) {
		return new Mail(from, password, server);
	}

	public static GMail gmail(String from, String password) {
		return new GMail(from, password);
	}

	public static MailGun mailgun(String mailgunHost, String mailgunApiKey) {
		return new MailGun(mailgunHost, mailgunApiKey);
	}

}