package com.prac.client;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PageOne extends Composite {
	private VerticalPanel vPanel = new VerticalPanel();
	private HorizontalPanel result_hPanel = new HorizontalPanel();
	private DBConnectionAsync rpc; // remote procedure call provider
	public String user_id;
	
	//components for db access
	public static ListBox authorName = new ListBox();
	public static TextBox audiobookTitle = new TextBox();
	public static TextBox book_to_purchase = new TextBox();

	public PageOne() {
		// ask GWT to return implementation of my DBConnection interface
		// MySQLConnection instance is stuffed into DBConnectionAsync var rpc
		// will act as remote procedure call provider
		rpc = (DBConnectionAsync) GWT.create(DBConnection.class);
		ServiceDefTarget target = (ServiceDefTarget) rpc;
		// The path 'MySQLConnection' is determined in
		// ./com.prac/practical_2_1.gwt.xml
		// This path should direct our server to listen for this context on the
		// server side,
		// thus intercepting the rpc requests. How to get our server ???
		String moduleRelativeURL = GWT.getModuleBaseURL() + "MySQLConnection";
		target.setServiceEntryPoint(moduleRelativeURL);
		initWidget(vPanel);

		HorizontalPanel hPanel1 = new HorizontalPanel();
		Label Lbl1 = new Label("View all audiobooks in the system");
		Button btn1 = new Button("View all books");
		btn1.addClickHandler(new GetAllBooksBTN());
		hPanel1.setBorderWidth(1);
		hPanel1.add(Lbl1);
		hPanel1.add(btn1);

		HorizontalPanel hPanel2 = new HorizontalPanel();
		Label Lbl2 = new Label("View all audiobooks written by each author");
//		authorName.getElement().setPropertyString("placeholder", "enter author name");
		AsyncCallback<List<String>> author_names = new GetAuthors<List<String>>();
		rpc.getAuthors(author_names);
		Button btn2 = new Button("View books by author");
		btn2.addClickHandler(new GetAllBooksByAUTHBTN());
		hPanel2.setBorderWidth(1);
		hPanel2.add(Lbl2);
		hPanel2.add(authorName);
		hPanel2.add(btn2);

		HorizontalPanel hPanel3 = new HorizontalPanel();
		Label Lbl3 = new Label("View all reviews for any audiobook");
		audiobookTitle.getElement().setPropertyString("placeholder", "enter audiobook title");
		Button btn3 = new Button("View reviews of book");
		btn3.addClickHandler(new GetAllReviewsOfBookBTN());
		hPanel3.setBorderWidth(1);
		hPanel3.add(Lbl3);
		hPanel3.add(audiobookTitle);
		hPanel3.add(btn3);

		HorizontalPanel hPanel4 = new HorizontalPanel();
		Label Lbl4 = new Label("Search for 'popular' books");
		Button btn4 = new Button("View most popular books");
		btn4.addClickHandler(new GetPopBooksBTN());
		hPanel4.setBorderWidth(1);
		hPanel4.add(Lbl4);
		hPanel4.add(btn4);

		HorizontalPanel hPanel5 = new HorizontalPanel();
		Label Lbl5 = new Label("Purchase an audio book");
		book_to_purchase.getElement().setPropertyString("placeholder", "enter purchase ISBN");
		Button btn5 = new Button("Make Purchase");
		btn5.addClickHandler(new purchBookBTN());
		hPanel5.setBorderWidth(1);
		hPanel5.add(Lbl5);
		hPanel5.add(book_to_purchase);
		hPanel5.add(btn5);

		Label myLbl = new Label("Enter required information and " 
				+ "click the buttons below that you desire, "
				+ "to perform desired actions ");

		vPanel.add(myLbl);
		vPanel.add(hPanel1);
		vPanel.add(hPanel2);
		vPanel.add(hPanel3);
		vPanel.add(hPanel4);
		vPanel.add(hPanel5);
		Button btn6 = new Button("Back");
		btn6.addClickHandler(new backBTN());
		vPanel.add(btn6);
	}
	// method to print results from database
	public void printResult(List<List<String>> items){
		result_hPanel.clear();
		for(int col = 0 ; col < items.get(1).size(); col++){
			VerticalPanel db_col = new VerticalPanel(); // set border here
			for(int row = 0 ; row < items.size(); row++){
				Label col_data  = new Label(items.get(row).get(col));
				if(items.get(row).get(col) == "")
					col_data.setText("Not provided");
				db_col.add(col_data);
			}
			db_col.setBorderWidth(1);
			result_hPanel.add(db_col);
		}
		vPanel.add(result_hPanel);
	}

	// asynchronous callback handler to receive all books in db
	private class GetAllBooks<T> implements AsyncCallback<List<List<String>>> {
		public void onFailure(Throwable ex) {
			RootPanel.get().add(new HTML("RPC to get all books failed."));
		}

		public void onSuccess(List<List<String>> books) {
			printResult(books);
		}
	}
	
	// asynchronous callback handler to receive books by author in db
	private class GetBooksByAuth<T> implements AsyncCallback<List<List<String>>> {
		public void onFailure(Throwable ex) {
			result_hPanel.clear();
			RootPanel.get().add(new HTML("RPC to get all books by author failed."));
		}

		public void onSuccess(List<List<String>> books) {
			printResult(books);
		}
	}
	
	// asynchronous callback handler to receive reviews from db
	private class GetReviewsOfBook<T> implements AsyncCallback<List<List<String>>> {
		public void onFailure(Throwable ex) {
			result_hPanel.clear();
			RootPanel.get().add(new HTML("RPC to get all reviews of book failed."));
		}

		public void onSuccess(List<List<String>> reviews) {
			printResult(reviews);
		}
	}	
	// asynchronous callback handler to receive pop books in db
	private class GetPopBooks<T> implements AsyncCallback<List<List<String>>> {
		public void onFailure(Throwable ex) {
			RootPanel.get().add(new HTML("RPC to get pop books failed."));
		}

		public void onSuccess(List<List<String>> books) {
			printResult(books);
		}
	}
	// asynchronous callback handler to purchase book
	private class PurchaseBook<T> implements AsyncCallback<String> {
		public void onFailure(Throwable ex) {
			RootPanel.get().add(new HTML("RPC to purchase book failed."));
		}

		public void onSuccess(String str) {
			result_hPanel.clear();
			Label result = new Label(str);
			result_hPanel.add(result);
			vPanel.add(result_hPanel);
		}
	}
	
	// asynchronous callback handler to get author names 
	private class GetAuthors<T> implements AsyncCallback<List<String>> {
		public void onFailure(Throwable ex) {
			RootPanel.get().add(new HTML("RPC to get authors failed"));
		}

		public void onSuccess(List<String> authors) {
			authorName.setTitle("Author");
			for(String author : authors)
				authorName.addItem(author);
			authorName.setVisibleItemCount(1);		
		}
	}

	// calls method specified by synch/asynch interfaces using RPC provider and aplying the callback handler
	private class GetAllBooksBTN implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {
			AsyncCallback<List<List<String>>> books = new GetAllBooks<List<List<String>>>();
			rpc.getAllBooks(books);
		}
	}
	// calls method specified by synch/asynch interfaces using RPC provider and aplying the callback handler
	private class GetAllBooksByAUTHBTN implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {
			AsyncCallback<List<List<String>>> booksByAuth = new GetBooksByAuth<List<List<String>>>();
			rpc.getBooksByAuthor(authorName.getSelectedValue(), booksByAuth);
		}
	}
	// calls method specified by synch/asynch interfaces using RPC provider and aplying the callback handler
	private class GetAllReviewsOfBookBTN implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {
			AsyncCallback<List<List<String>>> reviewsOfBook = new GetReviewsOfBook<List<List<String>>>();
			rpc.getReviewsOfBook(audiobookTitle.getText(), reviewsOfBook);
		}
	}
	private class GetPopBooksBTN implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {
			AsyncCallback<List<List<String>>> books = new GetPopBooks<List<List<String>>>();
			rpc.getPopBooks(books);
		}
	}
	private class purchBookBTN implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {
			AsyncCallback<String> str = new PurchaseBook<String>();
			rpc.purchBook(book_to_purchase.getText(), user_id,str);
		}
	}
	private class backBTN implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {
			Log_In_Screen log_in = new Log_In_Screen();
			RootPanel.get().clear();
			RootPanel.get().add(log_in);
		}
	}
}
