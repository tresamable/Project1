package com.page;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.tresa.framewok.code.Page;

public class GitHubUserHomePage  extends Page{

	protected GitHubUserHomePage() {
		// no direct object
	}

	Elements elements;

	@Override
	public void initElements() {
		elements = new Elements();
		PageFactory.initElements(driver(), elements);
	}
	private static class Elements {
		
		
	}
	public GitHubAccountAdminPage openAdminAccountsPageUrl(){
		driver().get("https://github.com/settings/admin");
		return Page.getInstance(GitHubAccountAdminPage.class);
	}

}
