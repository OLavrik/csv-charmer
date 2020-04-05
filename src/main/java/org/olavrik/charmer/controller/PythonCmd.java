package org.olavrik.charmer.controller;

import java.util.ArrayList;

public final class PythonCmd {
    private PythonCmd() {

    }

    public static String[] getDataFrameSize() {
        return new String[]{"print(df.size)"};
    }

    public static String[] getDataFrame() {
        return new String[]{"print(*(';'.join([str(_) for _ in df.iloc[index].to_list()]) "
                + "for index in range(len(df))), sep='\\n' )"};
    }

    public static String[] loadDataFrame(final String filename) {
        StringBuilder commands = new StringBuilder("filename=\"");

        commands.append(filename);
        commands.append("\"");

        return new String[]{commands.toString(), "df=pd.read_csv(filename)"};
    }

    public static String[] init() {
        return new String[]{"import pandas as pd", "pd.set_option('display.max_rows', None)",
                "pd.set_option('display.max_columns', None)"};
    }

    public static String[] getDatFrameSlice(final int start, final int end) {
        StringBuilder commands = new StringBuilder("print(df[");

        commands.append(start);
        commands.append(":");
        commands.append(end);
        commands.append("])");

        return new String[]{commands.toString()};
    }

    public static String[] getDataHistograms() {
        return new String[]{"print(*(';'.join([str(_) for _ in list(df[name].value_counts().sort_index())]) "
                + "for name in df.columns), sep='\\n')"};
    }

    public static String[] addRow(final Integer indexRow, final ArrayList<String> row) {
        final int numCommands = 7;
        int cmdIter = 0;

        String[] commands = new String[numCommands];

        commands[cmdIter++] = "insert_before=" + indexRow;
        commands[cmdIter++] = "df1 = df[:insert_before]";
        commands[cmdIter++] = "df2 = df[insert_before:]";
        commands[cmdIter++] = "new_row = pd.DataFrame(columns=list(df1.columns.values))";

        StringBuilder rowArray = new StringBuilder("new_row.loc[0] =[");

        for (int index = 0; index < row.size() - 1; index++) {
            rowArray.append("\"");
            rowArray.append(row.get(index));
            rowArray.append("\",");
        }

        rowArray.append("\"");
        rowArray.append(row.get(row.size() - 1));
        rowArray.append("\"]");

        commands[cmdIter++] = rowArray.toString();
        commands[cmdIter++] = "df = pd.concat([df1, new_row, df2])";
        commands[cmdIter++] = "df.reindex()";

        return commands;
    }

    public static String[] changeValueCell(final Integer indexRow, final String nameColumn, final String newValue) {
        StringBuilder commands = new StringBuilder("df.at[");
        commands.append(indexRow);
        commands.append(", ");
        commands.append(nameColumn);
        commands.append("]=\"");
        commands.append(newValue);
        commands.append("\"");
        return new String[]{commands.toString()};
    }

    public static String[] deleteRow(final Integer indexRow) {
        StringBuilder commands = new StringBuilder("df=df.drop([");
        commands.append(indexRow);
        commands.append("])");
        return new String[]{commands.toString(), "df.reindex()"};
    }


    public static String[] saveCSV() {
        return new String[]{"df.to_csv(filename, index = False)"};
    }


    public static String[] getHeader() {
        return new String[]{"print([_ for _ in df.columns])"};
    }


}

