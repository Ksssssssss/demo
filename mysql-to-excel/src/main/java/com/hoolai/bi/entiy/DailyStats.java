package com.hoolai.bi.entiy;/**
 *
 *
 *@description: report
 *@author: Ksssss(chenlin@hoolai.com)
 *@time: 2019-09-10 17:11
 * 
 */
 
public class DailyStats {
    /** 游戏id*/
    private int gameid;
    /** 平台id*/
    private int snid;
    /** 日期 */
    private String ds;
    /** 总安装 */
    private int totalNum;
    /** 日活跃 */
    private int dauNum;
    /** 日安装 */
    private int installNum;
    /** 日付费人数 */
    private int payCount;
    /** 日付费次数 */
    private int payTimes;
    /** 日付费金额 */
    private int payAmount;
    /** 日安装付费人数 */
    private int payInstallCount;
    /** 日安装付费人数 */
    private int payInstallTimes;
    /** 日安装付费金额 */
    private int payInstallAmount;

    public int getGameid() {
        return gameid;
    }

    public void setGameid(int gameid) {
        this.gameid = gameid;
    }

    public int getSnid() {
        return snid;
    }

    public void setSnid(int snid) {
        this.snid = snid;
    }

    public String getDs() {
        return ds;
    }

    public void setDs(String ds) {
        this.ds = ds;
    }


    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public int getDauNum() {
        return dauNum;
    }

    public void setDauNum(int dauNum) {
        this.dauNum = dauNum;
    }

    public int getInstallNum() {
        return installNum;
    }

    public void setInstallNum(int installNum) {
        this.installNum = installNum;
    }

    public int getPayCount() {
        return payCount;
    }

    public void setPayCount(int payCount) {
        this.payCount = payCount;
    }

    public int getPayTimes() {
        return payTimes;
    }

    public void setPayTimes(int payTimes) {
        this.payTimes = payTimes;
    }

    public int getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(int payAmount) {
        this.payAmount = payAmount;
    }

    public int getPayInstallCount() {
        return payInstallCount;
    }

    public void setPayInstallCount(int payInstallCount) {
        this.payInstallCount = payInstallCount;
    }

    public int getPayInstallTimes() {
        return payInstallTimes;
    }

    public void setPayInstallTimes(int payInstallTimes) {
        this.payInstallTimes = payInstallTimes;
    }

    public int getPayInstallAmount() {
        return payInstallAmount;
    }

    public void setPayInstallAmount(int patInstallAmount) {
        this.payInstallAmount = patInstallAmount;
    }

    @Override
    public String toString() {
        return "DailyStats{" +
                "gameid=" + gameid +
                ", snid=" + snid +
                ", ds='" + ds + '\'' +
                ", totalNum=" + totalNum +
                ", dauNum=" + dauNum +
                ", installNum=" + installNum +
                ", payCount=" + payCount +
                ", payTimes=" + payTimes +
                ", payAmount=" + payAmount +
                ", payInstallCount=" + payInstallCount +
                ", payInstallTimes=" + payInstallTimes +
                ", payInstallAmount=" + payInstallAmount +
                '}';
    }
}
