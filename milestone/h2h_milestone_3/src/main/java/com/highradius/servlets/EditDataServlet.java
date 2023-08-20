package com.highradius.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.highradius.implementation.InvoiceDaoImpl;
import com.highradius.model.Invoice;
import java.io.PrintWriter;
import java.util.List;

/**
 * Servlet implementation class EditDataServlet
 */
@WebServlet("/EditDataServlet")
public class EditDataServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public EditDataServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		setAccessControlHeaders(response);
		// TODO Auto-generated method stub

		int slNo = Integer.parseInt(request.getParameter("slNo"));

		// Retrieve the invoice record based on the "slNo"
		InvoiceDaoImpl invoiceDaoImpl = new InvoiceDaoImpl();
		Invoice invoice = invoiceDaoImpl.getInvoiceBySlNo(slNo);
		// Create Gson instance
		Gson gson = new GsonBuilder().create();

		// Convert the data to JSON format
		String jsonInvoices = gson.toJson(invoice);

		// Set the response type to JSON
		response.setContentType("application/json");

		// Send the JSON response back to the client
		PrintWriter out = response.getWriter();
		out.print(jsonInvoices);
		out.flush();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		setAccessControlHeaders(response);
		// Retrieve the "slNo" parameter from the request
		int slNo = Integer.parseInt(request.getParameter("slNo"));

		BufferedReader reader = request.getReader();
		StringBuilder requestBody = new StringBuilder();
		String line;
		while ((line = reader.readLine()) != null) {
			requestBody.append(line);
		}
		reader.close();

		Gson gson = new Gson();

		// Parse the JSON string to an Invoice object
		Invoice invoice = gson.fromJson(requestBody.toString(), Invoice.class);

		// Retrieve data from the parsed object
		int customerOrderID = invoice.getCustomerOrderID();
		int salesOrg = invoice.getSalesOrg();
		String distributionChannel = invoice.getDistributionChannel();
		String division = invoice.getDivision();
		float releasedCreditValue = invoice.getReleasedCreditValue();
		String purchaseOrderType = invoice.getPurchaseOrderType();
		int companyCode = invoice.getCompanyCode();
		String orderCreationDate = invoice.getOrderCreationDate();
		String orderCreationTime = invoice.getOrderCreationTime();
		String creditControlArea = invoice.getCreditControlArea();
		int soldToParty = invoice.getSoldToParty();
		float orderAmount = invoice.getOrderAmount();
		String requestedDeliveryDate = invoice.getRequestedDeliveryDate();
		String orderCurrency = invoice.getOrderCurrency();
		String creditStatus = invoice.getCreditStatus();
		int customerNumber = invoice.getCustomerNumber();
		float amountInUsd = invoice.getAmountInUsd();
		long uniqueCustNumber = invoice.getUniqueCustNumber();

		Invoice newInvoice = new Invoice(slNo, customerOrderID, salesOrg, distributionChannel, division,
				releasedCreditValue, purchaseOrderType, companyCode, orderCreationDate, orderCreationTime,
				creditControlArea, soldToParty, orderAmount, requestedDeliveryDate, orderCurrency, creditStatus,
				customerNumber, amountInUsd, uniqueCustNumber);

		InvoiceDaoImpl invoiceDaoImpl = new InvoiceDaoImpl();
		invoiceDaoImpl.updateInvoice(slNo, newInvoice);

		Gson ggson = new GsonBuilder().create();

		// Convert the data to JSON format
		String responses = ggson.toJson("Invoice has been Updated successfully");

		response.setContentType("application/json");

		// Send the JSON response back to the client
		PrintWriter out = response.getWriter();
		out.print(responses);
		out.flush();

	}

	private void setAccessControlHeaders(HttpServletResponse resp) {
		resp.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
		resp.setHeader("Access-Control-Allow-Methods", "POST, OPTIONS");
		resp.setHeader("Access-Control-Allow-Headers", "Content-Type");
	}

}
