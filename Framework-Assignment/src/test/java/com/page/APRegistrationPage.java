package com.page;

import java.util.Map;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Reporter;

import com.dp.helper.ApRegistrationFields;
import com.tresa.framewok.code.Page;
import com.tresa.framewok.code.SelectBy;

public class APRegistrationPage extends AutomationPracticePage {

	private APRegistrationPage() {
		// only in same class
	}

	Elements elements;

	@Override
	public void initElements() {
		super.initElements();
		elements = new Elements();
		PageFactory.initElements(driver(), elements);
	}

	private static class Elements {
		@FindBy(id = "id_gender1")
		private WebElement radioMr;
		@FindBy(id = "id_gender2")
		private WebElement radioMrs;
		@FindBy(id = "customer_firstname")
		private WebElement txtFirstName;
		@FindBy(id = "customer_lastname")
		private WebElement txtLastName;
		@FindBy(id = "email")
		private WebElement txtEmail;
		@FindBy(id = "passwd")
		private WebElement txtPassword;
		
		@FindBy(id = "days")
		private WebElement selectDays;
		@FindBy(id = "months")
		private WebElement selectMonths;
		@FindBy(id = "years")
		private WebElement selectYears;
		@FindBy(id = "newsletter")
		private WebElement cbNewsletter;
		@FindBy(id = "optin")
		private WebElement cbOffers;

		// **** Address *****/
		@FindBy(id="firstname")
		private WebElement txtAddrFirstName;
		@FindBy(id = "lastname")
		private WebElement txtAddrLastname;
		@FindBy(id = "company")
		private WebElement txtCompany;
		@FindBy(id = "address1")
		private WebElement txtAddress1;
		@FindBy(id = "address2")
		private WebElement txtAddress2;
		@FindBy(id = "city")
		private WebElement txtCity;
		@FindBy(id = "id_state")
		private WebElement selectState;
		@FindBy(id = "postcode")
		private WebElement txtZip;
		@FindBy(id = "id_country")
		private WebElement selectCountry;
		
		@FindBy(id = "other")
		private WebElement txtAdditionalInfo;
		@FindBy(id = "phone")
		private WebElement txtPhone;
		@FindBy(id = "phone_mobile")
		private WebElement txtMobile;
		@FindBy(id = "alias")
		private WebElement txtAlias;
		
		@FindBy(id="submitAccount")
		private WebElement btnSubmit;

	}
	public APMyAccountPage registerUser(Map<String, String> regData) {
		selectTitle(regData.get(ApRegistrationFields.TITLE));
		type(this.elements.txtFirstName,regData.get(ApRegistrationFields.FIRST_NAME));
		type(this.elements.txtLastName,regData.get(ApRegistrationFields.LAST_NAME));
		type(this.elements.txtEmail,regData.get(ApRegistrationFields.EMAIL2));
		type(this.elements.txtPassword,regData.get(ApRegistrationFields.PASSWORD));
		selectDateOfBirth(regData.get(ApRegistrationFields.DOB));
		tick(this.elements.cbNewsletter,regData.get(ApRegistrationFields.NEWSLETTER));
		tick(this.elements.cbOffers,regData.get(ApRegistrationFields.OFFERS));
		enterAddress(regData);
		this.click(this.elements.btnSubmit);
		if(this.isErrorDislayedOnPage()){
			return null;
		}
		
		return Page.getInstance(APMyAccountPage.class);
	}
	
	private void enterAddress(Map<String, String> regData) {
		type(this.elements.txtAddrFirstName,regData.get(ApRegistrationFields.ADDRESS_FIRST_NAME));
		type(this.elements.txtAddrLastname,regData.get(ApRegistrationFields.ADDRESS_LAST_NAME));
		type(this.elements.txtCompany,regData.get(ApRegistrationFields.ADDRESS_COMPANY));
		type(this.elements.txtAddress1,regData.get(ApRegistrationFields.ADDRESS_LINE1));
		type(this.elements.txtAddress2,regData.get(ApRegistrationFields.ADDRESS_LINE2));
		type(this.elements.txtCity,regData.get(ApRegistrationFields.ADDRESS_CITY));
		select(SelectBy.VISIBLE_TEXT, this.elements.selectState, regData.get(ApRegistrationFields.ADDRESS_STATE));
		type(this.elements.txtZip,regData.get(ApRegistrationFields.ZIP));
		select(SelectBy.VISIBLE_TEXT, this.elements.selectCountry, regData.get(ApRegistrationFields.COUNTRY));
		type(this.elements.txtAdditionalInfo,regData.get(ApRegistrationFields.COUNTRY));
		type(this.elements.txtPhone,regData.get(ApRegistrationFields.HOME_PHONE));
		type(this.elements.txtMobile,regData.get(ApRegistrationFields.MOBILE_PNONE));
		type(this.elements.txtAlias,regData.get(ApRegistrationFields.ADDRESS_ALIAS));
	}

	private void selectDateOfBirth(String value) {
		try{
		String[] chunks = value.split("/");
		//extra 2 spaces added for formatting in visible text
		select(SelectBy.VISIBLE_TEXT, this.elements.selectDays,Integer.valueOf(chunks[0])+"  ");
		select(SelectBy.INDEX, this.elements.selectMonths,chunks[1]);
		select(SelectBy.VISIBLE_TEXT, this.elements.selectYears,chunks[2]+"  ");
		}
		catch(Exception e){
			Reporter.log("Skipping Date of Birth due to error : "+e.getMessage(),true);
		}
	}
	
	private void selectTitle(String value) {
		if(value.equalsIgnoreCase("Mr")){
			this.click(this.elements.radioMr);
		}
		else if(value.equalsIgnoreCase("Mrs")){
			this.click(this.elements.radioMrs);
		}
		
	}
	
}
