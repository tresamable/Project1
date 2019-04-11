package com.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.tresa.framewok.code.Page;

public class GitHubHomePage  extends Page{
	protected GitHubHomePage() {
		// no direct object
	}

	Elements elements;

	@Override
	public void initElements() {
		elements = new Elements();
		PageFactory.initElements(driver(), elements);
	}

	private static class Elements {
		@FindBy(className="flash-notice")
		WebElement divFlashNotice;
		
		@FindBy(linkText="Sign in")
		WebElement linkSignIn;
		
		@FindBy(linkText="SignUp")
		WebElement linkSignUp;
	}
	public boolean isFlashNoticeDisplayed(){
		//return isDisplayed(elements.divFlashNotice);
		return isDisplayed(By.className("flash-notice"),20);
	}
	public String getFlashNoticeText(){
		return getText(elements.divFlashNotice).trim();
	}
	public GitHubRegistrationPage clickSignUpLink(){
		click(this.elements.linkSignUp);
		return Page.getInstance(GitHubRegistrationPage.class);
	}
	public GitHubLoginPage clickSignInLink(){
		click(this.elements.linkSignIn);
		return Page.getInstance(GitHubLoginPage.class);
	}
	
}
