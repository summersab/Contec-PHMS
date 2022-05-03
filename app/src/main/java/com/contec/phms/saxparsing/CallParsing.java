package com.contec.phms.saxparsing;

import com.contec.phms.manager.message.MessageFromServer;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class CallParsing {
    private static CallParsing mCallparsing;

    public static CallParsing getInstance() {
        if (mCallparsing == null) {
            mCallparsing = new CallParsing();
        }
        return mCallparsing;
    }

    public List<MessageFromServer> getMessageFromServer(String pContent) {
        List<MessageFromServer> _listMessage = new ArrayList<>();
        try {
            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            XmlParser handler = new XmlParser();
            parser.parse(new ByteArrayInputStream(pContent.getBytes()), handler);
            return handler.getArray();
        } catch (Exception e) {
            e.printStackTrace();
            return _listMessage;
        }
    }
}
