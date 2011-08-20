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
	private Long dialogIdFilter2 = null;
	private boolean tcapMsgData = false;
	private boolean detailedDialog = false;
	private boolean detailedComponents = false;
	
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
	
	public Long getDialogIdFilter2() {
		return dialogIdFilter2;
	}

	public boolean getTcapMsgData() {
		return tcapMsgData;
	}

	public boolean getDetailedDialog() {
		return detailedDialog;
	}

	public boolean getDetailedComponents() {
		return detailedComponents;
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
	
	public void setDialogIdFilter2(Long dialogIdFilter2) {
		this.dialogIdFilter2 = dialogIdFilter2;
	}

	public void setTcapMsgData(boolean tcapMsgData) {
		this.tcapMsgData = tcapMsgData;
	}
	
	public void setDetailedDialog(boolean detailedDialog) {
		this.detailedDialog = detailedDialog;
	}

	public void setDetailedComponents(boolean detailedComponents) {
		this.detailedComponents = detailedComponents;
	}
}

