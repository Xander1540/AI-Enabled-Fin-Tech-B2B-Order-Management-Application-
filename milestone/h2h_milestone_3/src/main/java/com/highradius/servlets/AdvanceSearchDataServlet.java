package com.highradius.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.highradius.implementation.InvoiceDaoImpl;
import com.highradius.model.Invoice;

/**
 * Servlet implementation class AdvanceSearchDataServlet
 */
@WebServlet("/AdvanceSearchDataServlet")
public class AdvanceSearchDataServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdvanceSearchDataServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// Retrieve the request parameters
		response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
		response.setHeader("Access-Control-Allow-Methods", "POST, OPTIONS");
		response.setHeader("Access-Control-Allow-Headers", "Content-Type");
		
		
		String customerOrderIDParam = request.getParameter("customerOrderID");
		String customerNumberParam = request.getParameter("customerNumber");
		String salesOrgParam = request.getParameter("salesOrg");

		// Validate and parse the parameters
		int customerOrderID = customerOrderIDParam != null ? Integer.parseInt(customerOrderIDParam) : 0;
		int customerNumber = customerNumberParam != null ? Integer.parseInt(customerNumberParam) : 0;
		int salesOrg = salesOrgParam != null ? Integer.parseInt(salesOrgParam) : 0;

		// Retrieve the invoice record based on the parameters
		InvoiceDaoImpl invoiceDaoImpl = new InvoiceDaoImpl();
		Invoice invoice = invoiceDaoImpl.getInvoiceByAdvanceSearch(customerOrderID, customerNumber, salesOrg);

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
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
