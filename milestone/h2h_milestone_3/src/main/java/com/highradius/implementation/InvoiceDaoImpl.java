package com.highradius.implementation;

import com.highradius.model.InvoiceData;
import com.highradius.connection.DatabaseConnection;
import com.highradius.model.Invoice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.highradius.model.Invoice;

import java.util.ArrayList;
import java.util.List;

public class InvoiceDaoImpl implements InvoiceDao {
	private DatabaseConnection databaseConnection;

	public InvoiceDaoImpl() {
		databaseConnection = new DatabaseConnection();
	}

	@Override
	public List<Invoice> getInvoice() {
		return databaseConnection.getInvoices();
	}

	@Override
	public void insertInvoice(Invoice invoice) {
		databaseConnection.addInvoice(invoice);
	}

	@Override
	public void updateInvoice(int slNo, Invoice invoice) {
		// Implement update logic here

		try (Connection connection = DatabaseConnection.getConn()) {

			PreparedStatement statement = connection.prepareStatement("UPDATE H2H_OAP SET CUSTOMER_ORDER_ID=?, "
					+ "SALES_ORG=?, DISTRIBUTION_CHANNEL=?, DIVISION=?, RELEASED_CREDIT_VALUE=?, "
					+ "PURCHASE_ORDER_TYPE=?, COMPANY_CODE=?, ORDER_CREATION_DATE=?, ORDER_CREATION_TIME=?, "
					+ "CREDIT_CONTROL_AREA=?, SOLD_TO_PARTY=?, ORDER_AMOUNT=?, REQUESTED_DELIVERY_DATE=?, "
					+ "ORDER_CURRENCY=?, CREDIT_STATUS=?, CUSTOMER_NUMBER=?, AMOUNT_IN_USD=?, "
					+ "UNIQUE_CUST_ID=? WHERE SL_no=?");

			// Set the parameter values in the prepared statement
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
			statement.setInt(19, slNo);

			// Execute the update query
			statement.executeUpdate();

			// Print a success message
			System.out.println("Invoice with slNo " + slNo + " has been updated successfully.");

		} catch (SQLException e) {
			e.printStackTrace();

		}

	}

	@Override
	public boolean deleteInvoice(int slNo) {
		try (Connection connection = DatabaseConnection.getConn()) {
			String query = "DELETE FROM h2h_oap WHERE sl_no = ?";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, slNo);

			int rowsDeleted = statement.executeUpdate();

			return rowsDeleted > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Invoice getInvoiceBySlNo(int slNo) {

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Invoice invoice = null;

		try {
			// Get a database connection
			connection = DatabaseConnection.getConn();

			// Prepare the SQL statement
			String sql = "SELECT * FROM h2h_oap WHERE Sl_no = ?";
			statement = connection.prepareStatement(sql);
			statement.setInt(1, slNo);

			// Execute the query
			resultSet = statement.executeQuery();
			// Create an Invoice object with the retrieved data

			// If a record is found, create an Invoice object
			if (resultSet.next()) {

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
				invoice = new Invoice(slNo, customerOrderID, salesOrg, distributionChannel, division,
						releasedCreditValue, purchaseOrderType, companyCode, orderCreationDate, orderCreationTime,
						creditControlArea, soldToParty, orderAmount, requestedDeliveryDate, orderCurrency, creditStatus,
						customerNumber, amountInUsd, uniqueCustNumber);
				return invoice;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// Close the database resources
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return invoice;
	}

	@Override
	public Invoice getInvoiceByCustomerOrderId(int customerOrderID) {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Invoice invoice = null;

		try {
			// Get a database connection
			connection = DatabaseConnection.getConn();

			// Prepare the SQL statement
			String sql = "SELECT * FROM h2h_oap WHERE CUSTOMER_ORDER_ID = ?";
			statement = connection.prepareStatement(sql);
			statement.setInt(1, customerOrderID);

			// Execute the query
			resultSet = statement.executeQuery();
			// Create an Invoice object with the retrieved data

			// If a record is found, create an Invoice object
			if (resultSet.next()) {

				int slNo = resultSet.getInt("Sl_no");
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
				invoice = new Invoice(slNo, customerOrderID, salesOrg, distributionChannel, division,
						releasedCreditValue, purchaseOrderType, companyCode, orderCreationDate, orderCreationTime,
						creditControlArea, soldToParty, orderAmount, requestedDeliveryDate, orderCurrency, creditStatus,
						customerNumber, amountInUsd, uniqueCustNumber);
				return invoice;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// Close the database resources
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return invoice;
	}

	@Override
	public Invoice getInvoiceByAdvanceSearch(int customerOrderID, int customerNumber, int salesOrg) {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Invoice invoice = null;

		try {
			// Get a database connection
			connection = DatabaseConnection.getConn();

			// Prepare the SQL statement
			StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM h2h_oap");
			if (customerOrderID != 0) {
				sqlBuilder.append(" WHERE CUSTOMER_ORDER_ID = ?");
			} else if (customerNumber != 0) {
				sqlBuilder.append(" WHERE CUSTOMER_NUMBER = ?");
			} else if (salesOrg != 0) {
				sqlBuilder.append(" WHERE SALES_ORG = ?");
			}

			String sql = sqlBuilder.toString();
			statement = connection.prepareStatement(sql);

			// Set parameter values based on the search criteria
			if (customerOrderID != 0) {
				statement.setInt(1, customerOrderID);
			} else if (customerNumber != 0) {
				statement.setInt(1, customerNumber);
			} else if (salesOrg != 0) {
				statement.setInt(1, salesOrg);
			}

			// Execute the query
			resultSet = statement.executeQuery();

			// If a record is found, create an Invoice object
			if (resultSet.next()) {
				int slNo = resultSet.getInt("Sl_no");
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
				float amountInUsd = resultSet.getFloat("AMOUNT_IN_USD");
				long uniqueCustNumber = resultSet.getLong("UNIQUE_CUST_ID");
				int ccustomerOrderID = resultSet.getInt("CUSTOMER_ORDER_ID");
				int ssalesOrg = resultSet.getInt("SALES_ORG");
				int ccustomerNumber = resultSet.getInt("CUSTOMER_NUMBER");

				// Create an Invoice object with the retrieved data
				invoice = new Invoice(slNo, ccustomerOrderID, ssalesOrg, distributionChannel, division,
						releasedCreditValue, purchaseOrderType, companyCode, orderCreationDate, orderCreationTime,
						creditControlArea, soldToParty, orderAmount, requestedDeliveryDate, orderCurrency, creditStatus,
						ccustomerNumber, amountInUsd, uniqueCustNumber);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// Close the database resources
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return invoice;
	}

	@Override
	public List<InvoiceData> getAllInvoicesForAnalyticalView(int customerNumber, String distributionChannel) {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		List<InvoiceData> invoiceData = new ArrayList<>();

		try {
			// Get a database connection
			connection = DatabaseConnection.getConn();

			// Prepare the SQL statement
			String sql;
			if (customerNumber != 0 && !distributionChannel.isEmpty()) {
				sql = "SELECT DISTRIBUTION_CHANNEL, SUM(ORDER_AMOUNT) AS totalAmount " + "FROM h2h_oap "
						+ "WHERE CUSTOMER_NUMBER = ? AND DISTRIBUTION_CHANNEL = ? " + "GROUP BY DISTRIBUTION_CHANNEL";
				statement = connection.prepareStatement(sql);
				statement.setInt(1, customerNumber);
				statement.setString(2, distributionChannel);
			} else if (customerNumber != 0) {
				sql = "SELECT ORDER_AMOUNT, COUNT(CUSTOMER_NUMBER) AS customerCount " + "FROM h2h_oap "
						+ "WHERE CUSTOMER_NUMBER = ? " + "GROUP BY ORDER_AMOUNT";
				statement = connection.prepareStatement(sql);
				statement.setInt(1, customerNumber);
			} else if (!distributionChannel.isEmpty()) {
				sql = "SELECT DISTRIBUTION_CHANNEL, SUM(ORDER_AMOUNT) AS totalAmount " + "FROM h2h_oap "
						+ "WHERE DISTRIBUTION_CHANNEL = ? " + "GROUP BY DISTRIBUTION_CHANNEL";
				statement = connection.prepareStatement(sql);
				statement.setString(1, distributionChannel);
			} else {
				sql = "SELECT DISTRIBUTION_CHANNEL, SUM(ORDER_AMOUNT) AS totalAmount " + "FROM h2h_oap "
						+ "GROUP BY DISTRIBUTION_CHANNEL";
				statement = connection.prepareStatement(sql);
			}

			// Execute the query
			resultSet = statement.executeQuery();

			// Process the result set

			if (customerNumber != 0 && distributionChannel.isEmpty()) {
				while (resultSet.next()) {
					String invoiceDistributionChannel = resultSet.getString("customerCount");
					double invoiceTotalAmount = resultSet.getDouble("ORDER_AMOUNT");

					// Create an InvoiceData object and add the data
					InvoiceData data = new InvoiceData(invoiceTotalAmount, invoiceDistributionChannel);
					invoiceData.add(data);
				}
			} else {
				while (resultSet.next()) {
					String invoiceDistributionChannel = resultSet.getString("DISTRIBUTION_CHANNEL");
					double invoiceTotalAmount = resultSet.getDouble("totalAmount");

					// Create an InvoiceData object and add the data
					InvoiceData data = new InvoiceData(invoiceDistributionChannel, invoiceTotalAmount);
					invoiceData.add(data);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// Close the database resources
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		// Convert the array to JSON format

		return invoiceData;
	}

	@Override
	public Invoice setInvoiceBySlNo(int slNo, float orderAmount, float amountInUsd) {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Invoice updatedInvoice = null;

		try {
			// Get a database connection
			connection = DatabaseConnection.getConn();

			// Prepare the SQL statement to update the order amount and amount in USD
			String sql = "UPDATE h2h_oap SET ORDER_AMOUNT = ?, AMOUNT_IN_USD = ? WHERE Sl_no = ?";
			statement = connection.prepareStatement(sql);
			statement.setFloat(1, orderAmount);
			statement.setFloat(2, amountInUsd);
			statement.setInt(3, slNo);

			// Execute the update
			int rowsAffected = statement.executeUpdate();
			statement.close();

			if (rowsAffected > 0) {
				// If the update was successful, retrieve the updated invoice data
				String selectSql = "SELECT * FROM h2h_oap WHERE Sl_no = ?";
				statement = connection.prepareStatement(selectSql);
				statement.setInt(1, slNo);

				resultSet = statement.executeQuery();

				if (resultSet.next()) {
					// Retrieve the updated invoice data
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
					String requestedDeliveryDate = resultSet.getString("REQUESTED_DELIVERY_DATE");
					String orderCurrency = resultSet.getString("ORDER_CURRENCY");
					String creditStatus = resultSet.getString("CREDIT_STATUS");
					int customerNumber = resultSet.getInt("CUSTOMER_NUMBER");
					long uniqueCustNumber = resultSet.getLong("UNIQUE_CUST_ID");

					// Create an Invoice object with the updated data
					updatedInvoice = new Invoice(slNo, customerOrderID, salesOrg, distributionChannel, division,
							releasedCreditValue, purchaseOrderType, companyCode, orderCreationDate, orderCreationTime,
							creditControlArea, soldToParty, orderAmount, requestedDeliveryDate, orderCurrency,
							creditStatus, customerNumber, amountInUsd, uniqueCustNumber);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// Close the database resources
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return updatedInvoice;
	}

}
