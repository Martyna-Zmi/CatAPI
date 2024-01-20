package com.example.demo;

import com.example.demo.model.Cat;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.when;
import static io.restassured.RestAssured.with;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RestAssuredTests {
    private static final String URI = "http://localhost:8080";
    @Test //opcja 1 [JSONowa]
    public void testGetCat(){
        when().get(URI+"/cat/find/1").then().statusCode(200)
                .assertThat()
                .body("id", equalTo(1))
                .body("name", equalTo("MILEK"))
                .log().body();
    }
    @Test //opcja 2 [obiektowa]
    public void testGetCat2(){
        Cat cat = when().get(URI+"/cat/find/1").then().statusCode(200)
                .extract()
                .as(Cat.class);
        assertEquals(1, cat.getId());
    }
    @Test //dodawanie [faktycznie dodaje]
    public void testPostCat(){
        with().body(new Cat("BUBU", 2, "czarny"))
                .contentType("application/json")
                .post(URI+"/cat/add")
                .then()
                .assertThat()
                .body("id", greaterThan(1))
                .body("name", equalTo("BUBU"))
                .body("colour", equalTo("czarny"))
                .body("age", equalTo(2))
                .statusCode(200);
    }
    @Test
    public void testPutCat(){
        Cat cat = new Cat("BUBU", 3, "czarny");
        cat.setId(2L);
        with().body(cat)
                .contentType("application/json")
                .put(URI+"/cat/update")
                .then()
                .assertThat()
                .body("id", equalTo(2))
                .body("name", equalTo("BUBU"))
                .body("colour", equalTo("czarny"))
                .body("age", equalTo(3))
                .statusCode(200);
    }
}
