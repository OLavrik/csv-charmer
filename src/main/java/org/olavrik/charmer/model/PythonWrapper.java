package org.olavrik.charmer.model;

import java.io.*;
import java.util.ArrayList;


public final class PythonWrapper {
    private ProcessBuilder processBuilder;
    private String pythonPath;
    private Process process;
    private BufferedWriter bufferedWriter;
    private final int timeWhait = 100;

    public PythonWrapper() {

    }

    public void setPythonPath(final String possiblePythonPath) {
        this.pythonPath = (possiblePythonPath == null) ? "/usr/local/bin/python3" : possiblePythonPath;
    }


    private ArrayList<String> readOutput() throws IOException, InterruptedException {
        BufferedReader outputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        ArrayList<String> dataOutput = new ArrayList<String>();
        while (!outputReader.ready()) {
            Thread.sleep(timeWhait);
        }
        String line;

        while (outputReader.ready() && (line = outputReader.readLine()) != null) {
            dataOutput.add(line);
        }
        return dataOutput;
    }


    public Boolean checkPython() throws InterruptedException {
        try {


            ProcessBuilder checkpython = new ProcessBuilder(this.pythonPath, "-i", "--version");
            Process check = checkpython.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(check.getInputStream()));
            BufferedReader readererror = new BufferedReader(new InputStreamReader(check.getErrorStream()));

            while (!reader.ready() && !readererror.ready()) {
                Thread.sleep(timeWhait);
            }

            if (readererror.ready()) {
                return false;
            }

            String line;
            if (reader.ready()) {
                line = reader.readLine();

                return line != null && line.contains("Python") && line.contains("3.");
            }
            return false;
        } catch (IOException e) {
            return false;
        }


    }


    public void startProcess() throws IOException {
        this.processBuilder = new ProcessBuilder(this.pythonPath, "-i");
        this.process = this.processBuilder.start();

    }

    public ArrayList<String> runCmd(final String[] command, final Boolean flag) {
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
        } catch (IOException | InterruptedException e) {
            return null;

        }
    }
}
