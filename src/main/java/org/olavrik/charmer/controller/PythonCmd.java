package org.olavrik.charmer.controller;

import java.util.ArrayList;

public class PythonCmd {

    public static String[] getDataFrameSize() {
        return new String[]{"print(df.size)"};
    }

    public static String[] getDataFrame() {
        return new String[]{"print(*(';'.join([str(_) for _ in df.iloc[index].to_list()]) " +
                "for index in range(len(df))), sep='\\n' )"};
    }

    public static String[] loadDataFrame(String filename) {
        StringBuilder commands=new StringBuilder("filename=\"" );

        commands.append(filename);
        commands.append("\"");

        return new String[]{commands.toString(), "df=pd.read_csv(filename, sep=',')"};
    }

    public static String[] init() {
        return new String[]{"import pandas as pd", "pd.set_option('display.max_rows', None)",
                "pd.set_option('display.max_columns', None)"};
    }

    public static String[] getDatFrameSlice(int start, int end) {
        StringBuilder commands=new StringBuilder("print(df[");
        commands.append(start);
        commands.append(":");
        commands.append(end);
        commands.append("])");
        return new String[]{commands.toString()};
    }

    public static String[] getNumColumns() {
        return new String[]{"print(len(df.columns))"};
    }

    public static String[] getNumRows() {
        return new String[]{"print(len(df)-1)"};
    }


    public static String[] addRow(Integer indexRow, ArrayList<String> row) {
        String[] commands = new String[6];

        commands[0] = "insert_before=" + indexRow;
        commands[1] = "df1 = df[:insert_before]";
        commands[2] = "df2 = df[insert_before:]";
        commands[3] = "new_row = pd.DataFrame(columns=list(df1.columns.values))";

        StringBuilder rowArray = new StringBuilder("new_row.loc[0] =[");

        for (int index = 0; index < row.size() - 1; index++) {
            rowArray.append("\"");
            rowArray.append(row.get(index));
            rowArray.append("\",");
        }

        rowArray.append("\"");
        rowArray.append(row.get(row.size() - 1));
        rowArray.append("\"]");

        commands[4] = rowArray.toString();
        commands[5] = "df = pd.concat([df1, new_row, df2])";

        return commands;
    }

    public static String[] changeValueCell(String header, Integer rowCount, String newValue) {
        StringBuilder commands = new StringBuilder("df.set_value(\"");

        commands.append(rowCount);
        commands.append("\", \"");
        commands.append(header);
        commands.append("\", ");
        commands.append("newValue");
        commands.append(")");

        return new String[]{commands.toString()};
    }

    public static String[] deletRow(Integer indexRow) {
        StringBuilder commands = new StringBuilder("df=df.drop([");

        commands.append(indexRow);
        commands.append("])");

        return new String[]{commands.toString()};
    }


    public static String[] saveCSV() {
        return new String[]{"df.to_csv(filename, index = False)"};
    }


    public static String[] getHeader() {
        return new String[]{"print([_ for _ in df.columns])"};
    }


}

