package mercafacil;

import mercafacil.vista.SupermercadoGUI;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(SupermercadoGUI::new);
    }
}
