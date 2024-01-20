package com.example.demo.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class EditTestPage {
    private WebDriver webDriver;
    public static final String URL = "http://localhost:8081/editView/1";
    @FindBy(id = "name")
    private WebElement nameField;
    @FindBy(id = "age")
    private WebElement ageField;
    @FindBy(id = "edit")
    private WebElement editSubmit;
    public EditTestPage(WebDriver webDriver){
        this.webDriver = webDriver;
    }
    public void open(){
        webDriver.get(URL);
        PageFactory.initElements(webDriver, this);
    }
    public void fillForm(){
        ageField.clear();
        ageField.sendKeys("3");
        nameField.clear();
        nameField.sendKeys("Mieczysław");
    }
    public void submitEdit(){
        editSubmit.click();
    }
}
