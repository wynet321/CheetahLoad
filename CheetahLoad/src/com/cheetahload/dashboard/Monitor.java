package com.cheetahload.dashboard;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class Monitor {
	public Monitor() {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Dashboard");
		// shell.setMaximized(true);
		FormLayout layout = new FormLayout();
		layout.marginHeight = 10;
		layout.marginWidth = 10;
		layout.spacing = 10;
		//shell.setLayout(layout);

		showTabFolder(shell);

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	private void showTreeView() {

	}

	private void showChartView() {

	}

	private void showTableView() {

	}

	private void showTabFolder(Shell shell) {
		CTabFolder tabFolder = new CTabFolder(shell,SWT.BORDER|SWT.BOTTOM);
		tabFolder.setBounds(10, 10, 800, 400);;
		tabFolder.setSelectionForeground(new Color(null, 255,255,255));
		tabFolder.setSelectionBackground(new Color(null, 0,0,255));
		tabFolder.setSimple(false);
		
		for (int i = 0; i < 4; i++) {
			CTabItem item = new CTabItem(tabFolder, SWT.CLOSE);
			item.setText("Item " + i);
			Composite comp=new Composite(tabFolder, SWT.NONE);
			comp.setBackground(new Color(null,255,255,255));
			comp.setBounds(0, 0, 400, 200);;
//			Label label=new Label(comp,SWT.NONE);
//			label.setText("Item "+i);
//			label.pack();
			item.setControl(comp);
		}
//tabFolder.pack();
	}
}
