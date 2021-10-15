package lixinandzhaoxw.assessment1project;
import org.odftoolkit.odfdom.doc.OdfTextDocument;
import org.odftoolkit.odfdom.dom.element.office.OfficeTextElement;
import org.odftoolkit.odfdom.dom.element.text.TextAElement;
import org.odftoolkit.odfdom.dom.element.text.TextSElement;
import org.odftoolkit.odfdom.dom.element.text.TextSpanElement;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.*;

public class FileIO {
    public static void SaveFile(String filepath, String content) throws FileNotFoundException {
        try {
            File file = new File(filepath);
            if (!file.exists()) {
                file.createNewFile();
            }
            BufferedWriter output = new BufferedWriter(new FileWriter(file));
            output.write(content);
            output.close();
        } catch (Exception e){
            e.printStackTrace();
        }

    }
    public static String OpenFile(String filepath) throws IOException {
        File file = new File(filepath);
        String end = "";
        try {
            BufferedReader input = new BufferedReader(new FileReader(file));
            String temp = null;
//            Use readLine to read file by line, and add them all.
            temp = input.readLine();
            if(temp != null) {
                end = temp;
            }
            while ((temp = input.readLine()) != null){
                end = end + "\n" + temp;
            }
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return end;
    }
    public static String OpenOdtFile(String filepath) {
        File file = new File(filepath);
        try {
            InputStream is = new FileInputStream(file);
            OdfTextDocument odtDocument = OdfTextDocument.loadDocument(is);
            OfficeTextElement odtNode = odtDocument.getContentRoot();
//            there are some things wrong in getContentRoot().getTextContent() (it can`t read NodeName"text:p"), so I build a new method for avoid this situation
            String odtContent = myReadNode(odtNode);
            is.close();
            return odtContent;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    private static String myReadNode(OfficeTextElement OTE){
        StringBuilder buffer = new StringBuilder();
        NodeList nodeList = OTE.getChildNodes();
//        the first node(always is text:sequence-decls) in OTE is useless, so I read it start at the second node.
        for (int i = 1; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            NodeList subNodeList = node.getChildNodes();
//            if a there are some nodes in this node, it goes to the loop below, which is build on code in getContentRoot().getTextContent();
            for (int j = 0; j < subNodeList.getLength(); j++) {
                Node tempNOde = subNodeList.item(j);
                if (tempNOde.getNodeType() == 3) {
                    buffer.append(tempNOde.getNodeValue());
                } else if (tempNOde.getNodeType() == 1) {
                    if (tempNOde instanceof TextSpanElement) {
                        buffer.append(((TextSpanElement)tempNOde).getTextContent());
                    } else if (tempNOde.getNodeName().equals("text:s")) {
                        Integer count = ((TextSElement)tempNOde).getTextCAttribute();

                        for(int k = 0; k < (count != null ? count : 1); ++k) {
                            buffer.append(' ');
                        }
                    } else if (tempNOde.getNodeName().equals("text:tab")) {
                        buffer.append('\t');
                    } else if (tempNOde.getNodeName().equals("text:line-break")) {
                        String lineseperator = System.getProperty("line.separator");
                        buffer.append(lineseperator);
                    } else if (tempNOde.getNodeName().equals("text:a")) {
                        buffer.append(((TextAElement)tempNOde).getTextContent());
                    } else if (tempNOde.getNodeName().equals("#text")) {
                        buffer.append(((TextAElement)tempNOde).getTextContent());
                    }
                }
            }
//            In common, a new p or h text mark there is a new line in .odt document. But if a .odt document just have one line, the content in the line will put in a p node, but it should not warp around.
            if (nodeList.getLength() > 2) {
                buffer.append("\n");
            }
        }
        return buffer.toString();
    }

}
