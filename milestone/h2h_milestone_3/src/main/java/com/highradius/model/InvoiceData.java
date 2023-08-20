package com.highradius.model;

public class InvoiceData {
    private String customerCount;
    private String distributionChannel;
    private double totalAmount;

    public InvoiceData(String distributionChannel, double totalAmount) {
        this.distributionChannel = distributionChannel;
        this.totalAmount = totalAmount;
    }
    
    public InvoiceData( double totalAmount,String customerCount) {
        this.customerCount = customerCount;
        this.totalAmount = totalAmount;
    }

	protected String getCustomerCount() {
		return customerCount;
	}

	protected void setCustomerCount(String customerCount) {
		this.customerCount = customerCount;
	}

	protected String getDistributionChannel() {
		return distributionChannel;
	}

	protected void setDistributionChannel(String distributionChannel) {
		this.distributionChannel = distributionChannel;
	}

	protected double getTotalAmount() {
		return totalAmount;
	}

	protected void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

    // Getters and setters (if needed) for distributionChannel and totalAmount
    // Other methods (if needed)
}
