package com.test;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HostnameVerifier;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.dp.GHRegistrationDataProviders;
import com.page.GitHubAccountAdminPage;
import com.page.GitHubEmailSettings;
import com.page.GitHubHomePage;
import com.page.GitHubLoginPage;
import com.page.GitHubRegistrationPage;
import com.page.GitHubUserHomePage;
import com.tresa.framewok.code.BaseTest;
import com.tresa.framewok.code.Page;
import com.tresa.framewok.util.EmailMessage;
import com.tresa.framewok.util.GmailMailboxReader;

public class GitHubRegistrationTest extends BaseTest {

	@BeforeMethod
	public void initBrowser() {
		driver().get("https://github.com/join?source=header-home");

	}

	@AfterMethod
	public void closeBrowser() {
		// no need to delete account as it's taken care in test only
		// close browser
		cleanUp();
	}

	// VT: PREREQUSITES: A gmail account "with less secure app" needed
	// More info: https://support.google.com/accounts/answer/6010255?hl=en
	// https://myaccount.google.com/lesssecureapps
	@Test(dataProviderClass = GHRegistrationDataProviders.class, dataProvider = GHRegistrationDataProviders.GIT_HUB_REG_DATA)
	public void testRegistrationWithValidDataAndEmailVerification(String userId, String password, String email,
			String emailPassword) {
		// make sure account is not already existing
		GitHubRegistrationPage regPage = Page.getInstance(GitHubRegistrationPage.class);
		boolean isDone = regPage.doRegister(userId, email, password);
		// if yes then delete account and close
		// open sign up
		if (!isDone) {
			// email already exists
			deleteAccount(userId, password);
			driver().get("https://github.com/join?source=header-home");
			isDone = regPage.doRegister(userId, email, password);
		}
		Assert.assertTrue(isDone, "Failed to create account");
		driver().get("https://github.com/settings/emails");
		GitHubEmailSettings emailSettingsPage = Page.getInstance(GitHubEmailSettings.class);
		Assert.assertTrue(emailSettingsPage.isEmailUnverified(),
				"Default email status for newly created account should be Unverified");
		// goto mailbox
		List<EmailMessage> inboxEmails = waitForNewUnReadEmail(email, emailPassword, 5);

		// List<EmailMessage> inboxEmails =
		// GmailMailboxReader.getRecentEmails(email, emailPassword, 5);
		Set<String> urls = new LinkedHashSet<String>();
		for (EmailMessage emailMessage : inboxEmails) {
			if (emailMessage.getSubject().equals("[GitHub] Please verify your email address.")) {
				String[] lines = emailMessage.getBody().split("\n");
				for (String line : lines) {
					if (line.startsWith("https://github.com/users")) {
						urls.add(line);
					}
				}
			}
		}
		Assert.assertFalse(urls.isEmpty(),
				"No email received with verification link with subject '[GitHub] Please verify your email address.'");
		List<String> urlList = new ArrayList<String>(urls);
		// assumption : one email only with one url for now.
		driver().get(urlList.get(0));
		GitHubHomePage homePage = Page.getInstance(GitHubHomePage.class);
		Assert.assertEquals(homePage.getFlashNoticeText(), "Your email was verified.",
				"Email verification message failed");
		driver().get("https://github.com/settings/emails");
		emailSettingsPage = Page.getInstance(GitHubEmailSettings.class);
		Assert.assertFalse(emailSettingsPage.isEmailUnverified(),
				"Email is still shown as Unverified even after email verification done");
	}

	private List<EmailMessage> waitForNewUnReadEmail(String email, String emailPassword, int i) {

		List<EmailMessage> inboxEmails = new ArrayList<EmailMessage>();
		final int MAX_RETRY = 4;
		for (int j = 0; j < MAX_RETRY; j++) {
			Reporter.log("Waiting for new emails..", true);
			inboxEmails = GmailMailboxReader.getRecentEmails(email, emailPassword, 5);
			if (inboxEmails.isEmpty()) {
				Page.wait(20);
				Reporter.log("Retry#" + (j + 1) + " : Waiting for new emails..", true);
				continue;
			} else {
				break;
			}
		}
		return inboxEmails;
	}

	private void deleteAccount(String userId, String password) {
		driver().get("https://github.com/");
		GitHubHomePage homePage = Page.getInstance(GitHubHomePage.class);
		GitHubLoginPage loginPage = homePage.clickSignInLink();
		GitHubUserHomePage userHomePage = loginPage.doLogin(userId, password);
		Assert.assertNotNull(userHomePage, "Failed to login, Invalid credentials: " + userId + "/" + password);
		GitHubAccountAdminPage adminPage = userHomePage.openAdminAccountsPageUrl();
		homePage = adminPage.performDeleteAccount(userId);
		Assert.assertTrue(homePage.isFlashNoticeDisplayed(), "Account deletionconfirmation notice was not displayed.");
		Assert.assertEquals(homePage.getFlashNoticeText(), "Account successfully deleted.",
				"Notice text after deleting account failed.");
	}

}