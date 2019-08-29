package com.hoolai.report.entiy;

import com.baomidou.mybatisplus.annotation.TableField;

/**
 * @author: Ksssss(chelin)
 * @create: 2019-08-24 16:39
 */
public class Retention {
    @TableField
    private int gameid;
    @TableField
    private int snid;
    @TableField
    private String ds;
    @TableField
    private int dr;
    private int retention;

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

    public int getDr() {
        return dr;
    }

    public void setDr(int dr) {
        this.dr = dr;
    }

    public int getRetention() {
        return retention;
    }

    public void setRetention(int retention) {
        this.retention = retention;
    }

    @Override
    public String toString() {
        return "Retention{" +
                "gameid=" + gameid +
                ", snid=" + snid +
                ", ds='" + ds + '\'' +
                ", dr=" + dr +
                ", retention=" + retention +
                '}';
    }
}
