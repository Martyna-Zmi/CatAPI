package com.example.demo.selenium;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.Select;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SeleniumTest {
    WebDriver webDriver;
    @BeforeEach
    public void setUp(){
        webDriver = new EdgeDriver();
    }
    @Test
    public void goFromViewAllToEdit(){
        ViewAllTestPage page = new ViewAllTestPage(webDriver);
        page.open();
        page.goToEdit();
        assertEquals(webDriver.getCurrentUrl(), EditTestPage.URL);
    }
    @Test
    public void goFromViewAllToDelete(){
        ViewAllTestPage page = new ViewAllTestPage(webDriver);
        page.open();
        page.goToDelete();
        assertEquals(webDriver.getCurrentUrl(), DeleteTestPage.URL);
    }
    @Test
    public void goFromViewAllToNew(){
        ViewAllTestPage page = new ViewAllTestPage(webDriver);
        page.open();
        page.goToNew();
        assertEquals(webDriver.getCurrentUrl(), AddNewTestPage.URL);
    }
    @Test
    public void editFormSubmit(){
        EditTestPage page = new EditTestPage(webDriver);
        page.open();
        page.fillForm();
        page.submitEdit();
        assertEquals(webDriver.getCurrentUrl(), ViewAllTestPage.URL);
    }
    @Test
    public void addFormSubmit(){
        AddNewTestPage page = new AddNewTestPage(webDriver);
        page.open();
        page.fillAge();
        page.fillName();
        Select colourSelect = new Select(webDriver.findElement(By.name("colour")));
        colourSelect.selectByVisibleText("czarny");
        page.addSubmit();
        assertEquals(webDriver.getCurrentUrl(), ViewAllTestPage.URL);
    }
    @Test
    public void deleteFormSubmit(){
        DeleteTestPage page = new DeleteTestPage(webDriver);
        page.open();
        page.checkConfirm();
        page.submitDelete();
        assertEquals(webDriver.getCurrentUrl(), ViewAllTestPage.URL);
    }
}
