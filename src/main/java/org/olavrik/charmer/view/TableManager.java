package org.olavrik.charmer.view;

import org.olavrik.charmer.controller.DataProvider;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public final class TableManager extends JFrame {
    private Object[] columnsHeader;
    private Object[][] dataArray;
    private DataProvider dataProvider;

    private DefaultTableModel model;
    private Box contents = new Box(BoxLayout.X_AXIS);
    private JTable table1;


    public TableManager(final DataProvider provider) {
        super();
        this.dataProvider = provider;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width / 2, screenSize.height / 2);
        setVisible(true);
        setResizable(true);
    }


    public void openCSV(final String filePath) throws IOException {
        dataProvider.openCSVSession(filePath);

        String fileName = Paths.get(filePath).getFileName().toString();

        String[] header = dataProvider.getCSVHeader();
        String[][] data = dataProvider.getCSVContent();
        HashMap<String, int[]> dataHistograms = dataProvider.getCSVHistograms(header);

        dataArray = data;
        columnsHeader = header;

        setTitle(fileName);

        model = new DefaultTableModel(dataArray, columnsHeader);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        table1 = new JTable(model);

        table1.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(final TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    if (columnsHeader[e.getColumn()] != "Id") {
                        dataProvider.changeCellValue(e.getFirstRow(),
                                columnsHeader[e.getColumn()].toString(),
                                table1.getModel().getValueAt(e.getFirstRow(),
                                        e.getColumn()).toString());
                    }
                }
            }
        });

        table1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table1.getTableHeader().setDefaultRenderer(new SimpleHeaderRenderer(dataHistograms));

        contents = new Box(BoxLayout.Y_AXIS);
        contents.add(new JScrollPane(table1));

        new PopupMenu();
        setContentPane(contents);
        contents.add(new FooterPanel());
        setVisible(true);


    }

    private void updateId() {
        for (int i = 0; i < model.getRowCount(); i++) {
            model.setValueAt(String.valueOf(i), i, 0);
        }
    }

    class PopupMenu implements ActionListener {
        PopupMenu() {
            JPopupMenu popupMenu = new JPopupMenu();
            JMenuItem item1 = new JMenuItem("Delete row");
            JMenuItem item2 = new JMenuItem("Add row before");
            JMenuItem item3 = new JMenuItem("Add row after");
            item1.addActionListener(this);
            item2.addActionListener(this);
            item3.addActionListener(this);
            popupMenu.add(item1);
            popupMenu.add(item2);
            popupMenu.add(item3);
            table1.setComponentPopupMenu(popupMenu);
        }

        private ArrayList<String> makeRow(final String[] row) {
            long startTime = System.nanoTime();

            ArrayList<String> futureRow = new ArrayList<>();
            for (int index = 0; index < columnsHeader.length - 1; index++) {
                if (index < row.length) {
                    futureRow.add(row[index]);
                } else {
                    futureRow.add("NaN");

                }
            }
            long endTime = System.nanoTime();

            long duration = (endTime - startTime);
            System.out.print("makeRow:");
            System.out.print(duration);
            System.out.println();
            return futureRow;
        }

        private void addRow(final Integer rowIndex) {
            if (rowIndex == -1) {
                JOptionPane.showMessageDialog(null,
                        "Row will be added to the end of the table");
            }
            String message2 = "Write row's element with comma separeted";
            String futureRow = JOptionPane.showInputDialog(null, message2);

            if (columnsHeader.length - 1 != futureRow.split(", ").length) {
                int select = JOptionPane.showConfirmDialog(null,
                        "Not enough elemntes, are you shure you want to concat?");
                if (select != JOptionPane.YES_OPTION) {
                    return;
                }
            }
            ArrayList<String> rowForCSV = makeRow(futureRow.split(","));
            dataProvider.addRow(rowIndex, rowForCSV);
            String[] futureR = DataProvider.concat(new String[]{String.valueOf(dataArray.length)},
                    futureRow.split(","));
            model.insertRow(rowIndex, futureR);
            updateId();

        }

        @Override
        public void actionPerformed(final ActionEvent e) {
            String evenName = e.getActionCommand();
            if (evenName.equals("Delete row")) {
                if (table1.getSelectedRow() == -1) {
                    JOptionPane.showMessageDialog(null, "Please,select row");
                } else {
                    int select = JOptionPane.showConfirmDialog(null, "delete?");
                    if (select == JOptionPane.YES_OPTION) {
                        int indexRow = table1.getSelectedRow();

                        model.removeRow(indexRow);

                        dataProvider.deleteRow(indexRow);


                    }
                }
            }
            if (evenName.equals("Add row before")) {
                Integer indexRow = table1.getSelectedRow();
                addRow(indexRow);
            }

            if (evenName.equals("Add row after")) {
                Integer indexRow = table1.getSelectedRow();
                addRow(indexRow + 1);
            }
        }

    }


    class FooterPanel extends JPanel implements ActionListener {
        FooterPanel() {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            final int panelHeight = 40;
            setPreferredSize(new Dimension(screenSize.width, panelHeight));

            JButton button1 = new JButton("Save CSV");
            add(button1);
            button1.addActionListener(this);

        }


        @Override
        public void actionPerformed(final ActionEvent e) {
            String evenName = e.getActionCommand();
            if (evenName.equals("Save CSV")) {
                int select = JOptionPane.showConfirmDialog(null,
                        "Are you sure you want to update CSV file?");
                if (select == JOptionPane.YES_OPTION) {
                    dataProvider.saveCSV();
                }
            }

        }
    }

    class SimpleHeaderRenderer extends JPanel implements TableCellRenderer {
        private JLabel label;
        private final HeaderHistogram hist;

        SimpleHeaderRenderer(final HashMap<String, int[]> histogramBins) {
            super(new BorderLayout());

            Box contentsHist = new Box(BoxLayout.Y_AXIS);
            label = new JLabel();
            final int size = 12;
            label.setFont(new Font("Consolas", Font.BOLD, size));
            contentsHist.add(label);

            hist = new HeaderHistogram();
            contentsHist.add(hist);

            hist.set(histogramBins);

            add(contentsHist);
        }

        @Override
        public Component getTableCellRendererComponent(final JTable table, final Object value,
                                                       final boolean isSelected, final boolean hasFocus,
                                                       final int row, final int column) {
            label.setText(value.toString());
            hist.setCurrent(value.toString());
            return this;
        }

        @Override
        public BaselineResizeBehavior getBaselineResizeBehavior() {
            return super.getBaselineResizeBehavior();
        }
    }


}




