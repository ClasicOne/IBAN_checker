package task;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import nl.garvelink.iban.Modulo97;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.IntStream;

public class Controller implements Initializable {
    @FXML
    Button checkButton, upload, clear;
    @FXML
    TextField textFiledInput;
    @FXML
    Text statusTextField, uploadStatus, fileName;

    File file;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        eventHandler();
    }
    private void eventHandler(){
        //Tikrina ar IBAN atitinka reikalavimus/*
        // Jai per mazai elentu ivesta rasys klaida
        // */
        checkButton.setOnAction((value) -> {

            String str = textFiledInput.getText();
            if (str.length() < 6)
                statusTextField.setText("Error");

            if (Modulo97.verifyCheckDigits(str))
                statusTextField.setText("Valid");
            else statusTextField.setText("Invalid");
        });
        upload.setOnAction(actionEvent -> {
            file = fileSelector();
            List<String> strings = new ArrayList<>();
            readFile(file, strings);
            fileName.setText(file.getName());
            System.out.println("File read");
            //Prideda true false prie kiekvienos eilutes
            IntStream.range(0, strings.size()).forEach(i -> strings.set(i, strings.get(i)+";"+Modulo97.verifyCheckDigits(strings.get(i))));
            System.out.println("File Checked");
            newFileAndWrite(file, strings);
            System.out.println("File written");
        });
        clear.setOnAction(event -> {
            file = null;
            fileName.setText("");
            uploadStatus.setText("Upload file");
        });
    }
    //sukuria nauja faila ir ji uzpildo toje pacioje vietoje kur pasirinktas failas yra
    private void newFileAndWrite(File file, List<String> strings) {
        try {
            try {
                Files.createFile(Paths.get(file.getPath().replace("txt","out")));
            } catch (FileAlreadyExistsException e) {
                uploadStatus.setText("File already checked");
                e.printStackTrace();
            }
            Files.write(Paths.get(file.getPath().replace("txt", "out")),strings, StandardCharsets.UTF_8);
            uploadStatus.setText("File checked");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // Atidaro failu pasirinkimo lentele
    private File fileSelector() {
        FileChooser fileChooser =new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        return fileChooser.showOpenDialog(new Stage());
    }
    //nuskaito pasirinkta faila ir ji sudeda i list
    private void readFile(File file, List<String> strings) {
        try {
            BufferedReader input = new BufferedReader(new FileReader(file));
            try {
                String line = null;
                while ((line = input.readLine()) != null) strings.add(line);
            } finally {
                input.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


}
