package org.olavrik.charmer.controller;

import java.util.ArrayList;

public final class PythonCmd {
    private final ArrayList<String> startCmd = new ArrayList<>();


    private final ArrayList<String> endCmd = new ArrayList<>();

    private static void joinArraysOnLeft(final ArrayList<String> start, final ArrayList<String> end) {
        start.addAll(end);
    }


    public PythonCmd() {
        startCmd.add("try:");
        endCmd.add("except Exception as e:");
        endCmd.add("  print (\"==InternalError==\")");
        endCmd.add("  print(e)");
        endCmd.add("\n");
    }

    public String[] getDataFrameSize() {
        ArrayList<String> commands;
        commands = startCmd;
        commands.add("  print(df.size)");
        joinArraysOnLeft(commands, endCmd);
        return commands.toArray(new String[0]);
    }

    public String[] getDataFrame() {
        ArrayList<String> commands = new ArrayList<>();
        joinArraysOnLeft(commands, startCmd);

        commands.add("  print(*(';'.join([str(_) for _ in df.iloc[index].to_list()]) "
                + "for index in range(len(df))), sep='\\n' )");
        joinArraysOnLeft(commands, endCmd);
        return commands.toArray(new String[0]);
    }

    public String[] loadDataFrame(final String filename) {
        ArrayList<String> commands = new ArrayList<>();
        joinArraysOnLeft(commands, startCmd);

        String fileCmd = "  filename=\"" + filename
                + "\"";
        commands.add(fileCmd);
        commands.add("  df=pd.read_csv(filename)");
        commands.add("  print(\"Success\")");
        joinArraysOnLeft(commands, endCmd);
        return commands.toArray(new String[0]);
    }

    public String[] init() {
        ArrayList<String> commands = new ArrayList<>();
        joinArraysOnLeft(commands, startCmd);
        commands.add("  import pandas as pd");
        commands.add("  pd.set_option('display.max_rows', None)");
        commands.add("  pd.set_option('display.max_columns', None)");
        commands.add("  print(\"Success\")");
        joinArraysOnLeft(commands, endCmd);
        return commands.toArray(new String[0]);
    }


    public String[] getDatFrameSlice(final int start, final int end) {
        ArrayList<String> commands = new ArrayList<>();
        joinArraysOnLeft(commands, startCmd);


        String sliceCmd = "  print(df[" + start
                + ":"
                + end
                + "])";
        commands.add(sliceCmd);
        joinArraysOnLeft(commands, endCmd);
        return commands.toArray(new String[0]);
    }

    public String[] getDataHistograms() {
        ArrayList<String> commands = new ArrayList<>();
        joinArraysOnLeft(commands, startCmd);

        commands.add("  print(*(';'.join([str(_) for _ in list(df[name].value_counts().sort_index())]) "
                + "for name in df.columns), sep='\\n')");

        joinArraysOnLeft(commands, endCmd);
        return commands.toArray(new String[0]);
    }

    public String[] addRow(final Integer indexRow, final ArrayList<String> row) {
        ArrayList<String> commands = new ArrayList<>();
        joinArraysOnLeft(commands, startCmd);


        commands.add("  insert_before=" + indexRow);
        commands.add("  df1 = df[:insert_before]");
        commands.add("  df2 = df[insert_before:]");
        commands.add("  new_row = pd.DataFrame(columns=list(df1.columns.values))");

        StringBuilder rowArray = new StringBuilder("  new_row.loc[0] =[");

        for (int index = 0; index < row.size() - 1; index++) {
            rowArray.append("\"");
            rowArray.append(row.get(index));
            rowArray.append("\",");
        }

        rowArray.append("\"");
        rowArray.append(row.get(row.size() - 1));
        rowArray.append("\"]");

        commands.add(rowArray.toString());
        commands.add("  df = pd.concat([df1, new_row, df2])");
        commands.add("  df.reindex()");
        commands.add("  print(\"Success\")");

        joinArraysOnLeft(commands, endCmd);
        return commands.toArray(new String[0]);
    }

    public String[] changeValueCell(final Integer indexRow, final String nameColumn, final String newValue) {
        ArrayList<String> commands = new ArrayList<>();
        joinArraysOnLeft(commands, startCmd);

        String cellCmd = "  df.at[" + indexRow
                + ", "
                + nameColumn
                + "]=\""
                + newValue
                + "\"";
        commands.add(cellCmd);
        commands.add("  print(\"Success\")");

        joinArraysOnLeft(commands, endCmd);
        return commands.toArray(new String[0]);
    }

    public String[] deleteRow(final Integer indexRow) {
        ArrayList<String> commands = new ArrayList<>();
        joinArraysOnLeft(commands, startCmd);

        String deleteCmd = "  df=df.drop([" + indexRow
                + "])";
        commands.add(deleteCmd);
        commands.add("  print(\"Success\")");

        joinArraysOnLeft(commands, endCmd);
        return commands.toArray(new String[0]);
    }


    public String[] saveCSV(String pathFile) {
        ArrayList<String> commands = new ArrayList<>();
        joinArraysOnLeft(commands, startCmd);

        commands.add("  df.to_csv(\"" + pathFile + "\",index = False)");
        commands.add("  print(\"Success\")");

        joinArraysOnLeft(commands, endCmd);
        return commands.toArray(new String[0]);
    }


    public String[] getHeader() {
        ArrayList<String> commands = new ArrayList<>();
        joinArraysOnLeft(commands, startCmd);

        commands.add("  print([_ for _ in df.columns])");

        joinArraysOnLeft(commands, endCmd);
        return commands.toArray(new String[0]);
    }


}

