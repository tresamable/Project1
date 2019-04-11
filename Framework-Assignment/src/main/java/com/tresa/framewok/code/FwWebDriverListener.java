package com.tresa.framewok.code;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.events.AbstractWebDriverEventListener;
import org.testng.Reporter;

public class FwWebDriverListener extends AbstractWebDriverEventListener {

	@Override
	public void beforeNavigateTo(String url, WebDriver driver) {
		System.out.printf("Opening url [%s]", url);
	}
	@Override
	public void afterNavigateTo(String url, WebDriver driver) {
		System.out.println(".... done");
	}
	@Override
	public void beforeChangeValueOf(WebElement element, WebDriver driver, CharSequence[] keysToSend) {
		StringBuilder sb = new StringBuilder();
		if (keysToSend != null) {
			for (CharSequence charSequence : keysToSend) {
				sb.append(charSequence);
			}
			String action = "typing";
			if (element.getTagName().equalsIgnoreCase("select")) {
				action = "selecting";
			}
			System.out.printf(action + " value [%s] in element [%s]", sb, getTextLocator(element));
		}
	}

	@Override
	public void afterChangeValueOf(WebElement element, WebDriver driver, CharSequence[] keysToSend) {
		if (keysToSend != null) {
			String action = "typing";
			if (element.getTagName().equalsIgnoreCase("select")) {
				action = "selecting";
			}
			System.out.println(".... " + action + " done.");
		}
	}

	@Override
	public void beforeClickOn(WebElement element, WebDriver driver) {
		System.out.printf("Clicking on element [%s]", getTextLocator(element));
	}

	@Override
	public void afterClickOn(WebElement element, WebDriver driver) {
		System.out.println(".... click done.");
	}

	private String getTextLocator(WebElement element) {
		Pattern pattern = Pattern.compile(".*-> (.*)].*");
		Matcher matcher = pattern.matcher(element.toString());
		if (matcher.find()) {
			return matcher.group(1);
		}
		return null;
	}

}
