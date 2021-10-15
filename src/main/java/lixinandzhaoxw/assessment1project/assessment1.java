package lixinandzhaoxw.assessment1project;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.print.*;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.*;
import javafx.scene.transform.Scale;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import javax.print.*;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import java.awt.image.BufferedImage;
import java.util.*;

import static javafx.application.Platform.exit;
import org.fxmisc.richtext.*;

public class assessment1  implements Initializable{

    @FXML
    private TextArea textarea;

    @FXML
    private MenuBar MenuBar;

    @FXML
    private Menu File;

    @FXML
    private MenuItem Minew;

    @FXML
    private MenuItem Miopen;

    @FXML
    private MenuItem misave;

    @FXML
    private Menu search;

    @FXML
    private MenuItem Misearch;

    @FXML
    private Menu view;

    @FXML
    private Menu manage;

    @FXML
    private MenuItem Miprint;

    @FXML
    private MenuItem Miexit;

    @FXML
    private Menu help;

    @FXML
    private MenuItem Miabout;

    @FXML
    public Label dateandtime;

    @FXML
    private MenuItem MiDT;

    @FXML
    private MenuItem Misaveas;

    @FXML
    private MenuItem Micopy;

    @FXML
    private MenuItem Mipaste;

    @FXML
    private MenuItem Micut;

    @FXML
    private MenuItem mipdf;

    @FXML
    private MenuItem Michange;

    @FXML
    private Label myfont;

    @FXML
    private Label mysize;

    @FXML
    private Label mycolor;

    @FXML
    private Label rowandcol;

    @FXML
    void tonew(ActionEvent event) throws IOException {
        boolean ifIn = false;
//        如果文件路径不为空，在文件内容与textarea内容一致时直接开启新窗口
        if(filepath!=null){
            String file = FileIO.OpenFile(filepath);
            if(file.equals(textarea.getText())){
//                标记值，表示文件已经开启新窗口了，无需再向用户询问
                ifIn = true;
                textarea.clear();
                filepath=null;
            }
        }
//        如果文件路径为空，或者文件内容与textarea内容不一致，则询问用户选择
        if (!ifIn){
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            Label label = new Label("File not save, save it?");
            Button yesBtn = new Button("Save");
            Button noBtn = new Button("Skip");
            yesBtn.setOnAction(e -> {
//                调用tosave方法，防止代码冗余
                try {
                    tosave(e);
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
                textarea.clear();
                filepath = null;
                stage.close();
            });
            noBtn.setOnMouseClicked(e ->{
//                用户不保存时直接清除界面，并初始化文件路径
                textarea.clear();
                filepath = null;
                stage.close();
            });
//            创建新的文本框，提示用户是否保存当前文本
            VBox vBox = new VBox();
            vBox.setPadding(new Insets(10));
            vBox.getChildren().addAll(label,yesBtn,noBtn);
            vBox.setMargin(label, new Insets(5));
            vBox.setMargin(yesBtn, new Insets(5));
            vBox.setMargin(noBtn, new Insets(5));
            vBox.setAlignment(Pos.TOP_CENTER);
            Scene scene = new Scene(vBox, 200, 100);
            stage.setResizable(false);
            stage.setScene(scene);
            stage.setTitle("new");
            stage.showAndWait();
        }

    }

    @FXML
    void toopen(ActionEvent event) throws IOException {
//        let user select their open file
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open");
        fileChooser.initialDirectoryProperty();
        fileChooser.getExtensionFilters().addAll(
//                可选择的文本类型
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("Odt Files", "*.odt"),
                new FileChooser.ExtensionFilter("Java Files", "*.java")

        );
        File file = fileChooser.showOpenDialog(stage);
//        put it into textarea
        if (file != null) {
            String content;
//            确保打开文件后可直接保存，而不是再次让用户搜索路径
            filepath=file.getAbsolutePath();
            if (filepath.substring(filepath.lastIndexOf(".") + 1).equals("odt")){
                content = FileIO.OpenOdtFile(filepath);
            }else{content = FileIO.OpenFile(filepath);}
            textarea.setText(content);
            textarea.positionCaret(1000000000);
        }

    }

    @FXML
    void tosave(ActionEvent event) throws FileNotFoundException {
        if (filepath == null){
//            在没有默认路径时调用tosaveas
            tosaveas(event);
        }
        else {
            String content = textarea.getText();
            FileIO.SaveFile(filepath,content);

        }
    }

    @FXML
    void tosaveas(ActionEvent event) throws FileNotFoundException {
//        创建文件选择器
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save as");
        fileChooser.initialDirectoryProperty();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("Odt Files", "*.odt"),
                new FileChooser.ExtensionFilter("Java Files", "*.java")

        );
        File file = fileChooser.showSaveDialog(stage);
        String content = textarea.getText();
        if (file != null) {
//            文件选择器读取到文件时将文件的绝对路径给予filepath
            filepath = file.getAbsolutePath();
            FileIO.SaveFile(filepath, content);
        }
    }

    @FXML
    void tosearch(ActionEvent event) {

//        display the page of stage
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
//        Setting prompt Message
        Label label= new Label();
        label.setText("Please input what you want to search:");
//          Set count display
        Label show_count = new Label("Count:");
        Label s_C = new Label("0 / 0");
//          next Button
        Button next = new Button("next");
//          the block to Search
        TextField Search_content = new TextField ();
        Button btn1 = new Button("confirm");


        btn1.setOnMouseClicked(event1 ->
        {
//            Determines whether the user enters the search content
            if(Search_content.getText().equals("")){
                Label noput= new Label();
                noput.setText("Please input something!");
                HBox hbox = new HBox();
                hbox.getChildren().add(noput);
                hbox.setAlignment(Pos.CENTER);
                Scene scene = new Scene(hbox, 200, 100);
                stage.setResizable(false);
                stage.setScene(scene);
                stage.setTitle("Put");
                stage.show();
            }else{
//                int count = 0;
                AtomicInteger new_count = new AtomicInteger(1);
                String getsearch = Search_content.getText();
                String gettext = textarea.getText();
                ArrayList<Integer> re = search(gettext,getsearch);

//                Determine whether the text contains the search content
                if(re.get(0) == 0){
                    Label nosear= new Label();
                    nosear.setText("Sorry! I can't find it.");
                    HBox hbox = new HBox();
                    hbox.getChildren().add(nosear);
                    hbox.setAlignment(Pos.CENTER);
                    Scene scene = new Scene(hbox, 200, 100);
                    stage.setResizable(false);
                    stage.setScene(scene);
                    stage.setTitle("No");
                    stage.show();
                }else{
//                    display the first search text and its position
                    s_C.setText(new_count.get() + " / " + re.get(0));
                    textarea.selectRange(re.get(new_count.get()),re.get(new_count.get())+getsearch.length());


//                    int finalCount = count;
                    next.setOnMouseClicked(event2 ->{
//                        Determine if there is only one search item, or if the last search item was reached
                        if(re.get(0) > 1 && new_count.intValue()<re.get(0)){
                            new_count.addAndGet(1);
                            s_C.setText(new_count.get() + " / " + re.get(0));
                            textarea.selectRange(re.get(new_count.get()),re.get(new_count.get())+getsearch.length());
                        }else if(re.get(0) > 1 && new_count.intValue()==re.get(0)){
//                           If the last search item is reached, the next one is the first search item
                            new_count.getAndSet(1);
                            s_C.setText(new_count.get() + " / " + re.get(0));
                            textarea.selectRange(re.get(new_count.get()),re.get(new_count.get())+getsearch.length());
                        }
                    });
                }

            }

        });

        HBox num = new HBox();
        num.getChildren().addAll(show_count,s_C,next);
        num.setMargin(show_count, new Insets(10));
        num.setMargin(s_C, new Insets(10));
        num.setMargin(next, new Insets(7));
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(10));
        vBox.getChildren().addAll(label,Search_content,btn1,num);
        vBox.setMargin(label, new Insets(5));
        vBox.setMargin(Search_content, new Insets(5));
        vBox.setMargin(btn1, new Insets(5));
        num.setAlignment(Pos.BOTTOM_CENTER);
        vBox.setAlignment(Pos.TOP_CENTER);
        Scene scene = new Scene(vBox, 300, 150);
        stage.setMinHeight(180);
        stage.setMinWidth(300);
        stage.setScene(scene);
        stage.setTitle("Search");
        stage.showAndWait();
    }


    ArrayList<Integer> search(String text, String search_t){
        ArrayList<Integer> result = new ArrayList<>();
        if(text.equals("")||search_t.equals("")){
            result.add(0);
            return result;
        }
//        计算有多少的值被搜索到
        int count = 0;
//        复制被搜索语句
        String copytext = text.substring(0,text.length());
//        列表储存所有索引值的位置
        ArrayList<Integer> in_dex = new ArrayList();
        //Returns the starting index of the first occurrence of the specified character getSearch in the string copyText, or -1 if there is no such character.
        while (copytext.indexOf(search_t) != -1) {
            in_dex.add(copytext.indexOf(search_t)+text.length()-copytext.length());
            count++;
            //Reassign the truncated string to copyText
            copytext = copytext.substring(copytext.indexOf(search_t) + search_t.length(), copytext.length());
        }

        result.add(count);
        result.addAll(in_dex);
//return count,indexlist
       return result;
    }

    @FXML
    void toprint(ActionEvent event) throws IOException {
        if (!textarea.getText().equals("")){
            //        生成临时pdf文件用于转化为jpg文件
            File file = new File("./tempPdfForCreatePng.pdf");
            if (!file.exists()){file.createNewFile();}
            String content = textarea.getText();
            if (file != null) {
                try {
                    Document document = new Document();
                    PdfWriter instance = PdfWriter.getInstance(document,
                            new FileOutputStream(file));
                    document.open();
                    BaseFont bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
                    Font font = new Font(bf, (float) textarea.getFont().getSize(), Font.NORMAL);
                    switch (myColor){
                        case "blue":
                            font.setColor(0,0,255);
                            break;
                        case "pink":
                            font.setColor(255,192,202);
                            break;
                        case "green":
                            font.setColor(255,192,202);
                            break;
                        case "black":
                            font.setColor(0,0,0);
                            break;
                        case "yellow":
                            font.setColor(255,255,0);
                            break;
                        case "orange":
                            font.setColor(255,165,0);
                            break;
                    }
                    String[] lines = textarea.getText().split("\n");
                    for (String line : lines) {
                        Paragraph p = new Paragraph("");
                        for (int i = 0; i < line.length(); i++) {
                            char temp = line.charAt(i);
                            p.add(new Chunk(temp, font));
                        }
                        document.add(p);
                    }
                    document.close();

                } catch (DocumentException de) {
                    System.err.println(de.getMessage());
                } catch (IOException ioe) {
                    System.err.println(ioe.getMessage());
                }
            }
//        pdf文件转化并生成临时png文件
            PDDocument tempPdf;
            File pngFile = new File("./tempPngForPrint.png");
            if(!pngFile.exists()){pngFile.createNewFile();}
            try {
                tempPdf = PDDocument.load(file);
                PDFRenderer pdfRenderer = new PDFRenderer(tempPdf);
                BufferedImage img = pdfRenderer.renderImageWithDPI(0, 500);
                ImageIO.write(img, "png", pngFile);
                tempPdf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
//        调用打印机进行打印
            HashPrintRequestAttributeSet Print_ask = new HashPrintRequestAttributeSet();
            DocFlavor docFlavor = DocFlavor.INPUT_STREAM.PNG;
//        查找打印机并设置默认打印机
            PrintService printService[] = PrintServiceLookup.lookupPrintServices(docFlavor, Print_ask);
            PrintService initService = PrintServiceLookup.lookupDefaultPrintService();
//        询问用户
            PrintService toUser = ServiceUI.printDialog(null, 200, 200, printService, initService, docFlavor, Print_ask);
            if (toUser != null) {
                try {
                    DocPrintJob job = toUser.createPrintJob();
                    FileInputStream fileInputStream = new FileInputStream(pngFile);
                    DocAttributeSet docAttributeSet = new HashDocAttributeSet();
                    Doc doc = new SimpleDoc(fileInputStream, docFlavor, docAttributeSet);
                    job.print(doc, Print_ask);
                } catch (PrintException e) {
                    e.printStackTrace();
                }
            }
//        删除临时文件
            if(file.exists()){
                file.delete();
            }
            if(pngFile.exists()){
                pngFile.delete();
            }
        }else{
            //        display the page of stage
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            Label put= new Label();
//            put.setText("Please input something!");
            HBox hbox = new HBox();
//            hbox.getChildren().add(put);
            CodeArea codeArea =new CodeArea();
            hbox.getChildren().add(codeArea);
            hbox.setAlignment(Pos.CENTER);
            Scene scene = new Scene(hbox, 200, 100);
            stage.setResizable(false);
            stage.setScene(scene);
            stage.setTitle("Put");
            stage.show();
        }
    }

    @FXML
    void toexit(ActionEvent event) throws IOException {
        //        If the file path is not empty, close it if the file contents match the textarea contents
        if(filepath!=null){
            String file = FileIO.OpenFile(filepath);
            if(file.equals(textarea.getText())){
//          the file is closed and no longer needs to be queried by the user
                exit();
            }
        }
//        If the file path is empty, or the file content is inconsistent with the textaREA content, the user is asked to choose
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        Label label = new Label("File not save, save it?");
        Button yesBtn = new Button("Save");
        Button noBtn = new Button("Skip");
        yesBtn.setOnAction(e -> {
//                Call tosave method to prevent code redundancy, save and exit
            try {
                tosave(e);
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
            stage.close();
            exit();
        });
        noBtn.setOnMouseClicked(e ->{
//                If the user does not save the file, exit directly
            stage.close();
            exit();
        });
//            Creates a new text box that prompts the user to save the current text
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(10));
        vBox.getChildren().addAll(label,yesBtn,noBtn);
        vBox.setMargin(label, new Insets(5));
        vBox.setMargin(yesBtn, new Insets(5));
        vBox.setMargin(noBtn, new Insets(5));
        vBox.setAlignment(Pos.TOP_CENTER);
        Scene scene = new Scene(vBox, 200, 100);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.setTitle("exit");
        stage.showAndWait();


    }

    @FXML
    void toabout(ActionEvent event) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);

//        Set all labels
        Label label= new Label();
        Label label1 = new Label();
        Label label2 = new Label();
        Label label3 = new Label();
        Label label4 = new Label();

//        Set all content for labels
        label.setText("Members:");
        label1.setText("Zhaoxiang Wang, 20007898");
        label2.setText("Xin Li, 20008022");
        label3.setText("Starting time:");
        label4.setText("2021/09/18");
        Button btn1 = new Button("confirm");
//        close the stage
        btn1.setOnMouseClicked(event1 ->
        {
            stage.close();
        });

        HBox hbox1 = new HBox();
        HBox hbox2 = new HBox();
        VBox vBox = new VBox();
        VBox vbox1 = new VBox();
//        layout for names sand ids of both team members
        vbox1.getChildren().addAll(label1,label2);
        hbox1.getChildren().addAll(label,vbox1);
//        layout for starting time
        hbox2.getChildren().addAll(label3,label4);
//        layout for all things in stage
        vBox.getChildren().addAll(hbox1,hbox2,btn1);
        vbox1.setSpacing(5);
        hbox1.setSpacing(5);
        hbox2.setSpacing(5);
        vBox.setSpacing(5);
        vBox.setPadding(new Insets(10));
        vBox.setAlignment(Pos.CENTER);
        Scene scene = new Scene(vBox, 250, 120);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.setTitle("About");
        stage.showAndWait();
    }

    @FXML
    void tocopy(ActionEvent event) {
        String a = textarea.getSelectedText();
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent clipboardContent = new ClipboardContent();
        clipboardContent.putString(a);
        clipboard.setContent(clipboardContent);
    }

    @FXML
    void tocut(ActionEvent event) {
        tocopy(event);
        textarea.deleteText(textarea.getSelection());
    }

    @FXML
    void topaste(ActionEvent event) {
        String con = Clipboard.getSystemClipboard().getString();
        textarea.deleteText(textarea.getSelection());
        textarea.insertText(textarea.getSelection().getEnd(),con);

    }

    @FXML
    void topdf(ActionEvent event) {
        if(!textarea.getText().equals("")) {
            String pdfpath = null;
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Create PDF to");
            fileChooser.initialDirectoryProperty();
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("pdf Files", "*.pdf")
            );
            File file = fileChooser.showSaveDialog(stage);
            String content = textarea.getText();
            if (file != null) {
                try {
                    Document document = new Document();
                    PdfWriter instance = PdfWriter.getInstance(document,
                            new FileOutputStream(file));
                    document.open();
                    BaseFont bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
                    Font font = new Font(bf, (float) textarea.getFont().getSize(), Font.NORMAL);
                    switch (myColor){
                        case "blue":
                            font.setColor(0,0,255);
                            break;
                        case "pink":
                            font.setColor(255,192,202);
                            break;
                        case "green":
                            font.setColor(255,192,202);
                            break;
                        case "black":
                            font.setColor(0,0,0);
                            break;
                        case "yellow":
                            font.setColor(255,255,0);
                            break;
                        case "orange":
                            font.setColor(255,165,0);
                            break;
                    }
                    String[] lines = textarea.getText().split("\n");
                    for (String line : lines) {
                        Paragraph p = new Paragraph("");
                        for (int i = 0; i < line.length(); i++) {
                            char temp = line.charAt(i);
                            p.add(new Chunk(temp, font));
                        }
                        document.add(p);
                    }
                    document.close();

                } catch (DocumentException de) {
                    System.err.println(de.getMessage());
                } catch (IOException ioe) {
                    System.err.println(ioe.getMessage());
                }
            }
        }else{
            //        display the page of stage
//            to inform users to input something to pdf
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            Label put= new Label();
            put.setText("Please input something!");
            HBox hbox = new HBox();
            hbox.getChildren().add(put);
            hbox.setAlignment(Pos.CENTER);
            Scene scene = new Scene(hbox, 200, 100);
            stage.setResizable(false);
            stage.setScene(scene);
            stage.setTitle("Put");
            stage.show();
        }

    }



    @FXML
    void tomidt(ActionEvent event) {
        String dt = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss").format(new Date());
        int a = textarea.getCaretPosition();
        textarea.insertText(a,dt);

    }

    @FXML
    void tochange(ActionEvent event) {
        double size = textarea.getFont().getSize();
        Map<String, ArrayList> colorMap = Readyaml.readYaml();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
//        create Font module
        VBox vboxFont = createvBox(colorMap, "Font");
        ListView Fontview = (ListView) vboxFont.getChildren().get(2);

//        create Color module
        VBox vboxColor = createvBox(colorMap, "Color");
        ListView Colorview = (ListView) vboxColor.getChildren().get(2);

//        create Size module
        VBox vboxSize = createvBox(colorMap, "Size");
        ListView Sizeview = (ListView) vboxSize.getChildren().get(2);

        Button confirmBTN = new Button("confirm");
        confirmBTN.setOnMouseClicked(e ->{
            String tempFont = (String) Fontview.getSelectionModel().getSelectedItem();
            String tempColor = (String) Colorview.getSelectionModel().getSelectedItem();
            String tempSize = (String) Sizeview.getSelectionModel().getSelectedItem();
            if(tempFont != null){
                myFont = "src\\main\\resources\\Font\\" + tempFont + ".ttf";
            }
            if(tempColor != null){
                myColor = tempColor;
            }
            if(tempSize != null){
                mySize = tempSize;
            }
            try (FileInputStream in = new FileInputStream(new File(myFont))){
                javafx.scene.text.Font font = javafx.scene.text.Font.loadFont(in, Integer.valueOf(mySize));
                textarea.setFont(font);
                textarea.setStyle("-fx-text-fill:" + myColor);
                myfont.setText("Font: " + myFont.substring(myFont.lastIndexOf("\\") + 1,myFont.lastIndexOf(".")));
                mysize.setText("Font-size: " + mySize);
                mycolor.setText("Font-color: " + myColor);
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            stage.close();
        });
        HBox topbox = new HBox();
        topbox.getChildren().addAll(vboxFont, vboxColor, vboxSize);
        VBox mainbox = new VBox();
        mainbox.getChildren().addAll(topbox, confirmBTN);
        mainbox.setAlignment(Pos.CENTER);
        Scene scene = new Scene(mainbox, 420, 200);
        stage.setScene(scene);
        stage.setTitle("Change Type");
        stage.showAndWait();
    }

//    Time and Date (T&D): retrieve the current time and data from the OS and
//                         place it at the top of the page of the editor.

//    Add public value for save address.
    public String filepath = null;
//    设置默认字体，颜色和字号
    public String myFont = null;
    public String mySize = null;
    public String myColor = null;

    public VBox createvBox(Map<String, ArrayList> map, String type){
//        设置组件整体的padding和部分的初始宽高
        VBox vboxType = new VBox();
        vboxType.setPadding(new Insets(10, 20, 20, 20));
        Label TypeLabel = new Label(type + ":");
        TypeLabel.setPrefSize(100, 10);
        TextField TypeField = new TextField();
        TypeField.setPrefSize(100, 20);
        ListView TypeView = new ListView();
        TypeView.setPrefSize(100, 80);
        ObservableList<String> Typelist = FXCollections.observableArrayList();
        TypeView.setEditable(false);
        for(Object Typename :  map.get(type)) {
            Typelist.add((String)Typename);
        }
        TypeView.setItems(Typelist);
//        监听列表的选择事件，使用户能点击列表时能在文本框中显示相应内容

        TypeView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
                @Override
                public void changed(ObservableValue observableValue, Object o, Object t1) {
                    TypeField.setText((String) TypeView.getSelectionModel().getSelectedItem());
                }
            }
        );

//        监听输入框事件，使用户能根据输入定位到列表的相应内容
        TypeField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if (t1 != null){
                    for(String type: Typelist){
                        if(type.contains(t1) && !t1.equals("")){
                            TypeView.scrollTo(Typelist.indexOf(type));
                            break;
                        }
                    }
                }
            }
        });

        vboxType.getChildren().addAll(TypeLabel, TypeField, TypeView);
        return vboxType;
    }

    int getrow(String text, int index_num){
        String new_text = text.substring(0,index_num);
        String copy_t = new_text.replaceAll("\n","");
        return new_text.length() - copy_t.length() + 1;
    }

    int getcol(String text, int index_num){
        String new_text = text.substring(0,index_num);
        String newer = text.substring(new_text.lastIndexOf("\n")+1,index_num);
        return newer.length()+1;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        DateFormat df = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss");
        //Implementation of some real-time features
        EventHandler<ActionEvent> eventHandler = e -> {
        //Check whether there is pasted content in real time
            if (Clipboard.getSystemClipboard().getString() != null){
                if (!Clipboard.getSystemClipboard().getString().equals("")){
                    Mipaste.setDisable(false);
                }
            }else {
                Mipaste.setDisable(true);
            }

            //Determines if any selected content can be cut and copied
            if(textarea.getSelectedText().equals("")){
                Micopy.setDisable(true);
                Micut.setDisable(true);
            }else{
                Micopy.setDisable(false);
                Micut.setDisable(false);
            }
        //Real-time update time and number of rows and columns
            dateandtime.setText(df.format(new Date()));
            rowandcol.setText("Lines: "+getrow(textarea.getText(),textarea.getCaretPosition())+ ","+" Columns: " + getcol(textarea.getText(),textarea.getCaretPosition()));
        };
        Timeline animation = new Timeline(new KeyFrame(Duration.millis(1000), eventHandler));
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.play();
        myFont = "src\\main\\resources\\Font\\simhei.ttf";
        mySize = "14";
        myColor = "black";
        myfont.setText("Font: " + myFont.substring(myFont.lastIndexOf("\\") + 1,myFont.lastIndexOf(".")));
        mysize.setText("Font-size: " + mySize);
        mycolor.setText("Font-color: " + myColor);
        try (FileInputStream in = new FileInputStream(new File(myFont))){
            javafx.scene.text.Font font = javafx.scene.text.Font.loadFont(in, Integer.valueOf(mySize));
            textarea.setFont(font);
            textarea.setStyle("-fx-text-fill:black");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
