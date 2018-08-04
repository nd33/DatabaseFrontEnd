package com.prac.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
// methods for synchronous callback (database access on server side)
public interface DBConnection extends RemoteService {
	// basic functionality
	// access db to give client list of all books in database
	public List<List<String>> getAllBooks();
	// access db to give client list of all books by Author in db
	public List<List<String>> getBooksByAuthor(String author);
	// access db to give client 2D list of all reviews of a particular book in db
	public List<List<String>> getReviewsOfBook(String book_name);
	// access db to give client 2D list of books which have been purchased
	public List<List<String>> getPopBooks();
	// access db to record new purchase
	public String purchBook(String ISBN, String usr_id);
	
	
	
	
	//extended functionality
	public List<String> getAuthors();
	// authenticate user
	public String authenticate(String ID, String password);	
	// register user
	public List<String> register(String ID, String password, String name, String date_of_birth, String email, String phone);
}
