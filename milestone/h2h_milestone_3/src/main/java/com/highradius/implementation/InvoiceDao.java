package com.highradius.implementation;

import com.highradius.model.Invoice;
import com.highradius.model.InvoiceData;

import java.sql.ResultSet;
import java.util.List;

public interface InvoiceDao {
    List<Invoice> getInvoice();
    void insertInvoice(Invoice invoice);
    void updateInvoice(int id, Invoice invoice);
	boolean deleteInvoice(int slNo);
	Invoice getInvoiceBySlNo(int slNo);
	Invoice getInvoiceByCustomerOrderId(int customerOrderID);
	Invoice getInvoiceByAdvanceSearch(int customerOrderID, int customerNumber, int salesOrg);
	Invoice setInvoiceBySlNo(int slNo, float orderAmount, float amountInUsd);
	List<InvoiceData> getAllInvoicesForAnalyticalView(int customerNumber, String distributionChannel);
}
