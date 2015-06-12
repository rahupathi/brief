package com.pgrs.models;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class CMembersByLine implements Serializable {

	private String linename;
	private ArrayList<TMembers> memberList = new ArrayList<TMembers>();

	public CMembersByLine(String name, ArrayList<TMembers> memberList) {
		super();
		this.linename = name;
		this.memberList = memberList;
	}

	public String getName() {
		return linename;
	}

	public void setLineName(String name) {
		this.linename = name;
	}

	public ArrayList<TMembers> getMembersList() {
		return memberList;
	}

	public void setMembersList(ArrayList<TMembers> _memberList) {
		this.memberList = _memberList;
	};

}
