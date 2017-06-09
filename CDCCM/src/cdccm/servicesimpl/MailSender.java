package cdccm.servicesimpl;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class MailSender {
	private String subject;
	private String body;
	private String to;
	private String filepath;
	public MailSender(String subject,String body,String to,String filepath)
	{
		this.subject=subject;
		this.body=body;
		this.to=to;
		this.filepath=filepath;
	}
	@SuppressWarnings({ "restriction", "static-access" })
    public void sendMail(){
        
		final String from = "chetan.patil.sender@gmail.com";
		final String user = "chetan.patil.sender@gmail.com";
        final String host = "smtp.gmail.com";
		final String pass = "chetan.patil";
		final int port=587;
		// Get system properties
		Properties properties = System.getProperties();

		// Setup mail server
		properties.setProperty("mail.smtp.host", host);
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.port", port);
		properties.put("mail.starttls.required", "true");
		properties.put("mail.smtp.auth", "true");

		java.security.Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());

		// Get the default Session object.and provide authetication information
		Session session = Session.getDefaultInstance(properties, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(from, pass);
			}
		});
		session.setDebug(false);
		try {
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from));

			message.addRecipient(Message.RecipientType.TO, new InternetAddress(this.to));

			message.setSubject(this.subject);

			BodyPart messageBodyPart = new MimeBodyPart();

			messageBodyPart.setText(this.body);

			Multipart multipart = new MimeMultipart();

			multipart.addBodyPart(messageBodyPart);

			// Part two is attachment
			messageBodyPart = new MimeBodyPart();
		
			DataSource source = new FileDataSource(this.filepath);
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName(this.filepath);
			multipart.addBodyPart(messageBodyPart);

			// Send the complete message parts including,subject,body,attachment
			message.setContent(multipart);

			// Send message
			Transport transport = session.getTransport("smtp");
			transport.connect(host, user, pass);
			transport.send(message, message.getAllRecipients());
			transport.close();
			System.out.println("Sent message successfully...to: "+ this.to);
		} catch (MessagingException mex) {
			mex.printStackTrace();
		}
}
}