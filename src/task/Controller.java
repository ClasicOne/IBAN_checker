package task;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import nl.garvelink.iban.IBAN;
import nl.garvelink.iban.Modulo97;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.IntStream;

public class Controller implements Initializable {
    @FXML Button checkButton, upload;
    @FXML TextField textFiledInput;
    @FXML Text statusTextField, uploadStatus;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        eventHandler();
    }
    private void eventHandler(){
        checkButton.setOnAction((value) -> {

            String str = textFiledInput.getText();
//            IBAN iban = IBAN.valueOf(str);
            if (str.length() < 6)
                statusTextField.setText("Error");
            try {
                Boolean temp = Modulo97.verifyCheckDigits(str);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
            if (Modulo97.verifyCheckDigits(str)) {
                statusTextField.setText("Numeris teisingas");
            }
            else {
                statusTextField.setText("Numeris neteisingas");
            }
        });
        upload.setOnAction(actionEvent -> {
            File file = fileSelector();
            List<String> strings = new ArrayList<>();
            readFile(file, strings);
            System.out.println("File read");
            IntStream.range(0, strings.size()).forEach(i -> strings.set(i, strings.get(i)+";"+Modulo97.verifyCheckDigits(strings.get(i))));
            System.out.println("File Checked");
//            writeToFile(file, strings);
            try {
                Files.createFile(Path.of(file.getPath().replace("txt","out")));
                Files.write(Path.of(file.getPath().replace("txt","out")),strings, StandardCharsets.UTF_8);
                uploadStatus.setText("File checked");
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("File written");
        });
    }

    private File fileSelector() {
        FileChooser fileChooser =new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        return fileChooser.showOpenDialog(new Stage());
    }

    private void readFile(File file, List<String> strings) {
        try {
            BufferedReader input = new BufferedReader(new FileReader(file));
            try {
                String line = null;
                while ((line = input.readLine()) != null) {
                    strings.add(line);
                }
            } finally {
                input.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


}
