package com.prac.client;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class RegistrationScreen extends Composite {
	private VerticalPanel vPanel = new VerticalPanel();
	private VerticalPanel result = new VerticalPanel();
	private DBConnectionAsync rpc; // remote procedure call provider
	
	//registration information
	private TextBox ID = new TextBox();
	private PasswordTextBox password = new PasswordTextBox();
	private TextBox name = new TextBox();
	private TextBox date_of_birth = new TextBox();
	private TextBox email = new TextBox();
	private TextBox phone = new TextBox();

	public RegistrationScreen() {
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
		Label Lbl1 = new Label("Username ID (10 digits exact. starting with 1 or 2):");
		ID.getElement().setPropertyString("placeholder", "e.g. 1402950203");
		hPanel1.setBorderWidth(1);
		hPanel1.add(Lbl1);
		hPanel1.add(ID);

		HorizontalPanel hPanel2 = new HorizontalPanel();
		Label Lbl2 = new Label("Password (up to 12 characters.):");
		password.getElement().setPropertyString("placeholder", "enter password");
		hPanel2.setBorderWidth(1);
		hPanel2.add(Lbl2);
		hPanel2.add(password);
		
		HorizontalPanel hPanel3 = new HorizontalPanel();
		Label Lbl3 = new Label("name (up to 50 characters): ");
		name.getElement().setPropertyString("placeholder", "enter name");
		hPanel3.setBorderWidth(1);
		hPanel3.add(Lbl3);
		hPanel3.add(name);
		
		HorizontalPanel hPanel4 = new HorizontalPanel();
		Label Lbl4 = new Label("date of birth:");
		date_of_birth.getElement().setPropertyString("placeholder", "yyyy-mm-dd");
		hPanel4.setBorderWidth(1);
		hPanel4.add(Lbl4);
		hPanel4.add(date_of_birth);
		
		HorizontalPanel hPanel5 = new HorizontalPanel();
		Label Lbl5 = new Label("email (up to 50 characters):");
		email.getElement().setPropertyString("placeholder", "example@abv.bg");
		hPanel5.setBorderWidth(1);
		hPanel5.add(Lbl5);
		hPanel5.add(email);
		
		HorizontalPanel hPanel6 = new HorizontalPanel();
		Label Lbl6 = new Label("phone number (up to 20 digits):");
		phone.getElement().setPropertyString("placeholder", "enter phone");
		hPanel6.setBorderWidth(1);
		hPanel6.add(Lbl6);
		hPanel6.add(phone);
		
		HorizontalPanel hPanel7 = new HorizontalPanel();
		Button btn1 = new Button("Back");
		btn1.addClickHandler(new backBTN());
		Button btn2 = new Button("REGISTER");
		btn2.addClickHandler(new RegUser());
		hPanel7.setBorderWidth(1);
		hPanel7.add(btn1);
		hPanel7.add(btn2);


		Label myLbl = new Label("Enter login information");

		vPanel.add(myLbl);
		vPanel.add(hPanel1);
		vPanel.add(hPanel2);
		vPanel.add(hPanel3);
		vPanel.add(hPanel4);
		vPanel.add(hPanel5);
		vPanel.add(hPanel6);
		vPanel.add(hPanel7);
	}
	
	// asynchronous callback handler to get author names 
	private class RegisterCust<T> implements AsyncCallback<List<String>> {
		public void onFailure(Throwable ex) {
			RootPanel.get().add(new HTML("RPC to register failed !"));
		}

		public void onSuccess(List<String> details) {
			result.clear();
			// show problem to user
			for(String str : details)
				if(str != "yes")
					result.add(new Label(str));
	
			vPanel.add(result);
			if(result.getWidgetCount() == 0){
				Label lbl = new Label("Regisration successful press \"Back\" to go back to login screen.");
				DialogBox box = new DialogBox();
				box.add(lbl);
				vPanel.add(box);
			}
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
	private class RegUser implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {
			AsyncCallback<List<String>> reg_handle = new RegisterCust<List<String>>();
			rpc.register(ID.getText(),password.getText(),name.getText(),
					date_of_birth.getText(), email.getText(), phone.getText(), reg_handle);
		}
	}
}
