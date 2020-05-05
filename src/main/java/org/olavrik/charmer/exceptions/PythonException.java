package org.olavrik.charmer.exceptions;

import java.util.ArrayList;

public final class PythonException extends Exception {

    private final String pythonError;


    public String getPythonError() {
        return pythonError;
    }


    public PythonException(final String message, final String cmd, final ArrayList<String> errorOutput) {
        super(message + cmd);
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 1; i < errorOutput.size(); i++) {
            stringBuilder.append(errorOutput.get(i));
        }

        pythonError = stringBuilder.toString();

    }
}
