package com.yq.springframework.test.Sample.beans;

public class TestBean {

    private int id;
    private String name;

    public TestBean(int id, String name) {
        this.id = id;
        this.name = name;
    }


    public String info(){
        return "TestBean I am";
    }


}
