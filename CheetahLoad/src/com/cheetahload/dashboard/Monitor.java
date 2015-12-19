package com.cheetahload.dashboard;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class Monitor {
	private Display display;
	private Shell shell;

	public Monitor() {
		display = new Display();
		shell = new Shell(display);
		shell.setText("Dashboard");
		// shell.setMaximized(true);
		// layout.marginHeight = 10;
		// layout.marginWidth = 10;
		// layout.spacing = 10;
		shell.setLayout(new FillLayout());

		SashForm sashFormHorizontal = new SashForm(shell, SWT.HORIZONTAL);

		Composite compTreeView = new Composite(sashFormHorizontal, SWT.BORDER);
		compTreeView.setLayout(new FillLayout());
		showTreeView(compTreeView);

		SashForm sashFormVertical = new SashForm(sashFormHorizontal, SWT.VERTICAL);
		Composite compChartView = new Composite(sashFormVertical, SWT.BORDER);
		compChartView.setLayout(new FillLayout());
		showChartView(compChartView);
		Composite compDataView = new Composite(sashFormVertical, SWT.BORDER);
		compDataView.setLayout(new FillLayout());
		showDataView(compDataView);

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	private void showTreeView(Composite comp) {
		Tree tree = new Tree(comp, SWT.BORDER);
		TreeItem item1 = new TreeItem(tree, 0);
		item1.setText("finally");
		TreeItem item2 = new TreeItem(tree, 0);
		item2.setText("wait");

	}

	private void showChartView(Composite comp) {
		CTabFolder tabFolder = new CTabFolder(comp, SWT.BORDER | SWT.BOTTOM);
		// tabFolder.setBounds(10, 10, 800, 400);
		tabFolder.setSelectionForeground(new Color(null, 255, 255, 255));
		tabFolder.setSelectionBackground(new Color(null, 0, 0, 255));
		tabFolder.setSimple(false);

		for (int i = 0; i < 4; i++) {
			CTabItem item = new CTabItem(tabFolder, SWT.CLOSE);
			item.setText("Item " + i);
			Composite comp1 = new Composite(tabFolder, SWT.NONE);
			comp.setBackground(new Color(null, 255, 255, 255));
			Label label = new Label(comp1, SWT.NONE);
			label.setText("Item " + i);
			// label.setBounds(10, 10, 100, 100);
			item.setControl(comp1);
		}
	}

	private void showDataView(Composite comp) {

	}

}
