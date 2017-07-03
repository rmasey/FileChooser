import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
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

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;

import java.util.ArrayList;         //arrayList store all the University objects
import java.util.Optional;

import java.util.Collections;

public class Main
{
    private static ArrayList<University> uniArrList = new ArrayList<University>();        
    private static ListView<University> uniListView;  
    private static University currentlySelectedUniversity = null;
    private static TextField txtFieldRanking;
    private static TextField txtFieldName;
    private static TextField txtFieldCountry;
    private static TextField txtFieldSearch;
    private static Pane rootPane;
    private static Label labelName;
    private static Label labelCountry;
    private static ImageView iv1 = new ImageView();
    private static Image image;

    public static void main(String args[])
    {               
        getDataFromTextFile();
        launchFX();              
    }

    private static void launchFX()
    {
        // Initialises JavaFX
        new JFXPanel();
        // Runs initialisation on the JavaFX thread
        Platform.runLater(() -> initialiseGUI());
    }

    private static void initialiseGUI() 
    {
        Stage stage = new Stage();
        stage.setTitle("Universities");
        stage.setResizable(false);
        rootPane = new Pane();
        stage.setScene(new Scene(rootPane));                        
        stage.setWidth(1024);
        stage.setHeight(768);
        stage.setOnCloseRequest((WindowEvent we) -> terminate(we));
        stage.show(); 

        labelName = new Label(" ");
        labelName.setLayoutX(350);
        labelName.setLayoutY(200);
        rootPane.getChildren().add(labelName);

        uniListView = new ListView<University>();
        uniListView.setLayoutX(50);
        uniListView.setLayoutY(100);
        uniListView.setOnMouseClicked((MouseEvent me) -> {
                currentlySelectedUniversity = uniListView.getSelectionModel().getSelectedItem();
                display();

            });
        rootPane.getChildren().add(uniListView);

        updateListView();

        Button btn = new Button();
        btn.setText("Add");
        btn.setLayoutX(10);
        btn.setLayoutY(10);
        btn.setOnAction((ActionEvent ae) -> GUI2.initialiseGUI2());
        rootPane.getChildren().add(btn);

        btn = new Button();
        btn.setText("Delete item");
        btn.setLayoutX(100);
        btn.setLayoutY(10);
        btn.setOnAction((ActionEvent ae) -> deleteSelectedItem());
        rootPane.getChildren().add(btn);

        txtFieldSearch = new TextField();
        txtFieldSearch.setLayoutX(10);
        txtFieldSearch.setLayoutY(50);
        txtFieldSearch.setPrefWidth(400);
        txtFieldSearch.setPromptText("Enter search text");
        rootPane.getChildren().add(txtFieldSearch);

        // add an event listener that is called whenever the text inside the textfield changess
        txtFieldSearch.textProperty().addListener((observable, oldValue, newValue) -> {
                String searchText = newValue.toLowerCase(); // or txtFieldSearch.getText();

                uniListView.getItems().clear();

                // loop over each university in the array list
                uniArrList.forEach(university -> {
                        // if it matches our search criteria, add it to the filtered list
                        if(university.toString().toLowerCase().contains(searchText)){
                            uniListView.getItems().add(university);
                        }
                    });
            });

        iv1.setLayoutX(300);
        iv1.setLayoutY(300);
        iv1.setFitWidth(100);
        iv1.setPreserveRatio(true);
        rootPane.getChildren().add(iv1);
    }

    private static void display(){
        labelName.setText(currentlySelectedUniversity.getImageFileName());      //displays the filename

        //load the image
        image = new Image("./Images/" + currentlySelectedUniversity.getImageFileName());
        iv1.setImage(image);
    }

    public static void addItem(University myUni){
        uniArrList.add(myUni);
        updateListView();
    }

    private static void updateListView()        //puts everything from the uniArrayList into the UniListView for the GUI
    {
        uniListView.getItems().clear();
        Collections.sort(uniArrList);    

        for(University uni : uniArrList) {
            uniListView.getItems().add(uni);
        }
    }

    private static void deleteSelectedItem() 
    {
        if (currentlySelectedUniversity == null) return;

        uniArrList.remove(currentlySelectedUniversity);
        updateListView();
        
        //need to also delete the related image
    }

    private static void getDataFromTextFile()       //this method gets the data from the text file and puts it into the uniArrayList
    {
        try
        { 
            FileReader fr = new FileReader("code.txt");     //file included in same folder as project
            BufferedReader br = new BufferedReader(fr);
            String line;

            while ((line = br.readLine()) != null && !line.trim().equals("")) {
                String[] values = line.split(",");  // splits lines by comma into a String array called values
                University uni = new University();      // create a new University object to put the data into
                uni.setRanking(Integer.parseInt(values[0]));  //make the ranking an integer
                uni.setName(values[1]);
                uni.setCountry(values[2]);
                uni.setImageFileName(values[3]);
                uniArrList.add(uni);           //add the object to the arrayList
            }
            br.close();
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }        
    }

    private static void writeDataToTextFile()      
    {
        try
        { 
            FileWriter fw = new FileWriter("code.txt");     //file included in same folder as project
            BufferedWriter bw = new BufferedWriter(fw);

            for(University uni : uniArrList) {
                bw.write(uni + ",");
                bw.newLine();
            }

            bw.close();
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }        
    }

    public static void terminate(WindowEvent we)
    {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Look, a Confirmation Dialog");
        alert.setContentText("Are you sure you want to quit?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            // user chose OK so close the application
            writeDataToTextFile() ;
            System.out.println("bye bye!");
            System.exit(0);
        } else {
            // user chose CANCEL or closed the dialog so do nothing
            we.consume();
        }
    }

}
