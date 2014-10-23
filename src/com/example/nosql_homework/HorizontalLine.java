package com.example.nosql_homework;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;

@SuppressWarnings("serial")
@Deprecated
public class HorizontalLine extends Label
{
	public HorizontalLine()
	{
		super("<hr />", ContentMode.HTML);
		// this.setWidth("80%");
		this.setHeight("5px");
    }
}
