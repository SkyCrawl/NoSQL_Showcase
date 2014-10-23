package com.example.nosql_homework;

import org.vaadin.dialogs.ConfirmDialog;

import com.google.gwt.event.dom.client.KeyCodes;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;

public class StaticMethods
{
	public static ConfirmDialog createTextConfirmDialog(UI parentUI, String message, Button.ClickListener okAction)
	{
		ConfirmDialog result = ConfirmDialog.getFactory().create(null, message, "OK", "Cancel");
		setDialogProperties(result);
		result.getOkButton().addClickListener(okAction);
		result.show(parentUI, null, true);
		parentUI.setFocusedComponent(result);
		return result;
	}
	
	@SuppressWarnings("serial")
	public static Window createComponentConfirmDialog(UI parentUI, String caption, Component subContent, final Button.ClickListener okAction)
	{
		// define underlying components
        Label spacer = new Label("");
        spacer.setSizeFull();
        
        final Button cancel = new Button("Cancel");
        final Button ok = new Button("OK");
        ok.addClickListener(new Button.ClickListener()
		{
			@Override
			public void buttonClick(ClickEvent event)
			{
				okAction.buttonClick(event);
				cancel.click();
			}
		});
        ok.setClickShortcut(KeyCode.ENTER, null);

        // create the buttons component
        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setSizeFull();
        buttons.setSpacing(true);
        buttons.addComponent(spacer);
        buttons.setExpandRatio(spacer, 1f);
        buttons.addComponent(ok);
        buttons.setComponentAlignment(ok, Alignment.MIDDLE_RIGHT);
        buttons.addComponent(cancel);
        buttons.setComponentAlignment(cancel, Alignment.MIDDLE_RIGHT);
        
        // wrap the content component
        VerticalLayout content = new VerticalLayout();
        content.setSpacing(true);
        content.addComponent(subContent);
        content.addComponent(buttons);

        // create the dialog
        final Window result = new Window(caption, content);
        content.setSizeUndefined();
        setDialogProperties(result);
        result.setSizeFull();
        result.setSizeUndefined();
        cancel.addClickListener(new Button.ClickListener()
		{
			@Override
			public void buttonClick(ClickEvent event)
			{
				result.close();
			}
		});
        parentUI.addWindow(result);
        parentUI.setFocusedComponent(result);
        
        return result;
	}
	
	private static void setDialogProperties(Window window)
	{
		window.setClosable(true);
		window.setDraggable(false);
		window.setModal(true);
		window.setResizable(false);
		window.setCloseShortcut(KeyCodes.KEY_ESCAPE, null);
	}
}
