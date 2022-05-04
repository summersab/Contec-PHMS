package com.contec.phms.util;

import android.util.Log;
//import com.alibaba.sdk.android.Constants;
import com.contec.phms.domain.HospitalBean;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import u.aly.bs;

public class ParseXmlService {
    private final String TAG = "ParseXmlService";

    public HashMap<String, String> parseXml(InputStream inStream) throws Exception {
        HashMap<String, String> hashMap = new HashMap<>();
        NodeList childNodes = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inStream).getDocumentElement().getChildNodes();
        for (int j = 0; j < childNodes.getLength(); j++) {
            Node childNode = childNodes.item(j);
            if (childNode.getNodeType() == 1) {
                Element childElement = (Element) childNode;
                if ("version".equals(childElement.getNodeName())) {
                    hashMap.put("version", childElement.getFirstChild().getNodeValue());
                } else if ("name".equals(childElement.getNodeName())) {
                    hashMap.put("name", childElement.getFirstChild().getNodeValue());
                } else if (Constants.URL.equals(childElement.getNodeName())) {
                    hashMap.put(Constants.URL, childElement.getFirstChild().getNodeValue());
                } else if ("content".equals(childElement.getNodeName())) {
                    hashMap.put("content", childElement.getFirstChild().getNodeValue());
                    String aaa = childElement.getFirstChild().getNodeValue();
                    Log.i("lz", "utf8 = " + getUTF8XMLString(childElement.getFirstChild().getNodeValue()));
                    Log.i("lz", "gbk = " + toGBK(aaa));
                }
            }
        }
        Log.e("lz", "version=" + hashMap.get("version") + "name" + hashMap.get("name") + Constants.URL + hashMap.get(Constants.URL) + hashMap.get("content"));
        return hashMap;
    }

    public String toGBK(String str) throws UnsupportedEncodingException {
        return changeCharset(str, "GBK");
    }

    public String changeCharset(String str, String newCharset) throws UnsupportedEncodingException {
        if (str != null) {
            return new String(str.getBytes(), newCharset);
        }
        return newCharset;
    }

    public static String getUTF8XMLString(String xml) {
        StringBuffer sb = new StringBuffer();
        sb.append(xml);
        String xmlUTF8 = bs.b;
        try {
            String xmString = new String(sb.toString().getBytes("UTF-8"));
            try {
                xmlUTF8 = URLEncoder.encode(xmString, "UTF-8");
                System.out.println("utf-8 encoding: " + xmlUTF8);
                String str = xmString;
            } catch (UnsupportedEncodingException e) {
                e = e;
                String str2 = xmString;
                e.printStackTrace();
                return xmlUTF8;
            }
        } catch (UnsupportedEncodingException e2) {
            UnsupportedEncodingException e = e2;
            e.printStackTrace();
            return xmlUTF8;
        }
        return xmlUTF8;
    }

    public static List<HospitalBean> readXML(String content) {
        List<HospitalBean> hospitals = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        ByteArrayInputStream in = new ByteArrayInputStream(content.getBytes());
        try {
            NodeList items = factory.newDocumentBuilder().parse(in).getDocumentElement().getElementsByTagName("hospitals");
            for (int i = 0; i < items.getLength(); i++) {
                HospitalBean hospital = new HospitalBean();
                NodeList childsNodes = ((Element) items.item(i)).getChildNodes();
                for (int j = 0; j < childsNodes.getLength(); j++) {
                    Node node = childsNodes.item(j);
                    if (node.getNodeType() == 1) {
                        Element childNode = (Element) node;
                        if ("hospitalid".equals(childNode.getNodeName())) {
                            hospital.setHospitalId(childNode.getFirstChild().getNodeValue());
                        } else if (SaveHospitalUtils.spHospitalname.equals(childNode.getNodeName())) {
                            hospital.setHospitalName(childNode.getFirstChild().getNodeValue());
                        }
                    }
                }
                hospitals.add(hospital);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hospitals;
    }
}
