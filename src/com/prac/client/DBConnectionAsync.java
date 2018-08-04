package com.prac.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

//methods for asynchronous callback (result from database on client side)
public interface DBConnectionAsync {
	// receive 2D list of all books in db
    public void getAllBooks(AsyncCallback<List<List<String>>> callback);
	// receive 2D list of all books by Author in db
	public void getBooksByAuthor(String author, AsyncCallback<List<List<String>>> callback); 
	// receive 2D list of all reviews of a book
	public void getReviewsOfBook(String book_name, AsyncCallback<List<List<String>>> callback);
	// receive 2D list of pop books
	public void getPopBooks(AsyncCallback<List<List<String>>> callback);
	// access db to record new purchase
	public void purchBook(String ISBN, String usr_id, AsyncCallback<String> callback);
	
	
	
	
	
	
	
	
	
	
	//extended functionality
	// get authors for ListBox
	public void getAuthors(AsyncCallback<List<String>> callback);
	// authenticate user
	public void authenticate(String ID, String password, AsyncCallback<String> callback);
	// register user
	public void register(String ID, String password, String name, String date_of_birth
			, String email, String phone, AsyncCallback<List<String>> callback);
}
