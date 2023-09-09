package com.atm.atmmachine.dto;

public class Transaction {

	
	
		public String orderId ;
		public String currency ;
		public Integer amount ;
		private String key;
		
		public Transaction(String orderId, String currency, Integer amount, String key) {
			super();
			this.orderId = orderId;
			this.currency = currency;
			this.amount = amount;
			this.key = key;
		}
		public Transaction() {
			super();
		}
		public String getOrderId() {
			return orderId;
		}
		public void setOrderId(String orderId) {
			this.orderId = orderId;
		}
		public String getCurrency() {
			return currency;
		}
		public void setCurrency(String currency) {
			this.currency = currency;
		}
		public Integer  getAmount() {
			return amount;
		}
		public void setAmount(Integer amount) {
			this.amount = amount;
		}
		public String getKey() {
			return key;
		}
		public void setKey(String key) {
			this.key = key;
		}
}
