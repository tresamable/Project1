package com.page;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.tresa.framewok.code.Page;

public class GitHubRegistrationPage extends Page{
	protected GitHubRegistrationPage() {
		// no direct object
	}

	Elements elements;

	@Override
	public void initElements() {
		elements = new Elements();
		PageFactory.initElements(driver(), elements);
	}

	private static class Elements {
		@FindBy(id="user_login")
		private WebElement txtUserName;
		
		@FindBy(id="user_email")
		private WebElement txtEmail;
		
		@FindBy(id="user_password")
		private WebElement txtPassword;
		
		@FindBy(id="signup_button")
		private WebElement btnCreateAccount;
		
		@FindBy(className="error")
		private WebElement errorPopup;
		
		@FindBy(className="js-choose-plan-submit")
		private WebElement btnContinue;

		@FindBy(className="alternate-action")
		private WebElement btnSkip;
	}
	
	public boolean doRegister(String userId, String email,String password){
		type(this.elements.txtUserName, userId+Keys.TAB);
		wait(3);
		if(isDisplayed(this.elements.errorPopup)){
			// account already exists / invalid id	
			return false;
		}
		type(this.elements.txtEmail,email);
		if(isDisplayed(this.elements.errorPopup)){
			// account already exists / invalid id	
			return false;
		}
		type(this.elements.txtPassword,password);
		click(this.elements.btnCreateAccount);
		if(isDisplayed(this.elements.errorPopup)){
			// account already exists / invalid id	
			return false;
		}
		this.click(this.elements.btnContinue);
		this.click(elements.btnSkip);
		return true;
	}
}
