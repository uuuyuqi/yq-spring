package com.yq.springframework.test.Sample.beans;

public class TestBeanPlus {
    private TestBean testBean;

    public TestBeanPlus() {
    }

    public TestBeanPlus(TestBean bean) {
        this.testBean = bean;
    }

    public TestBean getTestBean() {
        return testBean;
    }

    public void setTestBean(TestBean bean) {
        this.testBean = bean;
    }

    public String getInfo(){
        return testBean.info();
    }
}
