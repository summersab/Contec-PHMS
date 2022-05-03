package cn.com.contec_net_3_android_case;

import org.apache.commons.httpclient.methods.multipart.FilePart;

public class FormFile {
    private String contentType = FilePart.DEFAULT_CONTENT_TYPE;
    private byte[] data;
    private String filname;
    private String formname;

    public FormFile(String filname2, byte[] data2, String formname2, String contentType2) {
        this.data = data2;
        this.filname = filname2;
        this.formname = formname2;
        if (contentType2 != null) {
            this.contentType = contentType2;
        }
    }

    public byte[] getData() {
        return this.data;
    }

    public void setData(byte[] data2) {
        this.data = data2;
    }

    public String getFilname() {
        return this.filname;
    }

    public void setFilname(String filname2) {
        this.filname = filname2;
    }

    public String getFormname() {
        return this.formname;
    }

    public void setFormname(String formname2) {
        this.formname = formname2;
    }

    public String getContentType() {
        return this.contentType;
    }

    public void setContentType(String contentType2) {
        this.contentType = contentType2;
    }
}
