package com.seu.kse.bean;

import java.util.Date;

public class Tag {
    private String tagname;

    private Integer level;

    private String fathername;

    private Date time;

    private String topancestor;

    public String getTagname() {
        return tagname;
    }

    public void setTagname(String tagname) {
        this.tagname = tagname == null ? null : tagname.trim();
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getFathername() {
        return fathername;
    }

    public void setFathername(String fathername) {
        this.fathername = fathername == null ? null : fathername.trim();
    }

    public String getTopAncestor() {
        return topancestor;
    }

    public void setTopAncestor(String topancestor) {
        this.topancestor = topancestor == null ? null : topancestor.trim();
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}