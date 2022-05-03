package com.contec.phms.upload.cases.BC01;

import com.contec.phms.util.CLog;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class XML_BC01 {
    private String BIL;
    private String BLD;
    private String GLU;
    private String KET;
    private String LEU;
    private String NIT;
    private String PH;
    private String PRO;
    private String SG;
    private String URO;
    private String VC;
    private String ch01;
    private String ua;

    public int TOXML(String filename) {
        String _uploadXMlString = "<?xml version=\"1.0\" encoding=\"GBK\" ?><cmdinfo><ua>" + this.ua + "</ua>" + "<chol>" + this.ch01 + "</chol>" + "<URO>" + this.URO + "</URO>" + "<BLD>" + this.BLD + "</BLD>" + "<BIL>" + this.BIL + "</BIL>" + "<KET>" + this.KET + "</KET>" + "<GLU>" + this.GLU + "</GLU>" + "<PRO>" + this.PRO + "</PRO>" + "<PH>" + this.PH + "</PH>" + "<NIT>" + this.NIT + "</NIT>" + "<LEU>" + this.LEU + "</LEU>" + "<SG>" + this.SG + "</SG>" + "<VC>" + this.VC + "</VC>" + "</cmdinfo>";
        if (filename.trim().length() == 0) {
            return -1;
        }
        try {
            File fp = new File(filename);
            if (!fp.exists()) {
                fp.createNewFile();
            }
            OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(fp));
            osw.write(_uploadXMlString, 0, _uploadXMlString.length());
            osw.flush();
            osw.close();
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            CLog.i("MakeBaseCase", filename);
            return -1;
        }
    }

    public void setUa(String ua) {
        this.ua = ua;
    }

    public void setCh01(String ch012) {
        this.ch01 = ch012;
    }

    public void setURO(int uRO) {
        switch (uRO) {
            case 0:
                this.URO = "Norm";
                return;
            case 1:
                this.URO = "1+";
                return;
            case 2:
                this.URO = "2+";
                return;
            case 3:
                this.URO = ">=3+";
                return;
            default:
                return;
        }
    }

    public void setBLD(int bLD) {
        switch (bLD) {
            case 0:
                this.BLD = "-";
                return;
            case 1:
                this.BLD = "+-";
                return;
            case 2:
                this.BLD = "1+";
                return;
            case 3:
                this.BLD = "2+";
                return;
            case 4:
                this.BLD = "3+";
                return;
            default:
                return;
        }
    }

    public void setBIL(int bIL) {
        switch (bIL) {
            case 0:
                this.BIL = "-";
                return;
            case 1:
                this.BIL = "1+";
                return;
            case 2:
                this.BIL = "2+";
                return;
            case 3:
                this.BIL = "3+";
                return;
            default:
                return;
        }
    }

    public void setKET(int kET) {
        switch (kET) {
            case 0:
                this.KET = "-";
                return;
            case 1:
                this.KET = "1+";
                return;
            case 2:
                this.KET = "2+";
                return;
            case 3:
                this.KET = "3+";
                return;
            default:
                return;
        }
    }

    public void setGLU(int gLU) {
        switch (gLU) {
            case 0:
                this.GLU = "-";
                return;
            case 1:
                this.GLU = "+-";
                return;
            case 2:
                this.GLU = "1+";
                return;
            case 3:
                this.GLU = "2+";
                return;
            case 4:
                this.GLU = "3+";
                return;
            case 5:
                this.GLU = "4+";
                return;
            default:
                return;
        }
    }

    public void setPRO(int pRO) {
        switch (pRO) {
            case 0:
                this.PRO = "-";
                return;
            case 1:
                this.PRO = "+-";
                return;
            case 2:
                this.PRO = "1+";
                return;
            case 3:
                this.PRO = "2+";
                return;
            case 4:
                this.PRO = ">=3+";
                return;
            default:
                return;
        }
    }

    public void setPH(int pH) {
        switch (pH) {
            case 0:
                this.PH = "5";
                return;
            case 1:
                this.PH = "6";
                return;
            case 2:
                this.PH = "7";
                return;
            case 3:
                this.PH = "8";
                return;
            case 4:
                this.PH = "9";
                return;
            default:
                return;
        }
    }

    public void setNIT(int nIT) {
        switch (nIT) {
            case 0:
                this.NIT = "-";
                return;
            case 1:
                this.NIT = "1+";
                return;
            default:
                return;
        }
    }

    public void setLEU(int lEU) {
        switch (lEU) {
            case 0:
                this.LEU = "-";
                return;
            case 1:
                this.LEU = "+-";
                return;
            case 2:
                this.LEU = "1+";
                return;
            case 3:
                this.LEU = "2+";
                return;
            case 4:
                this.LEU = "3+";
                return;
            default:
                return;
        }
    }

    public void setSG(int sG) {
        switch (sG) {
            case 0:
                this.SG = "<=1.005";
                return;
            case 1:
                this.SG = "1.010";
                return;
            case 2:
                this.SG = "1.015";
                return;
            case 3:
                this.SG = "1.020";
                return;
            case 4:
                this.SG = "1.025";
                return;
            case 5:
                this.SG = ">=1.030";
                return;
            default:
                return;
        }
    }

    public void setVC(int vC) {
        switch (vC) {
            case 0:
                this.VC = "-";
                return;
            case 1:
                this.VC = "+-";
                return;
            case 2:
                this.VC = "1+";
                return;
            case 3:
                this.VC = "2+";
                return;
            case 4:
                this.VC = "3+";
                return;
            default:
                return;
        }
    }
}
