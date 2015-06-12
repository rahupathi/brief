package com.pgrs.models;

public class CMemberCollection extends TMembers {

	private String CollectionDate = "";
	private Double Amount;

	public String getCollectinDate() {
		return CollectionDate;
	}

	public void setCollectionDate(String _collection_date) {
		CollectionDate = _collection_date;
	}

	public Double getAmount() {
		return Amount;
	}

	public void setAmount(Double _Amount) {
		Amount = _Amount;
	}

}
