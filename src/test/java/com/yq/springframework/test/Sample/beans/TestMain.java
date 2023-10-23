package com.yq.springframework.test.Sample.beans;

public class TestMain {
    /*public static void main(String[] args) throws FileNotFoundException {
        File file = new File("file:\\D:\\study_proj\\1.java\\13.my-spring-framework\\yq-spring\\src\\test\\resources\\spring-context.xml");
        Document doc = XmlUtil.readXML(file);

        Element root = doc.getDocumentElement();

        NodeList nodes = root.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node item = nodes.item(i);
            System.out.println(item.getClass());
        }
    }*/

    public static void main(String[] args) {
/*        String fileCp = "classpath:spring-context.xml";
        InputStream is = TestMain.class.getResourceAsStream(fileCp);
        System.out.println(IoUtil.read(is,"UTF-8"));*/

        System.getProperties().forEach((k,v) -> System.out.println(k + "\t\t\t====>\t" + v));
    }
}
