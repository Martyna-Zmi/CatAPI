package com.example.demo.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class DeleteTestPage {
    private WebDriver webDriver;
    public final static String URL = "http://localhost:8081/deleteView/1";
    @FindBy(id = "confirm")
    private WebElement confirmCheckBox;
    @FindBy(id = "delete")
    private WebElement deleteSubmit;
    public DeleteTestPage(WebDriver webDriver){
        this.webDriver = webDriver;
    }
    public void open(){
        webDriver.get(URL);
        PageFactory.initElements(webDriver, this);
    }
    public void checkConfirm(){
        confirmCheckBox.click();
    }
    public void submitDelete(){
        deleteSubmit.click();
    }
}
