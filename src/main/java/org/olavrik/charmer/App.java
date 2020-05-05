package org.olavrik.charmer;


import org.olavrik.charmer.controller.DataProvider;
import org.olavrik.charmer.exceptions.PythonException;
import org.olavrik.charmer.view.TableManager;

import javax.swing.*;
import java.io.IOException;

final class App {
    private App() {

    }

    public static void main(final String[] args) throws IOException {
        DataProvider dataProvider = new DataProvider();
        TableManager tableManager = new TableManager(dataProvider);

        String pythonPath = null;

        JFileChooser fileOpen = new JFileChooser();
        fileOpen.setDialogTitle("Open Python interpreter (*.exe)");


        while (!dataProvider.setPythonPathIfPossible(pythonPath)) {
            int ret = fileOpen.showDialog(null, "Open Python interpreter");
            switch (ret) {
                case JFileChooser.APPROVE_OPTION:
                    pythonPath = fileOpen.getSelectedFile().getAbsolutePath();
                    continue;
                case JFileChooser.CANCEL_OPTION:
                    System.exit(0);
                case JFileChooser.ERROR_OPTION:
                    System.exit(0);
            }

        }

        try {

            dataProvider.startSession();
        } catch (PythonException e) {
            JOptionPane.showMessageDialog(null,
                    e.getMessage() + "\n" + e.getPythonError());
            System.exit(0);
        }

        String filename = null;

        while (filename == null) {
            fileOpen.setDialogTitle("Open CSV file (*.csv)");

            int usersChoice = fileOpen.showDialog(null, "Open CSV file");

            switch (usersChoice) {
                case JFileChooser.APPROVE_OPTION:
                    filename = fileOpen.getSelectedFile().getAbsolutePath();

                    if (!dataProvider.checkFile(filename.replace('\\', '/'))
                            || !filename.endsWith("csv")) {

                        filename = null;
                        JOptionPane.showMessageDialog(null,
                                "Error, file doesn't exist or incorrect format, please try again");
                    } else {
                        break;
                    }

                case JFileChooser.CANCEL_OPTION:
                case JFileChooser.ERROR_OPTION:
                    tableManager.dispose();
                    System.exit(0);
            }
        }

        tableManager.openCSV(filename);

    }

}
