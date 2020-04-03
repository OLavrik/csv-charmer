package org.olavrik.charmer.controller;

public class PythonCmd {

    public static String[] getDataFrameSize() {
        return new String[]{"print(df.size)"};
    }

    public static String[] getDataFrame() {
        return new String[]{"print(*(';'.join([str(_) for _ in df.iloc[index].to_list()]) for index in range(len(df))), sep='\\n' )"};
    }

    public static String[] loadDataFrame(String filename) {
        return new String[]{"filename=\"" + filename + "\"", "df=pd.read_csv(filename, sep=',')"};
    }

    public static String[] init() {
        return new String[]{"import pandas as pd", "pd.set_option('display.max_rows', None)", "pd.set_option('display.max_columns', None)"};
    }

    public static String[] getDatFrameSlice(int start, int end) {
        return new String[]{"print(df[" + Integer.toString(start) + ":" + Integer.toString(end) + "])"};
    }

    public static String[] getNumColumns() {
        return new String[]{"print(len(df.columns))"};
    }

    public static String[] getNumRows() {
        return new String[]{"print(len(df)-1)"};
    }

    public static String[] addRow(String rowVal, String indexCSV) {
        Integer indexRow = new Integer(indexCSV);

        String[] rowValues = rowVal.split(", ");
        String commands = new String();

        if (indexRow == -1) {
            // should be string templating elseway you are creating new cope for each + operation
            commands += "df.append([";
            for (int index = 0; index < rowValues.length - 1; index++) {
                commands += "\"" + rowValues[index] + "\", ";
            }
            commands += rowValues[rowValues.length] + "])";

        } else {
            commands += "df=pd.concat([pd.concat([df[:" + indexCSV + "],pd.Series([";
            for (int index = 0; index < rowValues.length - 1; index++) {
                commands += "\"" + rowValues[index] + "\", ";
            }
            commands += rowValues[rowValues.length-1] + "])], ignore_index=True),df[" + indexCSV + ":]],, ignore_index=True)";

        }
        return new String[]{commands};
    }

    public static String[] changeValueCell(String header, Integer rowCount, String newValue) {
        return new String[]{"df.set_value(\"" + rowCount + "\", \"" + header + "\", " + newValue + ")"};
    }

    public static String[] deletRow(String indexRow) {
        return new String[]{"df=df.drop(" + indexRow + ",axis=0)"};
    }

    public static String[] deleteColumn(String columnName) {
        return new String[]{"df=df.drop(\"" + columnName + "\", axis=1)"};
    }


    public static String[] saveCSV(String filename) {
        return new String[]{"df.to_csv(\"" + filename + "\", index = False)"};
    }


    public static String[] getHeader() {
        return new String[]{"print([_ for _ in df.columns])"};
    }


}

