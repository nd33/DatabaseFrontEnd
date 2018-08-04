package com.prac.server;

import java.sql.*;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.ibm.icu.text.DateFormat;
import com.ibm.icu.text.SimpleDateFormat;
import com.prac.client.DBConnection;


/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class MySQLConnection extends RemoteServiceServlet implements DBConnection{
	//set connection parameters
    private Connection conn = null;
//    private String status;
    private String dburl  = "jdbc:mariadb://nd33.host.cs.st-andrews.ac.uk/nd33_cs3101_db";
    // change username
    private String uname = "nd33";
    // change password
    private String password = "********";
    //make connection
    public MySQLConnection() {
            try {
            		Class.forName("org.mariadb.jdbc.Driver");
                    conn = DriverManager.getConnection(dburl, uname, password);
            } catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    }
    //synchronous database access method to get all books from db
    public List<List<String>> getAllBooks() {
    	ResultSet rs = null;
    	ResultSetMetaData books_md = null;
    	int num_of_cols = 0;
    	List<List<String>> books = new ArrayList<List<String>>();
		try {
			//create statement
			String getAllBooks = "SELECT * FROM audio_book";		
			//query database
			Statement getALL = conn.createStatement();
			rs = getALL.executeQuery(getAllBooks);
			books_md = rs.getMetaData();
			num_of_cols = books_md.getColumnCount();
			
			// add meta data for first row
			List<String> book_metadata = new ArrayList<String>();
			for(int i = 1 ; i <= num_of_cols ; i++){
				book_metadata.add(books_md.getColumnName(i));
			}
			books.add(book_metadata);		
			
			while(rs.next()){
				List<String> book = new ArrayList<String>();
				for(int i = 1 ; i <= num_of_cols ; i++){
					String col_data = rs.getString(i);
					book.add(col_data);
				}
				books.add(book);
			}
			//close utilities
			rs.close();
			getALL.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}				
		return  books;
    }
    //synchronous database access method to get all books by author from db
	public List<List<String>> getBooksByAuthor(String author) {
    	ResultSet rs = null;
    	ResultSetMetaData books_md = null;
    	int num_of_cols = 0;
    	List<List<String>> books = new ArrayList<List<String>>();
		try {
			//prepare statement	
			//SELECT * FROM audio_book WHERE audio_book.ISBN in (SELECT ISBN FROM wrote natural join author WHERE author.name = (?));
			PreparedStatement getBooksByAuth = conn.prepareStatement("SELECT * "
					+ "FROM audio_book "
					+ "WHERE audio_book.ISBN in "
					+ "(SELECT ISBN FROM wrote natural join author WHERE author.name = (?))");
			getBooksByAuth.setString(1, author);
			//query database
			rs = getBooksByAuth.executeQuery();
			books_md = rs.getMetaData();
			num_of_cols = books_md.getColumnCount();
			
			// add meta data for first row
			List<String> book_metadata = new ArrayList<String>();
			for(int i = 1 ; i <= num_of_cols ; i++){
				book_metadata.add(books_md.getColumnName(i));
			}
			books.add(book_metadata);		
			
			while(rs.next()){
				List<String> book = new ArrayList<String>();
				for(int i = 1 ; i <= num_of_cols ; i++){
					String col_data = rs.getString(i);
					book.add(col_data);
				}
				books.add(book);
			}
			//close utilities
			rs.close();
			getBooksByAuth.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}				
		return  books;
	}
	
	public List<List<String>> getReviewsOfBook(String book_name) {
    	ResultSet rs = null;
    	ResultSetMetaData review_md = null;
    	int num_of_cols = 0;
    	List<List<String>> reviews = new ArrayList<List<String>>();
		try {
			//prepare statement	
			PreparedStatement getReviewsOfBook = conn.prepareStatement("SELECT review.ID as customer, review.rating, review.comment "
					+ "FROM audio_book natural join review WHERE title = (?);");
			getReviewsOfBook.setString(1, book_name);
			//query database
			rs = getReviewsOfBook.executeQuery();
			review_md = rs.getMetaData();
			num_of_cols = review_md.getColumnCount();
			
			// add meta data for first row
			List<String> review_metadata = new ArrayList<String>();
			for(int i = 1 ; i <= num_of_cols ; i++){
				review_metadata.add(review_md.getColumnName(i));
			}
			reviews.add(review_metadata);		
			
			while(rs.next()){
				List<String> review = new ArrayList<String>();
				for(int i = 1 ; i <= num_of_cols ; i++){
					String col_data = rs.getString(i);
					review.add(col_data);
				}
				reviews.add(review);
			}
			//close utilities
			rs.close();
			getReviewsOfBook.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}				
		return  reviews;
	}
	
	public List<List<String>> getPopBooks() {
    	ResultSet rs = null;
    	ResultSetMetaData books_md = null;
    	int num_of_cols = 0;
    	List<List<String>> books = new ArrayList<List<String>>();
		try {
			//create statement
			String getAllBooks = "SELECT audio_book.title as pop_books, count(*) as num_of_purchases "
					+ "FROM audio_book, purchase "
					+ "WHERE purchase.ISBN = audio_book.ISBN "
					+ "group by audio_book.title "
					+ "order by num_of_purchases desc;";		
			//query database
			Statement getPopBooks = conn.createStatement();
			rs = getPopBooks.executeQuery(getAllBooks);
			books_md = rs.getMetaData();
			num_of_cols = books_md.getColumnCount();
			
			// add meta data for first row
			List<String> book_metadata = new ArrayList<String>();
			for(int i = 1 ; i <= num_of_cols ; i++){
				book_metadata.add(books_md.getColumnName(i));
			}
			books.add(book_metadata);		
			
			while(rs.next()){
				List<String> book = new ArrayList<String>();
				for(int i = 1 ; i <= num_of_cols ; i++){
					String col_data = rs.getString(i);
					book.add(col_data);
				}
				books.add(book);
			}
			//close utilities
			rs.close();
			getPopBooks.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}				
		return  books;
	}
	public String purchBook(String ISBN, String usr_id) {
		String result = "Purchase success !";
		try {
			//create statement
			PreparedStatement purchaseBook = conn.prepareStatement("insert into purchase values (?,?,?)");
			// fixed user to by book achieve basic functionality
			purchaseBook.setString(1, usr_id);
			purchaseBook.setString(2, ISBN);
			purchaseBook.setString(3, LocalDateTime.now().toString());
			purchaseBook.executeQuery();
			//close utilities
			purchaseBook.close();
		} catch (SQLException e) {
			result = "Purchase failed !";
			e.printStackTrace();
		}				
		return result;
	}
	
	
	
	
	
	
	
	//extended functionality
	
	// retrieve author names for ListBox
    public List<String> getAuthors() {
    	ResultSet rs = null;
    	List<String> authors = new ArrayList<String>();
		try {
			//create statement
			String getAthQ = "SELECT name FROM author";		
			//query database
			Statement getAuthors = conn.createStatement();
			rs = getAuthors.executeQuery(getAthQ);
			
			while(rs.next()){
				authors.add(rs.getNString("name"));
			}	
			//close utilities
			getAuthors.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}				
		return  authors;
    }
	@Override
	public String authenticate(String ID, String password) {
		String isAuthenticated = "no";
		try {
			//create statement
			PreparedStatement checkDetails = conn.prepareStatement("Select * FROM Customer where ID = (?) && password = (?);");
			checkDetails.setString(1, ID);
			checkDetails.setString(2, password);
			ResultSet rs = checkDetails.executeQuery();
			while(rs.next())
				isAuthenticated = "yes";
			//close utilities
			checkDetails.close();
		} catch (SQLException e) {
			isAuthenticated = "no";
			e.printStackTrace();
		}				
		return isAuthenticated;
	}
	@Override
	public List<String> register(String ID, String password, String name, String date_of_birth, String email, String phone) {
		List<String> details = new ArrayList<String>();
		String ID_check = "yes";
		String password_check = "yes";
		String name_check = "yes";
		String date_of_birth_check = "yes";
		String email_check = "yes";
		String phone_check = "yes";
		details.add(ID_check);
		details.add(password_check);
		details.add(name_check);
		details.add(date_of_birth_check);
		details.add(email_check);
		details.add(phone_check);
		
		//start information validation for registration
		//check ID
		if(!(ID.matches("[0-9]+")))
			details.set(0, "ID must only contain digits");
		if(ID.length() != 10)
			details.set(0, "ID must contain exactly 10 digits");
		//check password
		if(password.length() < 6 || password.length() > 12)
			details.set(1, "Password must be between 6 and 12 characters");
		//check name
		if(name.length() < 1 || name.length() > 50)
			details.set(2, "Name must be between 1 and 50 characters");
		//check date
		DateFormat df = new SimpleDateFormat("yyyy-mm-dd");
		try {
			df.format(df.parse(date_of_birth));
		} catch (ParseException e1) {
			details.set(3, "Not valid date format try yyyy-mm-dd");
			e1.printStackTrace();
		}     
		//check email
		Pattern email_pattern = Pattern.compile("^[A-Za-z0-9-]+(\\-[A-Za-z0-9])*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9])");
		Matcher email_match = email_pattern.matcher(email);
		if(email_match.find() == false)
			details.set(4, "Invalid email try sth like example@abv.bg");
		
		//check phone
		if(!(phone.matches("[0-9]+")))
			details.set(5, "phone must contain only digits");
		if(phone.length() > 20)
			details.set(5, "Phone must contain up to 20 digits");
			//check if ID, email and phone are free
			try {
				PreparedStatement checkCust = conn.prepareStatement("Select ID,email from Customer where ID = (?) || email = (?);");
				checkCust.setString(1, ID);
				checkCust.setString(2, email);
				ResultSet checkRS = checkCust.executeQuery();
				while(checkRS.next()){
					if(checkRS.getNString("ID").equalsIgnoreCase(ID))
						details.set(0, "User ID already exists !");
					if(checkRS.getNString("email").equalsIgnoreCase(ID))
						details.set(4, "Email already taken !");
				}
			} catch (SQLException e1) {
				details.set(3, "Serious Email or ID problem");
				e1.printStackTrace();
			}
			try {
				PreparedStatement checkPhone = conn.prepareStatement("Select * from customer_phone where phone_number = ? ;");
				checkPhone.setString(1, phone);
				ResultSet phoneCheck = checkPhone.executeQuery();
				while(phoneCheck.next()){
						details.set(5, "Phone number is already in use");
				}
			} catch (SQLException e1) {
				details.set(3, "Serious problem with phone");
				e1.printStackTrace();
			}
			//return details to client if info not valid
				for(String str : details)
					if(str != "yes")
						return details;
			
				//create statement
				PreparedStatement registerCustomer;
				try {
					//will be adding interleaved data to to tables set autocommit off
					conn.setAutoCommit(false);
					registerCustomer = conn.prepareStatement("insert into Customer values (?,?,?,?,?)");
					registerCustomer.setString(1, ID);
					registerCustomer.setString(2, password);
					registerCustomer.setString(3, name);
					registerCustomer.setString(4, date_of_birth);
					registerCustomer.setString(5, email);
					ResultSet rs = registerCustomer.executeQuery();
					//close utilities
				} catch (SQLException e1) {
					details.set(3, "Serious problem with customer storage");
					e1.printStackTrace();
				}
				
		try{
			PreparedStatement regPhone = conn.prepareStatement("insert into customer_phone values (?,?)");
			regPhone.setString(1, ID);
			regPhone.setString(2, phone);
			ResultSet rs1 = regPhone.executeQuery();
			//commit if both queries succeed
			conn.commit();
			conn.setAutoCommit(true);
				
		}catch (SQLException e) {
			// just choose random trigger to inform of problem
			details.set(3, "Serious problem with phone number storage");
			e.printStackTrace();
		}				
		return details;
	}
}
