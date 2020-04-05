package org.olavrik.charmer;


import org.olavrik.charmer.controller.DataProvider;
import org.olavrik.charmer.view.TableManager;

import javax.swing.*;
import java.io.IOException;

public final class App {
    private App() {

    }

    public static void main(final String[] args) throws IOException, InterruptedException {
        DataProvider dataProvider = new DataProvider();
        TableManager tableManager = new TableManager(dataProvider);

        String pythonPath = null;

        while (!dataProvider.setPythonPathIfPossible(pythonPath)) {
            pythonPath = JOptionPane.showInputDialog(tableManager,
                    "Python 3 was not found, please, enter python3 location:");
        }

        String filename = JOptionPane.showInputDialog(tableManager, "Enter csv path:");
        if (filename == null) {
            tableManager.dispose();

        } else {
            while (!dataProvider.checkFile(filename.replace('\\', '/'))) {
                JOptionPane.showMessageDialog(null,
                        "Error, file doesn't exist, please try again");
                filename = JOptionPane.showInputDialog(tableManager, "Enter csv path:");
            }

            tableManager.openCSV(filename);
        }

    }

}
