package edu.shop.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;
import java.util.Properties;

import org.postgresql.Driver;

import edu.sgop.view.View;
import edu.shop.model.Product;
import edu.shop.model.Shop;

public class Controller {
	static Connection connection = null;
	static String url = "jdbc:postgresql://localhost:5432/shop";
	static {
		try {
			Driver pgDriver = new Driver();
			DriverManager.registerDriver(pgDriver);

			FileInputStream FIS = new FileInputStream("dbConfig.properties");
			Properties properties = new Properties();
			properties.load(FIS);
			connection = DriverManager.getConnection(url, properties);

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int addShop(Shop shop) {
		try {
			//
			PreparedStatement shopDetails = connection.prepareStatement("INSERT INTO shop VALUES (?,?,?,?,?,?)");
			shopDetails.setInt(1, shop.getId());
			shopDetails.setString(2, shop.getShopName());
			shopDetails.setString(3, shop.getOwnerName());
			shopDetails.setString(4, shop.getAddress());
			shopDetails.setLong(5, shop.getContact());
			shopDetails.setString(6, shop.getGST());
			return shopDetails.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public Shop isShopExist() { // method to check shop exit or not
		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM shop");
			Shop isShopExist = new Shop();
			while (resultSet.next()) {
				isShopExist.setId(resultSet.getInt(1));
				isShopExist.setShopName(resultSet.getString(2));
				isShopExist.setOwnerName(resultSet.getString(3));
				isShopExist.setAddress(resultSet.getString(4));
				isShopExist.setContact(resultSet.getLong(5));
				isShopExist.setGST(resultSet.getString(6));
			}
			return isShopExist;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void addProduct(Shop shop, List<Product> products) {// method to add product in product list in shop
		for (Product product : products) {
			try {
				// Insert Product details in product table
				PreparedStatement productStatement = connection
						.prepareStatement("INSERT INTO product VALUES (?,?,?,?,?)");
				productStatement.setInt(1, product.getProductId());
				productStatement.setString(2, product.getProductName());
				productStatement.setDouble(3, product.getProductPrice());
				productStatement.setInt(4, product.getQuantity());
				productStatement.setBoolean(5, product.isAvailability());
				productStatement.executeUpdate();
				// Insert shop id and product id in shop_product table
				PreparedStatement shopProductStatement = connection
						.prepareStatement("INSERT INTO shop_product VALUES(?,?)");
				shopProductStatement.setInt(1, shop.getId());
				shopProductStatement.setInt(2, product.getProductId());
				shopProductStatement.executeUpdate();

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public ResultSet fetchAllProduct() {
		try {
			Statement statement = connection.createStatement();
			return checkProduct(statement.executeQuery("SELECT * FROM product"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ResultSet checkProduct(ResultSet resultSet) {
		try {
			Statement statement = connection.createStatement();
			byte count = 0;
			while (resultSet.next()) {
				if (++count > 0) {
					break;
				}
			}
			if (count == 1) {
				return statement.executeQuery("SELECT * FROM product;");
			} else {
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Product fetchParticularProduct(int id) {
		try {
			PreparedStatement prepareStatement = connection.prepareStatement("SELECT * FROM product WHERE product_id=?;");
			prepareStatement.setInt(1, id);
			ResultSet productResultSet = checkProduct(prepareStatement.executeQuery());
			// return that particular product
			Product product = new Product();
			while (productResultSet.next()) {
				if (productResultSet.getInt(1)==id) {
					product.setProductId(productResultSet.getInt(1));
					product.setProductName(productResultSet.getString(2));
					product.setProductPrice(productResultSet.getDouble(3));
					product.setQuantity(productResultSet.getInt(4));
					product.setAvailability(productResultSet.getBoolean(5));
				}
				
			}
			return product;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public int updateProduct(Product product) {
		try {
			PreparedStatement prepareStatement = connection.prepareStatement("UPDATE product SET product_name=?, product_price=?, product_quantity=?, product_availibility =? WHERE product_id=? ;");
			prepareStatement.setString(1, product.getProductName());
			prepareStatement.setDouble(2, product.getProductPrice());
			prepareStatement.setInt(3, product.getQuantity());
			prepareStatement.setBoolean(4, product.isAvailability());
			prepareStatement.setInt(5, product.getProductId());
		
			prepareStatement.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public int removeProduct(int productId) {
		try {
			CallableStatement removeProduct = connection.prepareCall("call remove_product(?,?,?)");
			removeProduct.registerOutParameter(1, Types.INTEGER);
			removeProduct.setInt(2, productId);
			removeProduct.registerOutParameter(3, Types.INTEGER);
			//
			removeProduct.executeUpdate();
			int before = removeProduct.getInt(1);
			int after = removeProduct.getInt(3);
			if (after < before) {
				return 2;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	

	public void closeConnection() {// method to close connection
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}


}
