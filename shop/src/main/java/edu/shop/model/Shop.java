package edu.shop.model;

import java.util.List;

public class Shop {
	
	private int id;
	private String shopName;
	private String ownerName;
	private String address;
	private long contact;
	private String GST;
	
	private List<Product> Products;
	
	public List<Product> getProducts() {
		return Products;
	}
	public void setProducts(List<Product> products) {
		Products = products;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public String getOwnerName() {
		return ownerName;
	}
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public long getContact() {
		return contact;
	}
	public void setContact(long contact) {
		this.contact = contact;
	}
	public String getGST() {
		return GST;
	}
	public void setGST(String gST) {
		GST = gST;
	}
//	@Override
//	public String toString() {
//		return "Shop [id=" + id + ", shopName=" + shopName + ", ownerName=" + ownerName + ", address=" + address
//				+ ", contact=" + contact + ", GST=" + GST + "]";
//	}
	
	
	
	
}
