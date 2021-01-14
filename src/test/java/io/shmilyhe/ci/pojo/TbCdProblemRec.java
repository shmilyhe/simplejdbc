package io.shmilyhe.ci.pojo;

import java.util.Date;


/**
 * <p>
 * 
 * </p>
 *
 * @author admin
 * @since 2020-01-06
 */

public class TbCdProblemRec   {

    private static final long serialVersionUID = 1L;


    private Integer problemId;

    private String problemTitle;

    private String problemDesc;

    private Integer problemStatus;

    private Integer creator;

    private String creatorName;

    private Date createTime;

    public Integer getProblemId() {
        return problemId;
    }

	/**
     * setProblemId
     * @param problemId Integer
     * @return TbCdProblemRec
     */
    public TbCdProblemRec setProblemId(Integer problemId) {
        this.problemId = problemId;
        return this;
    }
    public String getProblemTitle() {
        return problemTitle;
    }

	/**
     * setProblemTitle
     * @param problemTitle String
     * @return TbCdProblemRec
     */
    public TbCdProblemRec setProblemTitle(String problemTitle) {
        this.problemTitle = problemTitle;
        return this;
    }
    public String getProblemDesc() {
        return problemDesc;
    }

	/**
     * setProblemDesc
     * @param problemDesc String
     * @return TbCdProblemRec
     */
    public TbCdProblemRec setProblemDesc(String problemDesc) {
        this.problemDesc = problemDesc;
        return this;
    }
    public Integer getProblemStatus() {
        return problemStatus;
    }

	/**
     * setProblemStatus
     * @param problemStatus Integer
     * @return TbCdProblemRec
     */
    public TbCdProblemRec setProblemStatus(Integer problemStatus) {
        this.problemStatus = problemStatus;
        return this;
    }
    public Integer getCreator() {
        return creator;
    }

	/**
     * setCreator
     * @param creator Integer
     * @return TbCdProblemRec
     */
    public TbCdProblemRec setCreator(Integer creator) {
        this.creator = creator;
        return this;
    }
    public Date getCreateTime() {
        return createTime;
    }

	/**
     * setCreateTime
     * @param createTime Date
     * @return TbCdProblemRec
     */
    public TbCdProblemRec setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    @Override
    public String toString() {
        return "TbCdProblemRec{" 
	    + "problemId=" + problemId 
	    + ", problemTitle=" + problemTitle 
	    + ", problemDesc=" + problemDesc 
	    + ", problemStatus=" + problemStatus 
	    + ", creator=" + creator 
	    + ", createTime=" + createTime 
	    + "}";
    }
}
