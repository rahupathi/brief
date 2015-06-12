package com.pgrs.models;

import java.io.Serializable;

@SuppressWarnings("serial")
public class TLine implements Serializable {

	private int LineId;
	private String line_code = "";
	private String line_name = "";
	private String created_date="";
	private String delete_flag="";
	public int getLineId() {
		return LineId;
	}

	public void setLineId(int _row_id) {
		LineId = _row_id;
	}

	public String getLineCode() {
		return line_code;
	}

	public void setLineCode(String _line_code) {
		line_code = _line_code;
	}

	public String getLineName() {
		return line_name;
	}
	public void setDeleteFlag(String _delete_flag) {
		delete_flag = _delete_flag;
	}

	public String getDeleteFlag() {
		return delete_flag;
	}
	
	public void setLineName(String _line_name) {
		line_name = _line_name;
	}
	
	public String getCreatedDate() {
		return created_date;
	}

	public void setCreatedDate(String _created_Date) {
		created_date = _created_Date;
	}


}
