/**
 * Info about this package doing something for package-info.java file.
 */
package org.olavrik.charmer.controller;

import org.olavrik.charmer.model.PythonWrapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataProvider {
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

    public Boolean setPythonPathIfPossible(String pythonPath) throws InterruptedException {
        PythonWrapper pythonWrapper = new PythonWrapper();

        pythonPath = pythonPath != null ? pythonPath.replace('\\', '/') : pythonPath;

        pythonWrapper.setPythonPath(pythonPath);
        this.pythonPath = pythonWrapper.getPythonPath();
        return pythonWrapper.checkPython();
    }

    public Boolean checkFile(String filePath) {
        return new File(filePath.replace('\\', '/')).exists();
    }


    public void openCSVSession(String filename) throws IOException {
        this.pythonWrapper = new PythonWrapper();

        this.pythonWrapper.setPythonPath(pythonPath);
        this.pythonWrapper.startProcess();
        this.pythonWrapper.runCmd(PythonCmd.init(), false);
        this.pythonWrapper.runCmd(PythonCmd.loadDataFrame(filename.replace('\\', '/')), false);
    }


    public String[][] getCSVContent() {
        ArrayList<String> outputArray = this.pythonWrapper.runCmd(PythonCmd.getDataFrame(), true);

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
        ArrayList<String> outputArray = this.pythonWrapper.runCmd(PythonCmd.getDataFrameSize(), true);

        String line;
        line = outputArray.get(0);
        return line;

    }

    public String[] getCSVHeader() {
        ArrayList<String> outputArray = this.pythonWrapper.runCmd(PythonCmd.getHeader(), true);

        String line;

        line = outputArray.get(0);
        line = line.substring(1, line.length() - 1);

        String[] items = line.split(",");
        for (int index = 0; index < items.length; index++) {
            items[index] = items[index].trim();
        }

        return concat(new String[]{"Id"}, items);

    }


}

