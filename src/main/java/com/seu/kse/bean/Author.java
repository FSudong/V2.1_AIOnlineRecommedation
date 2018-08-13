package com.seu.kse.bean;

public class Author {
    private Integer aid;

    private String authorname;

    private Byte atype;

    private String organization;

    private String url;

    private Integer publishNum;

    private Integer citationNum;

    private Integer Hindex;

    private Double Pindex;

    private String researchInterests;

    public Integer getAminerIndex() {
        return aminerIndex;
    }

    public void setAminerIndex(Integer aminerIndex) {
        this.aminerIndex = aminerIndex;
    }

    private Integer aminerIndex;

    public Integer getPublishNum() {
        return publishNum;
    }

    public void setPublishNum(Integer publishNum) {
        this.publishNum = publishNum;
    }

    public Integer getCitationNum() {
        return citationNum;
    }

    public void setCitationNum(Integer citationNum) {
        this.citationNum = citationNum;
    }

    public Integer getHindex() {
        return Hindex;
    }

    public void setHindex(Integer hindex) {
        Hindex = hindex;
    }

    public Double getPindex() {
        return Pindex;
    }

    public void setPindex(Double pindex) {
        Pindex = pindex;
    }

    public String getResearchInterests() {
        return researchInterests;
    }

    public void setResearchInterests(String researchInterests) {
        this.researchInterests = researchInterests;
    }



    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public Integer getAid() {
        return aid;
    }

    public void setAid(Integer aid) {
        this.aid = aid;
    }

    public String getAuthorname() {
        return authorname;
    }

    public void setAuthorname(String authorname) {
        this.authorname = authorname == null ? null : authorname.trim();
    }

    public Byte getAtype() {
        return atype;
    }

    public void setAtype(Byte atype) {
        this.atype = atype;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization == null ? null : organization.trim();
    }
}