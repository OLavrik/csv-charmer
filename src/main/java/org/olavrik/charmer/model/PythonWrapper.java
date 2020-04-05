package org.olavrik.charmer.model;

import java.io.*;
import java.util.ArrayList;


public class PythonWrapper {
    ProcessBuilder processBuilder;
    String pythonPath;
    Process process;
    BufferedWriter bufferedWriter;

    public PythonWrapper() {

    }

    public void setPythonPath(String pythonPath) {
        this.pythonPath = (pythonPath == null) ? "/usr/local/bin/python3" : pythonPath;
    }

    public String getPythonPath() {
        return pythonPath;
    }


    private ArrayList<String> readOutput() throws IOException, InterruptedException {
        BufferedReader outputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        ArrayList<String> dataOutput = new ArrayList<String>();
        while (!outputReader.ready()) {
            Thread.sleep(100);
        }
        String line;

        while (outputReader.ready() && (line = outputReader.readLine()) != null) {
            dataOutput.add(line);
        }
        return dataOutput;
    }


    public Boolean checkPython() throws InterruptedException {
        try {


            ProcessBuilder checkpython = new ProcessBuilder(new String[]{this.pythonPath, "-i", "--version"});
            Process check = checkpython.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(check.getInputStream()));
            BufferedReader readererror = new BufferedReader(new InputStreamReader(check.getErrorStream()));

            while (!reader.ready() && !readererror.ready()) {
                Thread.sleep(100);
            }

            if (readererror.ready()) {
                return false;
            }

            String line;
            if (reader.ready() && (line = reader.readLine()) != null) {
                if (line.indexOf("Python") != -1 && line.indexOf("3.") != -1) {
                    return true;
                } else {
                    return false;
                }
            }
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }


    }


    public void startProcess() throws IOException {
        this.processBuilder = new ProcessBuilder(new String[]{this.pythonPath, "-i"});
        this.process = this.processBuilder.start();

    }

    public ArrayList<String> runCmd(String[] command, Boolean flag) {
        try {
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
            for (int i = 0; i < command.length; i++) {
                this.bufferedWriter.write(command[i]);
                this.bufferedWriter.newLine();
            }
            if (!flag) {
                this.bufferedWriter.write("print(\"Success\")");
                this.bufferedWriter.newLine();
            }
            this.bufferedWriter.flush();
            ArrayList<String> output = readOutput();

            return flag ? output : null;
        } catch (IOException e) {
            e.printStackTrace();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
