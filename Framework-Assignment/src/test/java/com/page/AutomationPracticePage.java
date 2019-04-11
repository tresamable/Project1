package com.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Reporter;

import com.tresa.framewok.code.Page;
/**
 * Class for all common elements such as header, footer menu etc.
 * @author tresaJohn
 *
 */
public class AutomationPracticePage extends Page{
	protected AutomationPracticePage() {
		// no direct object
	}

	Elements elements;

	@Override
	public void initElements() {
		elements = new Elements();
		PageFactory.initElements(driver(), elements);
	}

	private static class Elements {
		By alertLocator = By.className("alert-danger");
	}
	public boolean isErrorDislayedOnPage(){
		Reporter.log("Checking error message on webpage..",true);
		return isDisplayed(this.elements.alertLocator,5);
	}
	public String getErrorText(){
		return getText(this.elements.alertLocator,5);
	}
}
