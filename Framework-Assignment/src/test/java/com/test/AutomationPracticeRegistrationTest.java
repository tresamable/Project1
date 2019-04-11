package com.test;

import java.util.Map;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.dp.ApRegistrationDataProviders;
import com.dp.helper.ApRegistrationFields;
import com.page.APHomePage;
import com.page.APMyAccountPage;
import com.page.APRegistrationPage;
import com.tresa.framewok.code.BaseTest;
import com.tresa.framewok.code.Page;

public class AutomationPracticeRegistrationTest extends BaseTest{

	@BeforeMethod
	public void initBrowser(){
		driver().get("http://automationpractice.com/index.php?controller=authentication");
	}
	@AfterMethod
	public void closeBrowser(){
		cleanUp();
	}
	@Test(dataProviderClass = ApRegistrationDataProviders.class, dataProvider = ApRegistrationDataProviders.VALID_REG_TEST_DATA)
	public void testRegistration(Map<String,String> regData) {
		
		APHomePage apHomePage = Page.getInstance(APHomePage.class);
		APRegistrationPage regPage = apHomePage.openRegistrationPage(regData.get(ApRegistrationFields.EMAIL));
		Assert.assertNotNull(regPage,"Failed to create account with email id: "+regData.get(ApRegistrationFields.EMAIL));
		APMyAccountPage accountPage = regPage.registerUser(regData);
		if(Boolean.parseBoolean(regData.get(ApRegistrationFields.EXPECTED_LOGIN))){
			Assert.assertNotNull(accountPage,"Failed to create account with valid credentails, Error on page:"+regPage.getErrorText());
			// additional check to confirm page is valid
			Assert.assertFalse(accountPage.isErrorDislayedOnPage(),"Test failed, Error message is displayed on page -> "+ accountPage.getErrorText());
			apHomePage = accountPage.logOut();
			Assert.assertEquals(apHomePage.getPageUrl(),"http://automationpractice.com/index.php?controller=authentication&back=my-account", "Failed to logout, Login page did not open after logout");
			accountPage = apHomePage.doLogin(regData.get(ApRegistrationFields.EMAIL2),regData.get(ApRegistrationFields.PASSWORD));
			Assert.assertNotNull(accountPage, "Failed to login with newly account created, Email Id/Password :"+regData.get(ApRegistrationFields.EMAIL2)+"/"+regData.get(ApRegistrationFields.PASSWORD)+". Error on page:"+apHomePage.getErrorText());
		}
		else{
			Assert.assertNull(accountPage,"Failed to create account with valid credentails");
			Assert.assertTrue(regPage.isErrorDislayedOnPage(),"Test failed, Error message was not displayed on page");
			Reporter.log("------ Error Message/s on Page: ------\n"+ regPage.getErrorText()+"\n--------------",true);
			Assert.assertEquals(regPage.getErrorText(), regData.get(ApRegistrationFields.ERROR),"Expected Error message text failed");
		}
	}

}
