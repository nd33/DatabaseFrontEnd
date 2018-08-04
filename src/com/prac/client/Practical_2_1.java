package com.prac.client;


import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Practical_2_1 implements EntryPoint {
	
	public void onModuleLoad() {
		
		Log_In_Screen init_screen = new Log_In_Screen();
		RootPanel.get().add(init_screen);
	}
}
