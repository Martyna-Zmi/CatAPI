package com.example.demo.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class AddNewTestPage {
    private WebDriver webDriver;
    public static final String URL = "http://localhost:8081/addCat";
    @FindBy(id = "name")
    private WebElement nameField;
    @FindBy(id = "age")
    private WebElement ageField;
    @FindBy(id = "add")
    private WebElement addSubmit;
    public AddNewTestPage(WebDriver webDriver){
        this.webDriver = webDriver;
    }
    public void open(){
        webDriver.get(URL);
        PageFactory.initElements(webDriver, this);
    }
    public void fillName(){
        nameField.sendKeys("Buba");
    }
    public void fillAge(){
        ageField.clear();
        ageField.sendKeys("3");
    }
    public void addSubmit(){
        addSubmit.click();
    }
}
