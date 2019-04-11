package com.page;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.tresa.framewok.code.Page;

public class GitHubEmailSettings extends Page {

	protected GitHubEmailSettings() {
		// no direct object
	}

	Elements elements;

	@Override
	public void initElements() {
		elements = new Elements();
		PageFactory.initElements(driver(), elements);
	}

	private static class Elements {
		@FindBy(xpath="//*[normalize-space(text())='Unverified']")
		WebElement labelUnverified;
	}
	public boolean isEmailUnverified(){
		wait(1);
		return isDisplayed(this.elements.labelUnverified,10);
	}
}
