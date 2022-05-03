package com.contec.phms.device.fhr01;

public class FHRInfo {
    public short FHr1;
    public short FHr2;
    public short FMove;
    public int Index;
    public short Toco;

    public FHRInfo(short fHr1, int fHr2, int toco, int fMove, int i) {
    }

    public FHRInfo(short fHr1, short fHr2, short toco, short fMove, int index) {
        this.FHr1 = fHr1;
        this.FHr2 = fHr2;
        this.Toco = toco;
        this.FMove = fMove;
        this.Index = index;
    }

    public byte[] toByteArray() {
        return new byte[]{(byte) (this.FHr1 & 255), (byte) ((this.FHr1 >> 8) & 255), (byte) (this.FHr2 & 255), (byte) ((this.FHr2 >> 8) & 255), (byte) (this.Toco & 255), (byte) ((this.Toco >> 8) & 255), (byte) (this.FMove & 255), (byte) ((this.FMove >> 8) & 255), (byte) (this.Index & 255), (byte) ((this.Index >> 8) & 255), (byte) ((this.Index >> 16) & 255), (byte) ((this.Index >> 24) & 255)};
    }
}
