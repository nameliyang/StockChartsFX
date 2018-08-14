package com.zoicapital.stockchartsfx;

public class DailyStock {

    private String date;

    /**
     * 开盘价
     */
    private String TOPEN;

    /**
     *  最高价
     */
    private String HIGH;

    /**
     * 收盘价
     */
    private String TCLOSE;


    /**
     * 最低价
     */
    private String LOW;


    private Double ema12;


    private Double ema26;


    private Double diff;

    private Double dea;

    public Double getDea() {
        return dea;
    }

    public void setDea(Double dea) {
        this.dea = dea;
    }

    public Double getDiff() {
        return diff;
    }

    public void setDiff(Double diff) {
        this.diff = diff;
    }

    public Double getEma12() {
        return ema12;
    }

    public void setEma12(Double ema12) {
        this.ema12 = ema12;
    }

    public Double getEma26() {
        return ema26;
    }

    public void setEma26(Double ema26) {
        this.ema26 = ema26;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTCLOSE() {
        return TCLOSE;
    }

    public Double getClose(){
        return Double.parseDouble(TCLOSE);
    }
    public void setTCLOSE(String TCLOSE) {
        this.TCLOSE = TCLOSE;
    }

    public String getHIGH() {
        return HIGH;
    }

    public void setHIGH(String HIGH) {
        this.HIGH = HIGH;
    }

    public String getLOW() {
        return LOW;
    }

    public void setLOW(String LOW) {
        this.LOW = LOW;
    }

    public String getTOPEN() {
        return TOPEN;
    }

    public void setTOPEN(String TOPEN) {
        this.TOPEN = TOPEN;
    }

    @Override
    public String toString() {
        return "DailyStock{" +
                "date='" + date + '\'' +
                ", TCLOSE='" + TCLOSE + '\'' +
                ", HIGH='" + HIGH + '\'' +
                ", LOW='" + LOW + '\'' +
                ", TOPEN='" + TOPEN + '\'' +
                '}';
    }
}
