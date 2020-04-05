package org.olavrik.charmer;

import org.junit.*;
import org.olavrik.charmer.controller.PythonCmd;

import java.util.ArrayList;

import static junit.framework.TestCase.assertEquals;

public class PythonCmdTest {
    private PythonCmd pythonCmd;

    @Test
    public void checkCmdLoad(){
        String fileName="test.test";
        String[] actual=pythonCmd.loadDataFrame(fileName);
        assertEquals("filename=\"test.test\"", actual[0]);
        assertEquals("df=pd.read_csv(filename)", actual[1]);
    }

    @Test
    public void checkCmdSlice(){
        String[] actual=pythonCmd.getDatFrameSlice(2,3);
        assertEquals("print(df[2:3])", actual[0]);
    }

}
