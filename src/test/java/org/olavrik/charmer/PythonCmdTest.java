package org.olavrik.charmer;

import org.junit.Test;
import org.olavrik.charmer.controller.PythonCmd;

import static junit.framework.TestCase.assertEquals;

public class PythonCmdTest {
    private final PythonCmd pythonCmd = new PythonCmd();

    @Test
    public void checkCmdLoad() {
        String fileName = "test.test";
        String[] actual = pythonCmd.loadDataFrame(fileName);
        assertEquals("  filename=\"test.test\"", actual[1]);
        assertEquals("  df=pd.read_csv(filename)", actual[2]);
    }

    @Test
    public void checkCmdSlice() {
        String[] actual = pythonCmd.getDatFrameSlice(2, 3);
        assertEquals("  print(df[2:3])", actual[1]);
    }

}
