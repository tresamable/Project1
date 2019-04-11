package com.page;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.tresa.framewok.code.Page;

public class GitHubAccountAdminPage extends Page{

	protected GitHubAccountAdminPage() {
		// no direct object
	}

	Elements elements;

	@Override
	public void initElements() {
		elements = new Elements();
		PageFactory.initElements(driver(), elements);
	}

	private static class Elements {
		@FindBy(css="[rel=\"facebox[.dangerzone]\"]")
		private WebElement btnDeleteYourAccount;
		
		@FindBy(css=".dangerzone #sudo_login")
		private WebElement txtUsernameOrEmail;
		
		@FindBy(css=".dangerzone .confirmation-phrase")
		private WebElement stringConfirmPhrase;
		
		@FindBy(css=".dangerzone #confirmation_phrase")
		private WebElement txtConfirmPhrase;
		
		@FindBy(xpath="//*[@id='facebox']//*[text()='Cancel plan and delete this account']")
		private WebElement btnFinalDelete;
	}
	public GitHubHomePage performDeleteAccount(String emailOrId){
		click(elements.btnDeleteYourAccount);
		type(elements.txtUsernameOrEmail,emailOrId);
		type(elements.txtConfirmPhrase,getText(elements.stringConfirmPhrase));
		click(elements.btnFinalDelete);
		
		return Page.getInstance(GitHubHomePage.class);
	}
	
}
