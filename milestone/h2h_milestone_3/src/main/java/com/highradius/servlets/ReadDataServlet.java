package com.highradius.servlets;

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
 * Servlet implementation class ReadDataServlet
 */
@WebServlet("/ReadDataServlet")
public class ReadDataServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ReadDataServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Set CORS headers
		response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
		response.setHeader("Access-Control-Allow-Methods", "GET");
		response.setHeader("Access-Control-Allow-Headers", "Content-Type");

		InvoiceDaoImpl invoiceDaoImpl = new InvoiceDaoImpl();
		System.out.println("Data loading Servlet Demo");
		List<Invoice> invoices = invoiceDaoImpl.getInvoice();

		// Create Gson instance
		Gson gson = new GsonBuilder().create();

		// Convert the data to JSON format
		String jsonInvoices = gson.toJson(invoices);

		// Set the response type to JSON
		response.setContentType("application/json");

		// Send the JSON response back to the client
		PrintWriter out = response.getWriter();
		out.print(jsonInvoices);
		out.flush();
	}

}
