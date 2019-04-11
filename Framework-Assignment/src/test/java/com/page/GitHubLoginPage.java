package com.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;


import com.tresa.framewok.code.Page;

public class GitHubLoginPage extends Page{

	protected GitHubLoginPage() {
		// no direct object
	}

	Elements elements;

	@Override
	public void initElements() {
		elements = new Elements();
		PageFactory.initElements(driver(), elements);
	}
	private static class Elements {
		@FindBy(id="login_field")
		private WebElement txtLoginField;
		
		@FindBy(id="password")
		private WebElement txtPasswordField;
		
		@FindBy(css="[value='Sign in']")
		private WebElement btnSignIn;
		
		By divErrorLocator = By.className("js-flash-close");
		
	}

	public GitHubUserHomePage doLogin(String userId,String password){
		type(elements.txtLoginField,userId);
		type(elements.txtPasswordField,password);
		click(elements.btnSignIn);
		if(isDisplayed(elements.divErrorLocator,5)){
			return null;
		}
		return Page.getInstance(GitHubUserHomePage.class);
	}
}
