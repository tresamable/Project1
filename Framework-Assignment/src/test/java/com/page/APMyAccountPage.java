package com.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.tresa.framewok.code.Page;

public class APMyAccountPage extends AutomationPracticePage{
	protected APMyAccountPage() {
		// no direct object
	}

	Elements elements;

	@Override
	public void initElements() {
		super.initElements();
		elements = new Elements();
		PageFactory.initElements(driver(), elements);
	}

	private static class Elements {
		@FindBy(className="logout")
		private WebElement menuLogout;
		
	}

	public APHomePage logOut() {
		this.click(this.elements.menuLogout);
		return Page.getInstance(APHomePage.class);
	}
	
}
