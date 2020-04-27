
package org.olavrik.charmer.controller;

import org.olavrik.charmer.model.PythonWrapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class DataProvider {
    private PythonWrapper pythonWrapper;
    private String pythonPath;


    public static String[] concat(final String[] arr, final String... elements) {
        String[] tempArr = new String[arr.length + elements.length];
        System.arraycopy(arr, 0, tempArr, 0, arr.length);

        for (int i = 0; i < elements.length; i++) {
            tempArr[arr.length + i] = elements[i];
        }

        return tempArr;

    }

    public Boolean setPythonPathIfPossible(final String possiblePythonPath) throws InterruptedException {
        PythonWrapper pythonCheker = new PythonWrapper();

        pythonPath = possiblePythonPath != null
                ? possiblePythonPath.replace('\\', '/') : possiblePythonPath;

        pythonCheker.setPythonPath(pythonPath);

        return pythonCheker.checkPython();
    }

    public Boolean checkFile(final String filePath) {
        return new File(filePath.replace('\\', '/')).exists();
    }


    public void openCSVSession(final String filename) throws IOException {
        pythonWrapper = new PythonWrapper();

        pythonWrapper.setPythonPath(pythonPath);
        pythonWrapper.startProcess();
        pythonWrapper.runCmd(PythonCmd.init(), false);
        pythonWrapper.runCmd(PythonCmd.loadDataFrame(filename.replace('\\', '/')), false);
    }

    public void deleteRow(final Integer indexRow) {
        pythonWrapper.runCmd(PythonCmd.deleteRow(indexRow), false);
    }


    public String[][] getCSVContent() {
        ArrayList<String> outputArray = pythonWrapper.runCmd(PythonCmd.getDataFrame(), true);

        List<String[]> data = new ArrayList<String[]>();

        String line;

        for (int index = 0; index < outputArray.size(); index++) {
            line = index + ";" + outputArray.get(index);
            data.add(line.split(";"));
        }


        String[][] dataArray = new String[data.size()][data.get(0).length];
        for (int i = 0; i < data.size(); i++) {
            dataArray[i] = data.get(i);
        }

        return dataArray;

    }

    public String getCSVSize() {
        ArrayList<String> outputArray = pythonWrapper.runCmd(PythonCmd.getDataFrameSize(), true);

        String line;
        line = outputArray.get(0);
        return line;

    }

    public String[] getCSVHeader() {
        ArrayList<String> outputArray = pythonWrapper.runCmd(PythonCmd.getHeader(), true);

        String line;

        line = outputArray.get(0);
        line = line.substring(1, line.length() - 1);

        String[] items = line.split(",");
        for (int index = 0; index < items.length; index++) {
            items[index] = items[index].trim();
        }

        return concat(new String[]{"Id"}, items);

    }


    public HashMap<String, int[]> getCSVHistograms(final String[] headerCaptions) {
        ArrayList<String> outputArray = pythonWrapper.runCmd(PythonCmd.getDataHistograms(), true);

        HashMap<String, int[]> data = new HashMap<String, int[]>();

        for (int index = 0; index < outputArray.size(); index++) {
            String[] lines = outputArray.get(index).split(";");

            int[] values = new int[lines.length];
            for (int j = 0; j < lines.length; j++) {
                values[j] = Integer.parseInt(lines[j]);
            }
            data.put(headerCaptions[index + 1], values);
        }
        data.put(headerCaptions[0], new int[]{0, 0});

        return data;
    }

    public void changeCellValue(final Integer indexRow, final String nameColumn, final String newVal) {
        pythonWrapper.runCmd(PythonCmd.changeValueCell(indexRow, nameColumn, newVal), false);
    }

    public void saveCSV() {
        pythonWrapper.runCmd(PythonCmd.saveCSV(), false);
    }

    public void addRow(final Integer indexRow, final ArrayList<String> newRow) {
        pythonWrapper.runCmd(PythonCmd.addRow(indexRow, newRow), false);

    }

}

