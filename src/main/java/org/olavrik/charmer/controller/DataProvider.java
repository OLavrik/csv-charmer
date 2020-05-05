
package org.olavrik.charmer.controller;

import org.olavrik.charmer.exceptions.PythonException;
import org.olavrik.charmer.model.PythonWrapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public final class DataProvider {
    private PythonWrapper pythonWrapper;
    private String pythonPath;
    private final PythonCmd pythonCmd;

    public DataProvider() {
        pythonCmd = new PythonCmd();
    }

    private void checkOutput(final ArrayList<String> output, final String cmdName) throws PythonException {
        for (String elem : output) {
            if (elem.equals("==InternalError==")) {
                throw new PythonException("Error with command: ", cmdName, output);
            }
        }
    }


    public static String[] concat(final String[] arr, final String... elements) {
        String[] tempArr = new String[arr.length + elements.length];
        System.arraycopy(arr, 0, tempArr, 0, arr.length);

        System.arraycopy(elements, 0, tempArr, arr.length, elements.length);

        return tempArr;

    }

    public Boolean setPythonPathIfPossible(final String possiblePythonPath) {

        PythonWrapper pythonCheck = new PythonWrapper();

        pythonPath = possiblePythonPath != null
                ? possiblePythonPath.replace('\\', '/') : null;

        pythonCheck.setPythonPath(pythonPath);

        return pythonCheck.check(new String[]{pythonCheck.getPythonPath(), "-i", "--version"},
                new String[]{"Python", "3."});
    }


    public Boolean checkFile(final String filePath) {

        return new File(filePath.replace('\\', '/')).exists();
    }

    public void startSession() throws IOException, PythonException {
        pythonWrapper = new PythonWrapper();

        pythonWrapper.setPythonPath(pythonPath);
        pythonWrapper.startProcess();

        ArrayList<String> outputArray = pythonWrapper.runCmd(pythonCmd.init());
        checkOutput(Objects.requireNonNull(outputArray), "\"Import library Pandas\"");

    }


    public void openCSVSession(final String filename) throws PythonException {

        ArrayList<String> outputArray = pythonWrapper.runCmd(pythonCmd.loadDataFrame(filename.replace('\\', '/')));
        checkOutput(Objects.requireNonNull(outputArray), "\"Load dataframe\"");
    }

    public void deleteRow(final Integer indexRow) throws PythonException {
        ArrayList<String> outputArray = pythonWrapper.runCmd(pythonCmd.deleteRow(indexRow));
        checkOutput(Objects.requireNonNull(outputArray), "\"Delete row\"");

    }


    public String[][] getCSVContent() throws PythonException {
        ArrayList<String> outputArray = pythonWrapper.runCmd(pythonCmd.getDataFrame());

        checkOutput(Objects.requireNonNull(outputArray), "\"Get csv content\"");

        List<String[]> data = new ArrayList<>();

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

    public String getCSVSize() throws PythonException {
        ArrayList<String> outputArray = pythonWrapper.runCmd(pythonCmd.getDataFrameSize());

        checkOutput(Objects.requireNonNull(outputArray), "\"Get csv size\"");

        String line;
        line = outputArray.get(0);
        return line;

    }

    public String[] getCSVHeader() throws PythonException {
        ArrayList<String> outputArray = pythonWrapper.runCmd(pythonCmd.getHeader());

        checkOutput(Objects.requireNonNull(outputArray), "\"Get csv header\"");

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
        ArrayList<String> outputArray = pythonWrapper.runCmd(pythonCmd.getDataHistograms());

        HashMap<String, int[]> data = new HashMap<>();

        for (int index = 0; index < Objects.requireNonNull(outputArray).size(); index++) {
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

    public void changeCellValue(final Integer indexRow, final String nameColumn, final String newVal)
            throws PythonException {
        ArrayList<String> outputArray = pythonWrapper.runCmd(pythonCmd.changeValueCell(indexRow, nameColumn, newVal));
        checkOutput(Objects.requireNonNull(outputArray), "\"Change cell value\"");
    }


    public void saveCSV(String path) throws PythonException {
        ArrayList<String> outputArray = pythonWrapper.runCmd(pythonCmd.saveCSV(path));
        checkOutput(Objects.requireNonNull(outputArray), "\"Save csv\"");
    }

    public void addRow(final Integer indexRow, final ArrayList<String> newRow) throws PythonException {
        ArrayList<String> outputArray = pythonWrapper.runCmd(pythonCmd.addRow(indexRow, newRow));
        checkOutput(Objects.requireNonNull(outputArray), "\"Add row\"");

    }

}

