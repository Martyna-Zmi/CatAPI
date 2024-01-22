package com.example.demo.restAssured;

import com.example.demo.model.Cat;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.when;
import static io.restassured.RestAssured.with;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RestAssuredTests {
    private static final String URI = "http://localhost:8080";
    @Test
    public void testGetCat(){
        when().get(URI+"/cat/find/1").then().statusCode(200)
                .assertThat()
                .body("id", equalTo(1))
                .body("name", equalTo("MILEK"))
                .body("colour", equalTo("czarny"))
                .log().body();
    }
    @Test
    public void testGetCatFailed(){
        when().get(URI+"/cat/find/100")
                .then()
                .statusCode(404); //no such cat
    }
    @Test
    public void testGetCat2(){
        Cat cat = when().get(URI+"/cat/find/1").then().statusCode(200)
                .extract()
                .as(Cat.class);
        assertEquals(1, cat.getId());
    }
    @Test
    public void testPostCat(){
        Cat cat = new Cat("BUBU", 2, "czarny");
        cat.setId(2L);
        with().body(cat)
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
    public void testPostCatFailed(){
        Cat cat = new Cat("BUBU", 2, "czarny");
        cat.setId(1L);
        with().body(cat)
                .contentType("application/json")
                .post(URI+"/cat/add")
                .then()
                .statusCode(400); //cat already exists
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
    @Test
    public void testPutCatFailed(){
        Cat cat = new Cat("BUBU", 3, "czarny");
        cat.setId(10L);
        with().body(cat)
                .contentType("application/json")
                .put(URI+"/cat/update")
                .then()
                .statusCode(404); //can't update because cat doesn't exist
    }
    @Test
    public void testDelete(){
        with().contentType("application/json")
                .delete(URI+"/cat/deleteById/2")
                .then()
                .statusCode(200);
    }
    @Test
    public void testDeleteFailed(){
        with().contentType("application/json")
                .delete(URI+"/cat/deleteById/10")
                .then()
                .statusCode(404); //can't delete because cat doesn't exist
    }
}
