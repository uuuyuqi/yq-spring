package com.yq.springframework.test.Sample.beans;

public class TestBean {

    private int id = 9999999;
    private String name = "YOUR TEST WENT WRONG! PLEASE CHECK!";



    public TestBean(Integer id, String name) {
        this.id = id;
        this.name = name;
        System.out.println("TestBean(Integer id, String name) was used");
    }

    public TestBean(int id, String name) {
        this.id = id;
        this.name = name;
        System.out.println("TestBean(int id, String name) was used");
    }


    public String info(){
        return id + "" + name;
    }

    public String speakSelf(){
        return "TestBean I am";
    }


}
