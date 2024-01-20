package com.example.demo.model;

public class Cat {
    private Long id;
    private String name;
    private Integer age;
    private String colour;
    public Cat(String name, int age, String colour){
        this.name = name;
        this.age = age;
        this.colour = colour;
    }
    protected Cat(){
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }
    public String getColour() {
        return colour;
    }
}
