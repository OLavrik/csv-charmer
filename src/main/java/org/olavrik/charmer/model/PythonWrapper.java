package org.olavrik.charmer.model;

import java.io.*;
import java.util.ArrayList;


public final class PythonWrapper {
    private String pythonPath;
    private Process process;
    private final int timeWait = 100;

    public PythonWrapper() {
        String os = System.getProperty("os.name").toLowerCase();
        boolean isLinux = os.contains("mac") || os.contains("nix") || os.contains("nux") || os.contains("aix");

        if (isLinux) {
            pythonPath = "/usr/local/bin/python3";
        } else {
            String[] paths = System.getenv("PATH").toLowerCase().split(";");

            for (String path : paths) {
                if (path.contains("python3")) {
                    pythonPath = path + "/python.exe";
                    break;
                }
            }
        }
    }


    public String getPythonPath() {
        return pythonPath;
    }

    public void setPythonPath(final String possiblePythonPath) {
        pythonPath = (possiblePythonPath == null) ? pythonPath : possiblePythonPath;
    }


    private ArrayList<String> readOutput() throws IOException, InterruptedException {
        BufferedReader outputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        ArrayList<String> dataOutput = new ArrayList<>();

        while (!outputReader.ready()) {
            Thread.sleep(timeWait);
        }

        String line;
        while (outputReader.ready() && (line = outputReader.readLine()) != null) {
            dataOutput.add(line);
        }

        return dataOutput;
    }


    public Boolean check(final String[] commands, final String[] lookingFor) {
        try {
            ProcessBuilder checkPython = new ProcessBuilder(commands);
            Process check = checkPython.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(check.getInputStream()));

            while (!reader.ready()) {
                Thread.sleep(timeWait);
            }

            String line;
            if (reader.ready()) {
                line = reader.readLine();
                boolean flag = true;

                if (line != null) {
                    for (String elem : lookingFor) {
                        flag = flag && line.contains(elem);
                    }

                    return flag;
                } else {
                    return false;
                }
            }
            return false;
        } catch (IOException | InterruptedException e) {
            return false;
        }
    }


    public void startProcess() throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder(pythonPath, "-i");
        process = processBuilder.start();
    }

    public ArrayList<String> runCmd(final String[] command) {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));

            for (String s : command) {
                bufferedWriter.write(s);
                bufferedWriter.newLine();
            }

            bufferedWriter.flush();

            return readOutput();
        } catch (IOException | InterruptedException e) {
            return null;
        }
    }
}
