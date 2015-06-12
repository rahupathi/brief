package com.pgrs.models;

import java.io.Serializable;

@SuppressWarnings("serial")
public class TMembers implements Serializable {

	private String line_code = "";
	private int member_id;
	private int line_id;
	private String member_code = "";
	private String member_name = "";
	private String guarateed_by = "";
	private String alter_no = "";
	private String delete_flag = "";
	private String mobile_no = "";
	private String address = "";
	private String created_date="";
	private String created_by="";
	private byte[] photo=null;
	public String getLineCode() {
		return line_code;
	}

	public void setLineCode(String _line_code) {
		line_code = _line_code;
	}

	public void setMemberId(int _member_ID) {
		member_id = _member_ID;
	}

	public int getMemberId() {
		return member_id;
	}

	public void setLineId(int _line_id) {
		line_id = _line_id;
	}

	public int getLineId() {
		return line_id;
	}

	public void setDeleteFlag(String _delete_flag) {
		delete_flag = _delete_flag;
	}

	public String getDeleteFlag() {
		return delete_flag;
	}

	public String getMemberCode() {
		return member_code;
	}

	public void setMemberCode(String _member_code) {
		member_code = _member_code;
	}

	public String getMemberName() {
		return member_name;
	}

	public void setMemberName(String _member_name) {
		member_name = _member_name;
	}

	public String getAlternateNo() {
		return alter_no;
	}

	public void setAlternateNo(String _alter_no) {
		alter_no = _alter_no;
	}

	public String getGuaranteedBy() {
		return guarateed_by;
	}

	public void setGuaranteedBy(String _guarateed_by) {
		guarateed_by = _guarateed_by;
	}

	public String getMobileNumber() {
		return mobile_no;
	}

	public void setMobileNumber(String _mobileNumber) {
		mobile_no = _mobileNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String _address) {
		address = _address;
	}
	public String getCreatedDate() {
		return created_date;
	}

	public void setCreatedDate(String _created_Date) {
		created_date = _created_Date;
	}
	
	public String getCreatedBy() {
		return created_by;
	}

	public void setCreatedBy(String _CreatedBy) {
		created_by = _CreatedBy;
	}
	
	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] _photo) {
		photo = _photo;
	}
}
