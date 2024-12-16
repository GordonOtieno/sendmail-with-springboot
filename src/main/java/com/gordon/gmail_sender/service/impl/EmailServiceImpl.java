package com.gordon.gmail_sender.service.impl;

import java.io.File;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.gordon.gmail_sender.service.EmailService;
import com.gordon.gmail_sender.utils.EmailUtils;

import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.BodyPart;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

@Service
public class EmailServiceImpl implements EmailService {
	private static final String NEW_USER_ACCOUNT_VERIFICATION = "New User Verification";
	private static final String UTF_8_ENCODING = "UTF-8";
	private static final String EMAIL_TEMPLATE = "emailtemplate";
	@Value("${spring.mail.verify.host:http://localhost:8086}")
	//@Value(value = "http://localhost:8086")
  private String host;
   @Value("${spring.mail.username:otienogordon95@gmail.com}")
	//@Value(value = "otienogordon95@gmail.com")
  private String fromEmail;
   
   private final TemplateEngine templateEngine;
	
  private final JavaMailSender emailSender;
  
  public EmailServiceImpl(JavaMailSender emailSender, TemplateEngine templateEngine) {
      this.emailSender = emailSender;
      this.templateEngine= templateEngine;
  }
  
	@Override
	@Async
	public void sendSimpleMailMessage(String name, String to, String token) {
		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
			message.setFrom(fromEmail);
			message.setTo(to);
			message.setText(EmailUtils.getEmailMessage(name, host, token));
			emailSender.send(message);
			
		}catch(Exception exception) {
			System.out.println(exception.getMessage());
			throw new RuntimeException(exception.getMessage());
		}
	}

	@Override
	@Async
	public void sendMimeMessageWithAttachement(String name, String to, String token) {
		try {
			MimeMessage message = getMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true,UTF_8_ENCODING);
			helper.setPriority(1);
	        helper.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
	        helper.setFrom(fromEmail);
	        helper.setTo(to);
	        helper.setText(EmailUtils.getEmailMessage(name, host, token));
	        //add attachment
	        FileSystemResource panda = new FileSystemResource(new File(System.getProperty("user.home")+"/Desktop/upload/panda.JPG"));
	        FileSystemResource cv = new FileSystemResource(new File(System.getProperty("user.home")+"/Desktop/upload/cv.docx"));
	        helper.addAttachment(panda.getFilename(), panda);
	        helper.addAttachment(cv.getFilename(), cv);
			emailSender.send(message);
			
		}catch(Exception exception) {
			System.out.println(exception.getMessage());
			throw new RuntimeException(exception.getMessage());
		}
		
	}


	@Override
	@Async
	public void sendMimeMessageWithEmbeddedImages(String name, String to, String token) {
		try {
			MimeMessage message = getMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true,UTF_8_ENCODING);
			helper.setPriority(1);
	        helper.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
	        helper.setFrom(fromEmail);
	        helper.setTo(to);
	        helper.setText(EmailUtils.getEmailMessage(name, host, token));
	        //add attachment
	        FileSystemResource panda = new FileSystemResource(new File(System.getProperty("user.home")+"/Desktop/upload/panda.JPG"));
	        FileSystemResource cv = new FileSystemResource(new File(System.getProperty("user.home")+"/Desktop/upload/cv.docx"));
	        helper.addInline(getContentId(panda.getFilename()), panda);
	        helper.addInline(getContentId(cv.getFilename()), cv);
			emailSender.send(message);
			
		}catch(Exception exception) {
			System.out.println(exception.getMessage());
			throw new RuntimeException(exception.getMessage());
		}
		
	}

	@Override
	@Async
	public void sendHtmlEmail(String name, String to, String token) {
		try {
			Context context = new Context();
			context.setVariable("name", name);
			//context.setVariable("url", EmailUtils.getVerificationUrl(host,token));
			context.setVariables(Map.of("name",name, "url", EmailUtils.getVerificationUrl(host,token)));
			String text = templateEngine.process(EMAIL_TEMPLATE, context);
			MimeMessage message = getMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true,UTF_8_ENCODING);
			helper.setPriority(1);
	        helper.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
	        helper.setFrom(fromEmail);
	        helper.setTo(to);
	        helper.setText(text, true);
	        FileSystemResource panda = new FileSystemResource(new File(System.getProperty("user.home")+"/Desktop/upload/panda.JPG"));
	        FileSystemResource cv = new FileSystemResource(new File(System.getProperty("user.home")+"/Desktop/upload/cv.docx"));
	        helper.addAttachment(panda.getFilename(), panda);
	        helper.addAttachment(cv.getFilename(), cv);
	        emailSender.send(message);
			
		}catch(Exception exception) {
			System.out.println(exception.getMessage());
			throw new RuntimeException(exception.getMessage());
		}
		
	}

	@Override
	@Async
	public void sendHtmlEmailWithEmbeddedFiles(String name, String to, String token) {
		try {
            MimeMessage message = getMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF_8_ENCODING);
            helper.setPriority(1);
            helper.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
            helper.setFrom(fromEmail);
            helper.setTo(to);
            //helper.setText(text, true);
            Context context = new Context();
            context.setVariables(Map.of("name", name, "url", EmailUtils.getVerificationUrl(host, token)));
            String text = templateEngine.process(EMAIL_TEMPLATE, context);

            // Add HTML email body
            MimeMultipart mimeMultipart = new MimeMultipart("related");
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(text, "text/html");
            mimeMultipart.addBodyPart(messageBodyPart);

            // Add images to the email body
            BodyPart imageBodyPart = new MimeBodyPart();
            DataSource dataSource = new FileDataSource(System.getProperty("user.home") + "/Desktop/upload/panda.JPG");
            imageBodyPart.setDataHandler(new DataHandler(dataSource));
            imageBodyPart.setHeader("Content-ID", "image");
            mimeMultipart.addBodyPart(imageBodyPart);

            emailSender.send(message);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
		
	}

	private MimeMessage getMimeMessage() {
		return emailSender.createMimeMessage();
	}
	
	private String getContentId(String filename) {
		return "<" + filename + ">";
	}

	@Override
	public void sendMimeMessageWithEmbeddedFiles(String name, String to, String token) {
		// TODO Auto-generated method stub
		
	}

}
