package org.mobicents.protocols.ss7.tools.traceparser;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class Ss7ParseParameters {
	
	private int fileType = 1;	
	private String sourceFilePath = "";	
	private String msgLogFilePath = "";	
	private Integer applicationContextFilter = null;	
	private Long dialogIdFilter = null;
	
	public int getFileType() {
		return fileType;
	}
	
	public String getSourceFilePath() {	
		return sourceFilePath;
	}
	
	public String getMsgLogFilePath() {	
		return msgLogFilePath;
	}
	
	public Integer getApplicationContextFilter() {
		return applicationContextFilter;
	}
	
	public Long getDialogIdFilter() {
		return dialogIdFilter;
	}

	
	public void setFileType( int fileType ) {
		this.fileType = fileType;
	}
	
	public void setSourceFilePath(String sourceFilePath) {	
		this.sourceFilePath = sourceFilePath;
	}

	public void setMsgLogFilePath(String msgLogFilePath) {
		this.msgLogFilePath = msgLogFilePath;
	}
	
	public void setApplicationContextFilter(Integer applicationContextFilter) {
		this.applicationContextFilter = applicationContextFilter;
	}
	
	public void setDialogIdFilter(Long dialogIdFilter) {
		this.dialogIdFilter = dialogIdFilter;
	}
}

