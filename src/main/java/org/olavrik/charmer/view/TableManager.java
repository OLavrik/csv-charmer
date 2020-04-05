package org.olavrik.charmer.view;

import org.olavrik.charmer.controller.DataProvider;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

public class TableManager extends JFrame {
    private Object[] columnsHeader;
    private Object[][] dataArray;
    private ArrayList<String[]> datachanges = new ArrayList<>();
    private DataProvider dataProvider;

    private DefaultTableModel model;
    private Box contents = new Box(BoxLayout.X_AXIS);
    private JTable table1;


    public TableManager(DataProvider dataProvider) {
        super();
        this.dataProvider = dataProvider;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width / 2, screenSize.height / 2);
        setVisible(true);
        setResizable(true);
    }


    public void openCSV(String filePath) throws IOException {
        filePath = filePath.replace('\\', '/');
        dataProvider.openCSVSession(filePath);

        String fileName = Paths.get(filePath).getFileName().toString();

        String[] header = dataProvider.getCSVHeader();
        String[][] data = dataProvider.getCSVContent();
        this.dataArray = data;
        this.columnsHeader = header;
        setTitle(fileName);
        model = new DefaultTableModel(dataArray, columnsHeader);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        table1 = new JTable(model);

        table1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        contents = new Box(BoxLayout.Y_AXIS);
        contents.add(new JScrollPane(table1));
        setContentPane(contents);
        setVisible(true);


    }


}




