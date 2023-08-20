package com.highradius.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.highradius.implementation.InvoiceDaoImpl;
import com.highradius.model.Invoice;
import com.highradius.model.InvoiceData;

/**
 * Servlet implementation class AnalyticsViewDataServlet
 */
@WebServlet("/AnalyticsViewDataServlet")
public class AnalyticsViewDataServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AnalyticsViewDataServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
		response.setHeader("Access-Control-Allow-Methods", "GET, OPTIONS");
		response.setHeader("Access-Control-Allow-Headers", "Content-Type");

		// TODO Auto-generated method stub
		String customerNumberParam = request.getParameter("CustomerNumber");
		String distributionChannelParam = request.getParameter("DistributionChannel");

		int customerNumber;
		String distributionChannel;

		if (customerNumberParam != null && distributionChannelParam != null) {
			// Both parameters present
			customerNumber = Integer.parseInt(customerNumberParam);
			distributionChannel = distributionChannelParam;
		} else if (customerNumberParam != null) {
			// Only CustomerNumber parameter present
			customerNumber = Integer.parseInt(customerNumberParam);
			distributionChannel = ""; // Set distributionChannel to an appropriate default value
		} else if (distributionChannelParam != null) {
			// Only DistributionChannel parameter present
			customerNumber = 0; // Set customerNumber to an appropriate default value
			distributionChannel = distributionChannelParam;
		} else {
			customerNumber = 0; // Set customerNumber to an appropriate default value
			distributionChannel = "";
		}

		// Retrieve the invoice record based on the customerNumber and
		// distributionChannel
		InvoiceDaoImpl invoiceDaoImpl = new InvoiceDaoImpl();
		List<InvoiceData> invoices = invoiceDaoImpl.getAllInvoicesForAnalyticalView(customerNumber, distributionChannel);

		// Create Gson instance
		Gson gson = new GsonBuilder().create();

		// Convert the data to JSON format
		String result = gson.toJson(invoices);
//
//		// Set the response type to JSON
		response.setContentType("application/json");

		// Send the JSON response back to the client
		PrintWriter out = response.getWriter();
		out.print(result);
		out.flush();

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
