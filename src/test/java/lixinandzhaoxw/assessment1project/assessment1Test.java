package lixinandzhaoxw.assessment1project;

import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class assessment1Test {

    @Test
    public void search() {
        assessment1 as= new assessment1();
        String text = "Zhaoxiang Wang and Lixin, 20007898 and 20008022, start in 2021/09/18;";
        Integer[] expect1 = {4,6,11,15,35};
        Integer[] expect2 = {1,8};
        Integer[] expect3 = {0};
        assertArrayEquals(expect1, as.search(text,"an").toArray());
        assertArrayEquals(expect2,as.search(text,"g W").toArray());
        assertArrayEquals(expect3,as.search(text,"q").toArray());
        assertArrayEquals(expect3,as.search(text,"").toArray());
    }

    @Test
    public void toopen() throws IOException {
        String odtReader = FileIO.OpenOdtFile("./test.odt");
        String odtContent = "\tAbc\n" +
                "\t123\n" +
                "\t中文\n" +
                "\t  1\n";
        assertTrue(odtReader.equals(odtContent));
        
    }

    @Test
    public void tosave() throws IOException {
        String content = "\n abc \twds完整性77";
        FileIO.SaveFile("./savetest.txt", content);
        File file = new File("./savetest.txt");
        assertTrue(file.exists());
        assertTrue(content.equals(FileIO.OpenFile("./savetest.txt")));
    }
    @After
    public void after(){
        File file = new File("./savetest.txt");
        if(file.exists()){
            file.delete();
        }
    }
}