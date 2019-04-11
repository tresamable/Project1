package com.tresa.framewok.code;

import java.lang.reflect.Constructor;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;

public abstract class Page {
	protected WebDriverWait explicitWait;

	/**
	 * Method to initialize page objects. initElements() will be called once
	 * page object created
	 *
	 * @param clazz
	 *            Page class to create object
	 * @return Page object with initialized elements.
	 * @throws @throws
	 *             Exception
	 * @see #initElements()
	 */
	public static <T extends Page> T getInstance(Class<T> clazz) {
		T obj = null;
		try {
			@SuppressWarnings("unchecked")
			final Constructor<T> cons = (Constructor<T>) clazz.getDeclaredConstructors()[0];
			cons.setAccessible(true);
			obj = cons.newInstance();
			obj.initElements();
			obj.explicitWait = new WebDriverWait(obj.driver(), PropertiesManager.getExplicitTimeout());
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return obj;
	}

	/**
	 * Abstract method to initialize elements of web elements.
	 * <p>
	 * Every page class must override this abstract method to initialize object
	 * </p>
	 */
	abstract protected void initElements();

	/**
	 * Method to get driver instance form current test session
	 * 
	 * @return WebDriver instance
	 */
	protected WebDriver driver() {
		return BaseTest.driver();
	}

	protected void click(WebElement element) {
		// optional wait for element to be clickable
		optionalWaitForElementVisibility(element);
		try{
			element.click();
		}
		catch(Exception e){
			// Attempt 2 : using Actions mouse over and click
			try{
				Actions a = new Actions(driver());
				a.moveToElement(element).pause(Duration.ofMillis(500)).click().perform();
			}
			catch(Exception ex){
				// Attempt 3 : using JS Click
				JavascriptExecutor executor = (JavascriptExecutor) driver();
				executor.executeScript("arguments[0].click()");
			}
		}
	}

	protected void click(By locator) {
		click(get(locator));
	}

	protected WebElement get(By locator) {
		return new WebDriverWait(driver(), PropertiesManager.getElementTimeout())
				.until(ExpectedConditions.presenceOfElementLocated(locator));
	}

	protected WebElement get(By locator, int timeout) {
		return new WebDriverWait(driver(), timeout).until(ExpectedConditions.presenceOfElementLocated(locator));
	}

	/**
	 * Method to type value to textbox. existing text will be cleared if present
	 * 
	 * @param element
	 * @param textToType
	 */
	protected void type(WebElement element, String textToType) {
		type(element, textToType, true);
	}

	protected void type(WebElement element, String textToType, boolean clearExistingText) {
		if (clearExistingText)
			element.clear();
		if (textToType != null && !textToType.isEmpty()) {
			element.sendKeys(textToType);
		} else {
			Reporter.log("text to type is empty, skipping typing");
		}
	}

	protected void type(By locator, String textToType) {
		type(get(locator), textToType);
	}

	protected String getText(WebElement element) {
		if (element.getTagName().equalsIgnoreCase("input")) {
			return element.getAttribute("value");
		}
		return element.getText();
	}

	protected String getText(By locator) {
		return getText(get(locator));
	}

	protected String getText(By locator, int timeout) {
		try {
			return getText(get(locator, timeout));
		} catch (NoSuchElementException | NullPointerException | TimeoutException e) {
			return null;
		}
	}

	protected boolean isDisplayed(WebElement element) {
		try {
			return element.isDisplayed();
		} catch (NoSuchElementException e) {
			return false;
		}
	}
	protected boolean isDisplayed(WebElement element, int maxTimeout) {
		try {
			return waitForElementVisibility(element,maxTimeout).isDisplayed();
		} catch (NoSuchElementException | NullPointerException | TimeoutException e) {
			return false;
		}
	}
	protected boolean isDisplayed(By locator) {
		try {
			return get(locator).isDisplayed();
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	protected boolean isDisplayed(By locator, int maxTimeout) {
		try {
			return get(locator, maxTimeout).isDisplayed();
		} catch (NoSuchElementException | NullPointerException | TimeoutException e) {
			return false;
		}
	}
	

	protected void select(SelectBy selectBy, WebElement element, Object text) {
		if (text != null && !text.toString().isEmpty()) {
			Select select = new Select(element);
			switch (selectBy) {
			case INDEX:
				select.selectByIndex(Integer.parseInt(text.toString()));
				break;
			case VALUE:
				select.selectByValue(text.toString());
				break;
			case VISIBLE_TEXT:
				select.selectByVisibleText(text.toString());
				break;
			}
		} else {
			Reporter.log("Skipping selecting as value is nil", true);
		}
	}

	protected void tick(WebElement cbElement, String value) {
		tick(cbElement, Boolean.parseBoolean(value));
	}

	protected void tick(WebElement cbElement, boolean tick) {
		if (!(cbElement.isSelected() && tick)) {
			cbElement.click();
		}
	}

	public String getPageUrl() {
		return driver().getCurrentUrl();
	}

	/**
	 * Method to wait explicitly for given duration in seconds VT: Please avoid
	 * this method and dynamically use webdriverwait instead
	 * 
	 * @param sec
	 */
	public static void wait(int sec) {
		try {
			Thread.sleep(sec * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	protected WebElement waitForElementVisibility(WebElement element) {
		return waitForElementVisibility(element, PropertiesManager.getElementTimeout());
	}
	protected WebElement waitForElementVisibility(WebElement element,long timeout) {
		driver().manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
		WebElement e = null;
		try{
			e = new WebDriverWait(driver(), timeout).until(ExpectedConditions.visibilityOf(element));
		}
		finally{	
			driver().manage().timeouts().implicitlyWait(PropertiesManager.getImplicitWaitTimeout(), TimeUnit.SECONDS);
		}
		return e;
	}
	protected void optionalWaitForElementVisibility(WebElement element){
		try{
			waitForElementVisibility(element);
		}
		catch(Exception e){
			// do nothing as it's optional wait
		}
	}
}
