package com.framework.pages;

//pages/LoginPage.java
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {
 private WebDriver driver;

 @FindBy(id = "username")
 WebElement usernameField;

 @FindBy(id = "password")
 WebElement passwordField;

 @FindBy(id = "loginBtn")
 WebElement loginButton;

 public LoginPage(WebDriver driver) {
     this.driver = driver;
     PageFactory.initElements(driver, this);
 }

 public void login(String user, String pass) {
     usernameField.sendKeys(user);
     passwordField.sendKeys(pass);
     loginButton.click();
 }
}
