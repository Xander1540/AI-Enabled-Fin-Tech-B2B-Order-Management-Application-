package com.highradius.servlets;

import java.io.BufferedReader;
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
 * Servlet implementation class SetOrderAndAmount
 */
@WebServlet("/SetOrderAndAmount")
public class SetOrderAndAmount extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SetOrderAndAmount() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Retrieve the "slNo" parameter from the request
		response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
		response.setHeader("Access-Control-Allow-Methods", "POST, OPTIONS");
		response.setHeader("Access-Control-Allow-Headers", "Content-Type");
		int slNo = Integer.parseInt(request.getParameter("slNo"));
		float orderAmount = Float.parseFloat(request.getParameter("orderAmount"));
		float amountInUsd = Float.parseFloat(request.getParameter("amountInUsd"));

		InvoiceDaoImpl invoiceDaoImpl = new InvoiceDaoImpl();
		Invoice invoice = invoiceDaoImpl.setInvoiceBySlNo(slNo, orderAmount, amountInUsd);

		Gson gson = new GsonBuilder().create();

		// Convert the data to JSON format
		String responses = gson.toJson("Invoice has been Updated successfully");

		response.setContentType("application/json");

		// Send the JSON response back to the client
		PrintWriter out = response.getWriter();
		out.print(responses);
		out.flush();

	}

}
