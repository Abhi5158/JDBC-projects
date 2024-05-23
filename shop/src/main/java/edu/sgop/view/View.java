package edu.sgop.view;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import edu.shop.controller.Controller;
import edu.shop.model.Product;
import edu.shop.model.Shop;

public class View {
	// why static because non-static object loads during object creation
	// thats why we use static Scanner to
	static Scanner myInput = new Scanner(System.in);
	static Controller controller = new Controller();
	static Shop shop = new Shop();
	private static List<Product> products = new ArrayList<Product>();

	public static List<Product> getProducts() {
		return products;
	}

	public static void setProducts(List<Product> products) {
		View.products = products;
	}

	static {
//		Ask shop details if shop does not Exits
//		If shop exits then show shop details
		Shop shopExist = controller.isShopExist();
		if (shopExist.getId() != 0) {
			shop = shopExist;
			System.out.println("-----Welcome Back to Shop-----");
			System.out.println("Shop Details : ");
			System.out.println("Shop Id : " + shop.getId());
			System.out.println("Shop Name : " + shop.getShopName());
			System.out.println("Shop Owner Name : " + shop.getOwnerName());
			System.out.println("Shop Address : " + shop.getAddress());
			System.out.println("Shop Contact : " + shop.getContact());
			System.out.println("Shop GST : " + shop.getGST());
		} else {
			System.out.println("-----Welcome to Shop-----");
			System.out.print("Enter id : ");
			shop.setId(myInput.nextInt());
			myInput.nextLine();
			System.out.print("Enter Shop Name : ");
			shop.setShopName(myInput.nextLine());
			System.out.print("Enter Owner Name : ");
			shop.setOwnerName(myInput.nextLine());
			System.out.print("Enter Shop Address : ");
			shop.setAddress(myInput.nextLine());
			System.out.print("Enter Contact Number : ");
			shop.setContact(myInput.nextLong());
			myInput.nextLine();
			System.out.print("Enter GST Number : ");
			shop.setGST(myInput.nextLine());
			if (controller.addShop(shop) != 0) {
				System.out.println("Shop Added\n");
			}
		}
	}

	public static void main(String[] args) {

		do {
			System.out.println("Select Operation to Perform");
			System.out.println("1. Add Product's\n2. View Product's \n3. Update Product \n4. Remove Product's\n0. Exit");
			System.out.print("Enter Digit Respective to Desired Option : ");
			byte userChoise = myInput.nextByte();

			switch (userChoise) {
			case 1:// 1.Add Product

				boolean contiuneToAdd = true;
				do {
					Product product = new Product();
					System.out.print("Enter Product Id :");
					product.setProductId(myInput.nextInt());
					myInput.nextLine();
					System.out.print("Enter Product Name :");
					product.setProductName(myInput.nextLine());
					System.out.print("Enter Product Price :");
					product.setProductPrice(myInput.nextDouble());
					myInput.nextLine();
					System.out.print("Enter Product Quantity :");
					int quantity = myInput.nextInt();
					myInput.nextLine();
					product.setQuantity(quantity);
					if (quantity > 0) {
						product.setAvailability(true);
					} else {
						product.setAvailability(false);
					}
					products.add(product);
					System.out.print("Continue to Add Product ? y/n : ");
					String continueto = myInput.nextLine();
					if (continueto.equalsIgnoreCase("n")) {
						contiuneToAdd = false;
					}
				} while (contiuneToAdd);
				controller.addProduct(shop, products);
				break;
				
			case 2:
				System.out.println("List of Products Present Inside Shop :");
				ResultSet listOfProduct = controller.fetchAllProduct();
				if (listOfProduct == null) {
					System.out.println("No Product Exist, Inside Shop");
				} else {
					System.out.printf("| %-5s | %-15s | %-15s | %-17s | %-20s |%n", "Id", "Product Name",
							"Product Price", "Product Quantity", "Product Availibility");
					try {
						while (listOfProduct.next()) {
							System.out.printf("| %-5d ", listOfProduct.getInt(1));
							System.out.printf("| %-15s ", listOfProduct.getString(2));
							System.out.printf("| %-15f ", listOfProduct.getDouble(3));
							System.out.printf("| %-17d ", listOfProduct.getInt(4));
							System.out.printf("| %-20b |", listOfProduct.getBoolean(5));
							System.out.println();
							
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				break;
			
			case 3:// 3. Update Product
				ResultSet fetchAllProduct = controller.fetchAllProduct();
				System.out.println("List of Available Product to Update : ");
				if (fetchAllProduct == null) {
					System.out.println("No Product Exist, No Update Operation can br Perform");
				} else {
					System.out.println("\nAvailable Products In Shop :");
					System.out.printf("| %-5s | %-15s | %-15s | %-17s | %-20s |%n", "Id", "Product Name",
							"Product Price", "Product Quantity", "Product Availibility");
					try {
						while (fetchAllProduct.next()) {
							System.out.printf("| %-5d ", fetchAllProduct.getInt(1));
							System.out.printf("| %-15s ", fetchAllProduct.getString(2));
							System.out.printf("| %-15f ", fetchAllProduct.getDouble(3));
							System.out.printf("| %-17d ", fetchAllProduct.getInt(4));
							System.out.printf("| %-20b |", fetchAllProduct.getBoolean(5));
							System.out.println();
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}

					System.out.print("Enter Product Id to Update Product : ");
					int id = myInput.nextInt();
					myInput.nextLine();
					// TODO : Check id is present or not
					Product product = controller.fetchParticularProduct(id);
					boolean update = true;
					do {
						System.out.println("1. Product name\n2. Product Price\n3. Product Quantity\n4. Product Avaliability\n5. Press to Update");
						System.out.print("Choise Option to Update : ");
						byte toUpdate = myInput.nextByte();
						myInput.nextLine();
						
						switch (toUpdate) {
						case 1:
							System.out.print("Enter new Product Name : ");
							product.setProductName(myInput.nextLine());
							break;
						case 2:
							System.out.print("Enter new Product Price : ");
							product.setProductPrice(myInput.nextDouble());
							myInput.nextLine();
							break;
						case 3:
							System.out.print("Enter new Quantity : ");
							product.setQuantity(myInput.nextInt());
							myInput.nextLine();
							break;
						case 4:
							System.out.print("Enter new Avilibility : ");
							product.setAvailability(myInput.nextBoolean());
							break;
						case 5:
							controller.updateProduct(product);
							update =false;
							break;
						
						default:
							System.out.println("----- Invalid Option -----");
							break;
						}
						
					} while (update);
//					controller.updateProduct(product);

				}

				break;
				
			case 4:// 2. Remove product
				ResultSet productsResultSet = controller.fetchAllProduct();
				if (productsResultSet == null) {
					System.out.println("No Product Exist, No Remove Operation can br Perform");
				} else {
					System.out.println("\nAvailable Products In Shop :");
					System.out.printf("| %-5s | %-15s | %-15s | %-17s | %-20s |%n", "Id", "Product Name",
							"Product Price", "Product Quantity", "Product Availibility");
					try {
						while (productsResultSet.next()) {
							System.out.printf("| %-5d ", productsResultSet.getInt(1));
							System.out.printf("| %-15s ", productsResultSet.getString(2));
							System.out.printf("| %-15f ", productsResultSet.getDouble(3));
							System.out.printf("| %-17d ", productsResultSet.getInt(4));
							System.out.printf("| %-20b |", productsResultSet.getBoolean(5));
							System.out.println();
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
					System.out.print("Enter Product Id to Remove :");
					int productId = myInput.nextInt();
					myInput.nextLine();
					int removeProduct = controller.removeProduct(productId);
					if (removeProduct == 2) {
						System.out.println("Product Removed");
					} else {
						System.out.println("Product Not Remove, Please Enter Valid Id");
					}
				}

				break;

			case 0:// 0.Exit
				System.out.println("----- E X I T -----");
				controller.closeConnection();
				System.exit(0);

				break;

			default:
				System.out.println("----- INVALID SELECTION -----");
				break;
			}
		} while (true);
	}

}
//System.out.print("Choise Option to Update : ");
//byte toUpdate = myInput.nextByte();
//myInput.nextLine();
//switch (toUpdate) {
//case 1:
//	System.out.print("Enter new Product Name : ");
//	product.setProductName(myInput.nextLine());
//	break;
//case 2:
//	System.out.print("Enter new Product Price : ");
//	product.setProductPrice(myInput.nextDouble());
//	myInput.nextLine();
//	break;
//case 3:
//	System.out.print("Enter new Quantity : ");
//	product.setQuantity(myInput.nextInt());
//	myInput.nextLine();
//	break;
//case 4:
//	System.out.print("Enter new Avilibility : ");
//	product.setAvailability(myInput.nextBoolean());
//	break;
//
//default:
//	System.out.println("----- Invalid Option -----");
//	break;
//}
