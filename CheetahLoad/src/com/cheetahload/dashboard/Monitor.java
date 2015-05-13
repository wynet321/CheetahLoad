package com.cheetahload.dashboard;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
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
		shell.setLayout(new FillLayout());

		SashForm sashFormHorizontal = new SashForm(shell, SWT.HORIZONTAL);

		Composite compTreeView = new Composite(sashFormHorizontal, SWT.BORDER);
		compTreeView.setLayout(new FillLayout());
		initializeTreeView(compTreeView);

		SashForm sashFormVertical = new SashForm(sashFormHorizontal, SWT.VERTICAL);
		Composite compChartView = new Composite(sashFormVertical, SWT.BORDER);
		compChartView.setLayout(new FillLayout());

		Composite compDataView = new Composite(sashFormVertical, SWT.BORDER);
		compDataView.setLayout(new FillLayout());
		initializeDataView(compDataView);

		shell.open();

		produceChart(compChartView);

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	//need generate testResults from DB later
	private void initializeTreeView(Composite comp) {
		HashMap<String, HashMap<String, String>> testResults = new HashMap<String, HashMap<String, String>>();
		testResults.put(DateFormat.getDateInstance().format(new Date()), new HashMap<String, String>());
		testResults.get(DateFormat.getDateInstance().format(new Date())).put("Test1", "GUID1");
		testResults.get(DateFormat.getDateInstance().format(new Date())).put("Test2", "GUID2");

		Tree tree = new Tree(comp, SWT.BORDER | SWT.SINGLE | SWT.FULL_SELECTION);
		ArrayList<TreeItem> treeParentItems = new ArrayList<TreeItem>();
		for (int i = 0; i < testResults.keySet().size(); i++) {
			treeParentItems.add(new TreeItem(tree, SWT.None));
			treeParentItems.get(i).setText(testResults.keySet().toArray()[i].toString());
		}

		ArrayList<TreeItem> treeChildItems = new ArrayList<TreeItem>();
		for (int j = 0; j < testResults.get(DateFormat.getDateInstance().format(new Date())).keySet().size(); j++) {
			treeChildItems.add(new TreeItem(treeParentItems.get(0), SWT.None));
			treeChildItems.get(j).setText(testResults.get(DateFormat.getDateInstance().format(new Date())).keySet().toArray()[j].toString());
		}
		
		treeParentItems.get(0).setExpanded(true);
	}

	// TODO sample code for chart, need add event and modify later
	private void produceChart(Composite comp) {
		GC gc = new GC(comp);
		gc.setForeground(new Color(comp.getDisplay(), 255, 0, 0));
		gc.setLineStyle(SWT.LINE_SOLID);
		gc.setLineWidth(2);
		gc.drawLine(10, 10, 100, 100);
		gc.drawRectangle(10, 10, 40, 45);
		gc.dispose();
	}

	private void initializeDataView(Composite comp) {
		CTabFolder tabFolder = new CTabFolder(comp, SWT.BORDER | SWT.BOTTOM);
		tabFolder.setSelectionForeground(new Color(null, 255, 255, 255));
		tabFolder.setSelectionBackground(new Color(null, 0, 0, 255));
		tabFolder.setSimple(false);
		ArrayList<CTabItem> tabItems = new ArrayList<CTabItem>();
		ArrayList<Composite> tabItemComposites = new ArrayList<Composite>();
		for (int i = 0; i < 5; i++) {
			tabItems.add(new CTabItem(tabFolder, SWT.CLOSE));
			tabItemComposites.add(new Composite(tabFolder, SWT.NONE));
			tabItemComposites.get(i).setBackground(new Color(null, 255, 255, 255));
			Label label = new Label(tabItemComposites.get(i), SWT.NONE);
			label.setText("Item " + i);
			tabItems.get(i).setControl(tabItemComposites.get(i));
		}
		tabItems.get(0).setText("Test Configuration");
		tabItems.get(2).setText("Throughput");
		tabItems.get(3).setText("Response Time");
		tabItems.get(4).setText("Success Rate");
		tabItems.get(1).setText("General Result");

	}

}
