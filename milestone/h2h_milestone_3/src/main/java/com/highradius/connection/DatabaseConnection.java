package com.highradius.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.highradius.model.Invoice;

public class DatabaseConnection {
	private static final String URL = "jdbc:mysql://localhost:3306/h2h";
	private static final String USERNAME = "root";
	private static final String PASSWORD = "9918291126";
	private List<Invoice> invoices;

	public DatabaseConnection() {
		invoices = new ArrayList<>();
	}

	public static Connection getConn() throws SQLException {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return connection;
    }

	public List<Invoice> getInvoices() {

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		List<Invoice> invoices = new ArrayList<>();

		try {
			// Get a database connection
			connection = DatabaseConnection.getConn();

			// Create a SQL query
			String sql = "SELECT * from h2h_oap ; ";

			// Prepare the statement
			statement = connection.prepareStatement(sql);

			// Execute the query
			resultSet = statement.executeQuery();

			// Process the results
			while (resultSet.next()) {
				// Retrieve data from the current row
				int slNo = resultSet.getInt("Sl_no");
				int customerOrderID = resultSet.getInt("CUSTOMER_ORDER_ID");
				int salesOrg = resultSet.getInt("SALES_ORG");
				String distributionChannel = resultSet.getString("DISTRIBUTION_CHANNEL");
				String division = resultSet.getString("DIVISION");
				float releasedCreditValue = resultSet.getFloat("RELEASED_CREDIT_VALUE");
				String purchaseOrderType = resultSet.getString("PURCHASE_ORDER_TYPE");
				int companyCode = resultSet.getInt("COMPANY_CODE");
				String orderCreationDate = resultSet.getString("ORDER_CREATION_DATE");
				String orderCreationTime = resultSet.getString("ORDER_CREATION_TIME");
				String creditControlArea = resultSet.getString("CREDIT_CONTROL_AREA");
				int soldToParty = resultSet.getInt("SOLD_TO_PARTY");
				float orderAmount = resultSet.getFloat("ORDER_AMOUNT");
				String requestedDeliveryDate = resultSet.getString("REQUESTED_DELIVERY_DATE");
				String orderCurrency = resultSet.getString("ORDER_CURRENCY");
				String creditStatus = resultSet.getString("CREDIT_STATUS");
				int customerNumber = resultSet.getInt("CUSTOMER_NUMBER");
				float amountInUsd = resultSet.getFloat("AMOUNT_IN_USD");
				long uniqueCustNumber = resultSet.getLong("UNIQUE_CUST_ID");

				// Create an Invoice object with the retrieved data
				Invoice invoice = new Invoice(slNo, customerOrderID, salesOrg, distributionChannel, division,
						releasedCreditValue, purchaseOrderType, companyCode, orderCreationDate, orderCreationTime,
						creditControlArea, soldToParty, orderAmount, requestedDeliveryDate, orderCurrency, creditStatus,
						customerNumber, amountInUsd, uniqueCustNumber);

				// Add the invoice to the list
				invoices.add(invoice);
			}

//			// Print the retrieved invoices
//			for (Invoice invoice : invoices) {
//				System.out.println(invoice);
//			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// Close the resources
			try {
				if (resultSet != null)
					resultSet.close();
				if (statement != null)
					statement.close();
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return invoices;
	}

	public void addInvoice(Invoice invoice) {

		invoices.add(invoice);
		Connection connection = null;
		PreparedStatement statement = null;

		try {
			// Get a database connection
			connection = DatabaseConnection.getConn();

			// Create the SQL query to insert the invoice data
			String sql = "INSERT INTO h2h_oap (CUSTOMER_ORDER_ID, SALES_ORG, DISTRIBUTION_CHANNEL, DIVISION, RELEASED_CREDIT_VALUE, PURCHASE_ORDER_TYPE, COMPANY_CODE, ORDER_CREATION_DATE, ORDER_CREATION_TIME, CREDIT_CONTROL_AREA, SOLD_TO_PARTY, ORDER_AMOUNT, REQUESTED_DELIVERY_DATE, ORDER_CURRENCY, CREDIT_STATUS, CUSTOMER_NUMBER, AMOUNT_IN_USD, UNIQUE_CUST_ID) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

			// Prepare the statement
			statement = connection.prepareStatement(sql);

			// Set the parameter values for the SQL query

			statement.setInt(1, invoice.getCustomerOrderID());
			statement.setInt(2, invoice.getSalesOrg());
			statement.setString(3, invoice.getDistributionChannel());
			statement.setString(4, invoice.getDivision());
			statement.setFloat(5, invoice.getReleasedCreditValue());
			statement.setString(6, invoice.getPurchaseOrderType());
			statement.setInt(7, invoice.getCompanyCode());
			statement.setString(8, invoice.getOrderCreationDate());
			statement.setString(9, invoice.getOrderCreationTime());
			statement.setString(10, invoice.getCreditControlArea());
			statement.setInt(11, invoice.getSoldToParty());
			statement.setFloat(12, invoice.getOrderAmount());
			statement.setString(13, invoice.getRequestedDeliveryDate());
			statement.setString(14, invoice.getOrderCurrency());
			statement.setString(15, invoice.getCreditStatus());
			statement.setInt(16, invoice.getCustomerNumber());
			statement.setFloat(17, invoice.getAmountInUsd());
			statement.setLong(18, invoice.getUniqueCustNumber());

			// Execute the query to insert the invoice data
			int rowsAffected = statement.executeUpdate();

			if (rowsAffected > 0) {
				System.out.println("Invoice added successfully.");
			} else {
				System.out.println("Failed to add the invoice.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// Close the resources
			try {
				if (statement != null)
					statement.close();
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}

