package com.hoolai.bi.etlengine.mail;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.hoolai.bi.notifyer.mail.MailSenderProxy;
import com.hoolai.bi.report.job.SystemThreadPoolExecutor;

/**
 *  异步发送邮件 
 */
@Component
public class AsyncMailSender {
	
	private final SystemThreadPoolExecutor systemThreadPoolExecutor=new SystemThreadPoolExecutor(this.getClass().getSimpleName()+"-mail-sender", 3);
	
	public final void sendMail(String to,String subject,String content){
		
		AsyncSendMailThread asyncSendMailThread=new AsyncSendMailThread(new String[]{to}, subject, content);
		this.systemThreadPoolExecutor.submit(asyncSendMailThread);
		
	}
	
	public final void sendMail(String[] to,String subject,String content){
		
		AsyncSendMailThread asyncSendMailThread=new AsyncSendMailThread(to, subject, content);
		this.systemThreadPoolExecutor.submit(asyncSendMailThread);
		
	}
	
	private static class AsyncSendMailThread implements Runnable{
		
		private static final MailSenderProxy mailSenderProxy=new MailSenderProxy();
		
		private String[] toList;
		
		private String subject;
		
		private String content;

		public AsyncSendMailThread(String[] toList, String subject,
				String content) {
			super();
			this.toList = toList;
			this.subject = subject;
			this.content = content;
		}

		@Override
		public void run() {
			try {
				mailSenderProxy.sendMail(toList, subject, content);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
}
