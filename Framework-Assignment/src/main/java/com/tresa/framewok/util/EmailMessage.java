package com.tresa.framewok.util;

import java.util.ArrayList;
import java.util.List;

public class EmailMessage {

	private String from;
	private String subject;
	private List<String> attachment= new ArrayList<String>();
	private String body;
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public List<String> getAttachment() {
		return attachment;
	}
	public void setAttachment(List<String> attachment) {
		this.attachment = attachment;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public boolean hasAttachment(){
		return attachment.size()!=0;
	}
	@Override
	public String toString() {
		return "From: "+from+"\n"+
		"Subject: "+ subject+"\n"+
		"Attachments: "+attachment.size()+"\n"+
		"Email Body: "+ body+"\n";
	}
}
