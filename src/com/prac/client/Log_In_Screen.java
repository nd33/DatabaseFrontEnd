package com.prac.client;

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
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Log_In_Screen extends Composite {
	private VerticalPanel vPanel = new VerticalPanel();
	private HorizontalPanel result = new HorizontalPanel();
	private DBConnectionAsync rpc; // remote procedure call provider
	
	//components for db access
	public static TextBox ID = new TextBox();
	public static PasswordTextBox password = new PasswordTextBox();

	public Log_In_Screen() {
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
		Label Lbl1 = new Label("Username ID:");
		ID.getElement().setPropertyString("placeholder", "enter username ID");
		hPanel1.setBorderWidth(1);
		hPanel1.add(Lbl1);
		hPanel1.add(ID);

		Label Lbl2 = new Label("Password:");
		password.getElement().setPropertyString("placeholder", "enter password");
		hPanel1.setBorderWidth(1);
		hPanel1.add(Lbl2);
		hPanel1.add(password);
		
		Button btn1 = new Button("LOGIN");
		btn1.addClickHandler(new LogInBTN());
		Button btn2 = new Button("REGISTER");
		btn2.addClickHandler(new RegUser());
		hPanel1.setBorderWidth(1);
		hPanel1.add(btn1);
		hPanel1.add(btn2);


		Label myLbl = new Label("Enter login information");

		vPanel.add(myLbl);
		vPanel.add(hPanel1);
	}
	
	// asynchronous callback handler to authenticate user
	private class LogIn<T> implements AsyncCallback<String> {
		public void onFailure(Throwable ex) {
			RootPanel.get().add(new HTML("RPC to LOGIN failed"));
		}

		public void onSuccess(String isAuthenticated) {
			if(isAuthenticated.contentEquals("yes")){
				PageOne page = new PageOne();
				page.user_id = ID.getText();
				RootPanel.get().clear();
				RootPanel.get().add(page);
			}
			else{
				Label lbl3 = new Label("Enter correct ID and password !");
				result.clear();
				result.add(lbl3);
				vPanel.add(result);
			}
				
				
		}
	}

	private class LogInBTN implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {
			AsyncCallback<String> log_in_handle = new LogIn<String>();
			rpc.authenticate(ID.getText(),password.getText(), log_in_handle);
		}
	}
	private class RegUser implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {
			RegistrationScreen reg = new RegistrationScreen();
			RootPanel.get().clear();
			RootPanel.get().add(reg);
		}
	}
}