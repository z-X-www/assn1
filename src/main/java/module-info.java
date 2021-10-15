module lixinandzhaoxw.assessment1project {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires com.github.librepdf.openpdf;
    requires com.github.librepdf.pdfFontsExtra;
    requires simple.odf;
    requires odfdom.java;
    requires java.xml;
    requires org.apache.pdfbox;
    requires java.desktop;
    requires org.yaml.snakeyaml;
    requires org.fxmisc.richtext;


    opens lixinandzhaoxw.assessment1project to javafx.fxml;
    exports lixinandzhaoxw.assessment1project;
}