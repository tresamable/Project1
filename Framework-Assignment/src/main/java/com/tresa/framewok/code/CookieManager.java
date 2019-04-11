package com.tresa.framewok.code;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Set;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

public class CookieManager {

	public static void expoertCookie(WebDriver driver,String filePath){
		Set<Cookie> cookies = driver.manage().getCookies();
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath));
			oos.writeObject(cookies);
			oos.close();
			System.out.println("Cookie saved");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void importCookie(WebDriver driver,String filePath){
		driver.manage().deleteAllCookies();;
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath));
			Set<Cookie> cookies = (Set<Cookie>) ois.readObject();
			ois.close();
			for (Cookie cookie : cookies) {
				System.out.println(cookie);
				driver.manage().addCookie(cookie);
			}
			
			System.out.println("Cookies imported");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
