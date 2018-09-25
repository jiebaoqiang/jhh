package com.jhh.dc.baika.mapper.gen.domain;

import java.io.Serializable;
import java.util.Date;

public class CodeValueDO implements Serializable {
    private Integer id;

    private String codeType;

    private String codeCode;

    private String meaning;

    private String description;

    private String enabledFlag;

    private String sync;

    private Date updateDate;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodeType() {
        return codeType;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType == null ? null : codeType.trim();
    }

    public String getCodeCode() {
        return codeCode;
    }

    public void setCodeCode(String codeCode) {
        this.codeCode = codeCode == null ? null : codeCode.trim();
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning == null ? null : meaning.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getEnabledFlag() {
        return enabledFlag;
    }

    public void setEnabledFlag(String enabledFlag) {
        this.enabledFlag = enabledFlag == null ? null : enabledFlag.trim();
    }

    public String getSync() {
        return sync;
    }

    public void setSync(String sync) {
        this.sync = sync == null ? null : sync.trim();
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", codeType=").append(codeType);
        sb.append(", codeCode=").append(codeCode);
        sb.append(", meaning=").append(meaning);
        sb.append(", description=").append(description);
        sb.append(", enabledFlag=").append(enabledFlag);
        sb.append(", sync=").append(sync);
        sb.append(", updateDate=").append(updateDate);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        CodeValueDO other = (CodeValueDO) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getCodeType() == null ? other.getCodeType() == null : this.getCodeType().equals(other.getCodeType()))
            && (this.getCodeCode() == null ? other.getCodeCode() == null : this.getCodeCode().equals(other.getCodeCode()))
            && (this.getMeaning() == null ? other.getMeaning() == null : this.getMeaning().equals(other.getMeaning()))
            && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()))
            && (this.getEnabledFlag() == null ? other.getEnabledFlag() == null : this.getEnabledFlag().equals(other.getEnabledFlag()))
            && (this.getSync() == null ? other.getSync() == null : this.getSync().equals(other.getSync()))
            && (this.getUpdateDate() == null ? other.getUpdateDate() == null : this.getUpdateDate().equals(other.getUpdateDate()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getCodeType() == null) ? 0 : getCodeType().hashCode());
        result = prime * result + ((getCodeCode() == null) ? 0 : getCodeCode().hashCode());
        result = prime * result + ((getMeaning() == null) ? 0 : getMeaning().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        result = prime * result + ((getEnabledFlag() == null) ? 0 : getEnabledFlag().hashCode());
        result = prime * result + ((getSync() == null) ? 0 : getSync().hashCode());
        result = prime * result + ((getUpdateDate() == null) ? 0 : getUpdateDate().hashCode());
        return result;
    }
}