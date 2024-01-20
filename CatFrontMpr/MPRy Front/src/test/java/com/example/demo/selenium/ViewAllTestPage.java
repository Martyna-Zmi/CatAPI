package com.example.demo.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ViewAllTestPage {
    private WebDriver webDriver;
    public static final String URL = "http://localhost:8081/viewAll";
    @FindBy(id = "edit")
    private WebElement editLink;
    @FindBy(id = "delete")
    private WebElement deleteLink;
    @FindBy(id = "new")
    private WebElement newLink;
    public ViewAllTestPage(WebDriver webDriver){
        this.webDriver = webDriver;
    }
    public void open(){
        webDriver.get(URL);
        PageFactory.initElements(webDriver, this);
    }
    public void goToEdit(){
        editLink.click();
    }
    public void goToDelete(){
        deleteLink.click();
    }
    public void goToNew(){
        newLink.click();
    }
}
