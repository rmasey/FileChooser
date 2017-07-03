import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.WindowEvent;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;         //arrayList store all the University objects
import java.util.Optional;

import java.util.Collections;

import javafx.stage.FileChooser;
import java.io.File;
import java.nio.file.Files;
import static java.nio.file.StandardCopyOption.*;
import java.io.*;
import java.nio.file.*;

import javafx.stage.FileChooser;

public class GUI2
{
    private static TextField txtFieldRanking;
    private static TextField txtFieldName;
    private static TextField txtFieldCountry;
    private static Pane rootPane;
    private static String fileName;

    public static void initialiseGUI2() 
    {
        Stage stage = new Stage();
        stage.setTitle("Universities");
        stage.setResizable(false);
        rootPane = new Pane();
        stage.setScene(new Scene(rootPane));                        
        stage.setWidth(1024);
        stage.setHeight(768);
        stage.setOnCloseRequest((WindowEvent we) -> terminate(we, stage));
        stage.show(); 

        Label label = new Label("Enter ranking");
        label.setLayoutX(350);
        label.setLayoutY(150);
        rootPane.getChildren().add(label);

        txtFieldRanking = new TextField();
        txtFieldRanking.setLayoutX(400);
        txtFieldRanking.setLayoutY(200);
        txtFieldRanking.setPromptText("Enter University ranking");
        rootPane.getChildren().add(txtFieldRanking);

        txtFieldName = new TextField();
        txtFieldName.setLayoutX(400);
        txtFieldName.setLayoutY(250);
        txtFieldName.setPrefWidth(400);
        txtFieldName.setPromptText("Enter name for University");
        rootPane.getChildren().add(txtFieldName);

        txtFieldCountry = new TextField();
        txtFieldCountry.setLayoutX(400);
        txtFieldCountry.setLayoutY(300);
        txtFieldCountry.setPromptText("Enter country");
        rootPane.getChildren().add(txtFieldCountry);

        Button btn = new Button();
        btn.setText("Add new item");
        btn.setLayoutX(350);
        btn.setLayoutY(50);
        btn.setOnAction((ActionEvent ae) -> addNewItem(stage));
        rootPane.getChildren().add(btn);

        Button buttonLoad = new Button("Load Image");
        buttonLoad.setOnAction((ActionEvent ae) -> getImage(stage));
        rootPane.getChildren().add(buttonLoad);
    }

    public static void terminate(WindowEvent we, Stage stage)
    {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Look, a Confirmation Dialog");
        alert.setContentText("Are you sure you want to quit?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            // user chose OK so close the application
            stage.close();
        } else {
            // user chose CANCEL or closed the dialog so do nothing
            we.consume();
        }
    }

    private static void addNewItem(Stage stage) {
        stage.close();  //closes the pop up windows

        int ranking = Integer.parseInt(txtFieldRanking.getText());   
        String name = txtFieldName.getText();  
        String country = txtFieldCountry.getText();  
        String imageFileName = fileName;

        University newUni = new University(ranking, name, country, imageFileName);

        Main.addItem(newUni);

    }

    private static void getImage(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        //FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        // fileChooser.getExtensionFilters().add(extFilter);
        File sourceFile = fileChooser.showOpenDialog(stage);
        Path source = Paths.get(sourceFile.getAbsolutePath());  //full path to selected image

        System.out.println(source);                             //printed for debugging

        String stringPath = source.toString();                  //convert path to string to get file name only
        int position = stringPath.lastIndexOf("\\");            //find last \ to find last part which is file name
        fileName = stringPath.substring(position+1);
        System.out.println(fileName);   

        Path destination = Paths.get("./Images/" + fileName);  //.  is the current working directory 

        try {
            Files.copy(source, destination, REPLACE_EXISTING);
        } catch (IOException ex) {
            System.err.format("I/O Error when copying file");
        }
    }
}