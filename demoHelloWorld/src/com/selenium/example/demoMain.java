/**
 * 
 */
package com.selenium.example;

/**
 * @author hstone
 *
 */

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Proxy.ProxyType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

public class demoMain {

	/**
	 * @param filePathName where to store the screen shot
	 * NO validation of pathname is performed
	 */
	public static String collectPageSrc(String filePathName, WebDriver wDriver ){
		String pgSrc = wDriver.getPageSource();	
        try {
			FileUtils.writeStringToFile(new File(filePathName),pgSrc);
		} catch (IOException e) {
			e.printStackTrace();
		}
        return pgSrc;
	}

	
	/**
	 * @param filePathName where to store the screen shot
	 * NO validation of pathname is performed
	 */
	public static void takePix(String filePathName, WebDriver wDriver ){
		// take the screenshot at the end of every test
        File scrFile = ((TakesScreenshot)wDriver).getScreenshotAs(OutputType.FILE);
        // now save the screenshtot to a file some place
        try {
			FileUtils.copyFile(scrFile, new File(filePathName));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	static String webClientURL = "https://mobileqa.desertschools.org/webclient";
	static String proxyUrl ="mai01-iprxy-01p.clairmail.local";
	static String proxyPort = "3128";
	
	public static DesiredCapabilities getCapabilities(){
		// ---------- PROXY Capabilities --------------
		org.openqa.selenium.Proxy proxy = new org.openqa.selenium.Proxy();
		proxy.setSslProxy(proxyUrl +":"+ proxyPort); 
		proxy.setFtpProxy(proxyUrl +":"+ proxyPort); 
		proxy.setSocksUsername(""); 
		proxy.setSocksPassword(""); 
		
		DesiredCapabilities dc = DesiredCapabilities.firefox();
		dc.setCapability(CapabilityType.PROXY, proxy); 
		return dc;
	}
	public WebDriver wDriver = null;
	
	public static WebDriver getWebDriverInstance(){
		WebDriver wDriver = new FirefoxDriver(getCapabilities());
		
		return wDriver;
	}
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// ---------------------------------------------
		
		WebDriver wDriver = demoMain.getWebDriverInstance();
		
		//== Begin Session, navigate to web page

		wDriver.get(demoMain.webClientURL);		
		wDriver.manage().timeouts().pageLoadTimeout(100, TimeUnit.SECONDS);
		
		//-- collect page source
		String pageOne = demoMain.collectPageSrc("c:\\tmp\\LoginPage.html", wDriver);
		
		//## IF Login page continue
		Boolean isLogin = pageOne.contains("Remember my username");
		
		//===== LOGIN MFA FLOW:
		//== Username, Challenge, Password, Dashboard
		//== Username, Password, Dashboard
		
		//####### Verify LOGIN Page is displayed here
		


        //-- 
        //-- Page Elements to collect
		WebElement userNameInput = null;
		WebElement loginButton  = null;
		WebElement rememberUser = null;
		WebElement newLoanButton = null;
 		WebElement moreOptions = null;
		WebElement newLoanButtonClass = null;
		WebElement moreOptionsClass = null;
		WebElement copyRight = null;

		
		//--- Gather page elements needed for the test ---
		 userNameInput = wDriver.findElement(By.id("username"));//-- <input id="username" type="text" value="" autocorrect="off" autocapitalize="off" maxlength="32 autocomplete=" off"="">
		 loginButton = wDriver.findElement(By.id("login-button"));//-- <a id="login-button" class="button single_button" enabled="true">Log in</a>
		 rememberUser = wDriver.findElement(By.id("remember"));//-- 
		 newLoanButton = wDriver.findElement(By.linkText("New Loans")); //--  <a href="tel:6023355007"><div style="float:left;" class="login-bottom-button button new_loans call">New Loans</div></a>
 		 moreOptions =  wDriver.findElement(By.className("offline_more")); //-- <div style="float:right;" class="login-bottom-button button offline_more" data-view="offline-more">More Options</div>
		 newLoanButtonClass = wDriver.findElement(By.className("new_loans")); //--  <a href="tel:6023355007"><div style="float:left;" class="login-bottom-button button new_loans call">New Loans</div></a>
		 moreOptionsClass = wDriver.findElement(By.className("offline_more")); //-- <div style="float:right;" class="login-bottom-button button offline_more" data-view="offline-more">More Options</div>
		 copyRight = wDriver.findElement(By.tagName("footer"));  // -- <footer>© 2014 Desert Schools Federal Credit Union <br> Federally Insured by the NCUA</footer>		

		
		//---- Enter Username
		userNameInput.sendKeys("DSFCU24");	
		// take the screenshot at the end of every test
		demoMain.takePix("c:\\tmp\\userNameLogin.png", wDriver);
        loginButton.click();
          
        //======================
        //-- MFA FLOW 2nd screen possibilities: A: Challenge or B: SiteKey-Passphrase-Password
        
        //------ need to determine which screen has been displayed for this MFA flow        
		wDriver.manage().timeouts().pageLoadTimeout(100, TimeUnit.SECONDS);
		String secondPage = demoMain.collectPageSrc("c:\\tmp\\2ndPage.html", wDriver);		
		Boolean isChallengePage = secondPage.contains("Your Secret Question");
		Boolean isPassPhrasePage2 = secondPage.contains("Do you see the correct picture and phrase?");
		        
        //++ If MFA CHALLENGE do this
        if (isChallengePage) {
        //-- MFA FLOW 2nd screen possibilities: A: Challenge                
        
        //-- 
        //-- Page Elements to collect
        	WebElement inputAnswer = null;
        	WebElement submitButton =  null;
            WebElement title =  null;
            WebElement theQuestion = null;
            WebElement remember_ChkBx =  null;
            WebElement cancel_Btn =  null;

			try{
	        	 inputAnswer = wDriver.findElement( By.id("answer")); //-- <input id="answer" type="password" maxlength="32">
	        	 submitButton = wDriver.findElement(By.id("answer-button")); //-- <a id="answer-button" class="button">Submit</a>
	             title = wDriver.findElement(By.className("login-title")); //-- <div class="login-title">Your Secret Question</div>
	             theQuestion = wDriver.findElement(By.id("question"));//-- <label class="question" id="question">What college was your college rival (abbr: NYU, UCF)?</label>
	             remember_ChkBx = wDriver.findElement(By.id("remember"));//-- <input type="checkbox" id="remember">
	             cancel_Btn = wDriver.findElement(By.id("cancel-button")); //-- <a id="cancel-button" class="button warning-btn">Cancel</a>
			}
			catch (NoSuchElementException e){
				e.printStackTrace();
			}
			
        	inputAnswer.sendKeys("Test1");
        	demoMain.takePix("C:\\tmp\\secondPage.png", wDriver);
        	submitButton.click();
        	
        }else {
        	System.out.println("Second Page is NOT Challenge page value of isPassPhrasePage: "+ isPassPhrasePage2.toString() );
        }
       
        
        //------ need to determine which screen has been displayed for this MFA flow        
		wDriver.manage().timeouts().pageLoadTimeout(100, TimeUnit.SECONDS);
		String thirdPage = demoMain.collectPageSrc("c:\\tmp\\3ndPage.html", wDriver);	
		Boolean isPassPhrasePage3 = thirdPage.contains("Do you see the correct picture and phrase?");

       if (isPassPhrasePage2 || isPassPhrasePage3) {
	
		//++ IF SiteKey-Passphrase-Password do this
		
        //============== NEW Screen ========
        //-- MFA FLOW 2nd screen possibilities: B: SiteKey-Passphrase-Password
      
				
        //-- <div class="login-title">Your picture and phrase</div>
        //-- <div class="phrase-question">Do you see the correct picture and phrase?</div>
				
        //-- <img class="pict" src="data:image/png;base64,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">
        //-- <input id="password" type="password" autocorrect="off" autocapitalize="off" maxlength="32" autocomplete="off">
				
        //-- <label class="fine question">If the picture or phrase is not correct, do not enter your password. Please phone 1-602-433-7000 for help.</label>
        //-- If the picture or phrase is not correct, do not enter your password. Please phone 1-602-433-7000 for help.		
        //-- <label class="fine question">If the picture or phrase is not correct, do not enter your password. Please phone 1-602-433-7000 for help.</label>
        //-- <a id="cancel-button" class="button warning-btn">Cancel</a>
				
        //-- <a id="login-button" class="button">Log in</a>
        //-- 
       // }		
 	
        //============== NEW Screen ========
        
		//++ IF Dashboard do this: 
		
        //========== DASHBOARD ============ 
        //-- <a class="button signout" data-event="logout">Log Out</a>
        //-- <div class="logo"></div>
        //-- <a class="moneybag" data-icon="moneybag" data-view="account-summary" data-viewparam=""><div class="dashboard-label">Accounts</div></a>
        //-- 
        //-- <a class="activity" data-icon="activity" data-view="activity-options" data-viewparam="" style="overflow: visible;"><div class="dashboard-label">View Activity</div><span class="badge top" id="activity-options-badge" style="display: none;">0</span></a>
        //-- 
        //-- 
        //-- 
        //-- 
        //-- 
        //-- 
        //--  <div id="message_box">
        //-- 	<div class="error">
        //-- 		<h2>Your session has expired due to inactivity. Please log in again</h2>
        //-- 	</div>
        //--  </div>
        //-- 
        //-- 
        //-- <h2>Your session has expired due to inactivity. Please log in again</h2>
        
        
		//--- Perform the test ------
		
		
		
		wDriver.close();

	}

}




 



