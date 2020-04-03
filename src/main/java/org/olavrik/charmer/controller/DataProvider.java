package org.olavrik.charmer.controller;

import org.olavrik.charmer.model.PythonWrapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataProvider {
    PythonWrapper pythonWrapper;
    String pythonPath;


    public static String[] concat(String[] arr, String... elements) {
        String[] tempArr = new String[arr.length + elements.length];
        System.arraycopy(arr, 0, tempArr, 0, arr.length);

        for (int i = 0; i < elements.length; i++)
            tempArr[arr.length + i] = elements[i];
        return tempArr;

    }

    public Boolean checkPython(String s) throws InterruptedException {
        PythonWrapper pythonWrapper = new PythonWrapper();
        pythonWrapper.setPythonPath(s);
        pythonPath = pythonWrapper.getPythonPath();
        return pythonWrapper.checkPython();
    }

    public Boolean checkFile(String filePath) {
        return new File(filePath).exists();
    }


    public void openCSVSession(String filename) throws IOException {
        this.pythonWrapper = new PythonWrapper();
        // if you use this you should use it everywhere
        this.pythonWrapper.setPythonPath(pythonPath);
        this.pythonWrapper.startProcess();
        this.pythonWrapper.runCmd(PythonCmd.init(), false);
        this.pythonWrapper.runCmd(PythonCmd.loadDataFrame(filename), false);

    }


    public String[][] getCSVContent() {
        ArrayList<String> outputArray = this.pythonWrapper.runCmd(PythonCmd.getDataFrame(), true);

        List<String[]> data = new ArrayList<String[]>();

        String line;

        for (int index = 0; index < outputArray.size(); index++) {
            int strLength = outputArray.get(index).length();
            line = index + ";" + outputArray.get(index);
            data.add(line.split(";"));
        }

        // rename dataa to dataArray
        String[][] dataa = new String[data.size()][data.get(0).length];
        for (int i = 0; i < data.size(); i++) {
            dataa[i] = data.get(i);
        }

        return dataa;

    }

    public String getCSVSize()  {
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

        return concat(new String[]{"Id"}, line.split(", "));

    }


}

