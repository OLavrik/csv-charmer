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
        String os = System.getProperty("os.name").toLowerCase();
        Boolean isLinux = os.contains("mac") || os.contains("nix") || os.contains("nux") || os.contains("aix");
        if (isLinux) {
            pythonPath = "/usr/local/bin/python3";
        } else {
            String[] paths = System.getenv("PATH").toLowerCase().split(";");
            for (String path : paths) {
                if (path.contains("python3")) {
                    pythonPath = new StringBuilder(path).append("/python.exe").toString();
                    break;
                }
            }
        }
    }

    public void setPythonPath(final String possiblePythonPath) {
        pythonPath = (possiblePythonPath == null) ? pythonPath : possiblePythonPath;
    }


    private ArrayList<String> readOutput() throws IOException, InterruptedException {
        BufferedReader outputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
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


            ProcessBuilder checkpython = new ProcessBuilder(pythonPath, "-i", "--version");
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
        processBuilder = new ProcessBuilder(pythonPath, "-i");
        process = processBuilder.start();

    }

    public ArrayList<String> runCmd(final String[] command, final Boolean flag) {
        try {
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
            for (int i = 0; i < command.length; i++) {
                bufferedWriter.write(command[i]);
                bufferedWriter.newLine();
            }
            if (!flag) {
                bufferedWriter.write("print(\"Success\")");
                bufferedWriter.newLine();
            }
            bufferedWriter.flush();
            ArrayList<String> output = readOutput();

            return flag ? output : null;
        } catch (IOException | InterruptedException e) {
            return null;

        }
    }
}
