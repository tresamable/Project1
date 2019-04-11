package com.page;

import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;
import org.testng.annotations.Factory;

import com.dp.helper.ApRegistrationFields;
import com.tresa.framewok.code.Page;

public class APHomePage extends AutomationPracticePage{

	public APHomePage(){
		// no direct object
	}
	Elements elements;
	@Override
	public void initElements() {
		super.initElements();
		elements = new Elements();
		PageFactory.initElements(driver(), elements);
	}
	
	private static class Elements{
		//Create an account
		@FindBy(id="email_create")
		private WebElement txtEmail;
		@FindBy(id="SubmitCreate")
		private WebElement btnSubmitCreate;
		
		// Already Registered
		@FindBy(id="email")
		private WebElement txtLoginEmail;
		@FindBy(id="passwd")
		private WebElement txtLoginPassword;
		@FindBy(id="SubmitLogin")
		private WebElement btnSubmitLogin;
	}
	
	public APRegistrationPage openRegistrationPage(String email){
		type(this.elements.txtEmail,email);
		click(this.elements.btnSubmitCreate);
		if(this.isErrorDislayedOnPage()){
			Reporter.log("Envalid Email id, Error from page: "+ getErrorText(),true);
			return null;
		}
		APRegistrationPage regPage = Page.getInstance(APRegistrationPage.class);
		return regPage;
		
	}

	public APMyAccountPage doLogin(String email, String password) {
		type(this.elements.txtLoginEmail,email);
		type(this.elements.txtLoginPassword,password);
		click(this.elements.btnSubmitLogin);
		if(isErrorDislayedOnPage()){
			return null;
		}
		return Page.getInstance(APMyAccountPage.class);
	}

	
	
}
