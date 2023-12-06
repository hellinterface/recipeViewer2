module com.rvteam.recipeviewer2 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.ikonli.javafx;
    requires java.sql;
    requires org.xerial.sqlitejdbc;

    requires org.jsoup;

    exports com.rvteam.recipeviewer2;
    opens com.rvteam.recipeviewer2 to javafx.fxml;
    exports com.rvteam.recipeviewer2.controls;
    opens com.rvteam.recipeviewer2.controls to javafx.fxml;
    exports com.rvteam.recipeviewer2.navigation;
    opens com.rvteam.recipeviewer2.navigation to javafx.fxml;
    exports com.rvteam.recipeviewer2.data;
    opens com.rvteam.recipeviewer2.data to org.xerial.sqlitejdbc;
    exports com.rvteam.recipeviewer2.foodruParser;
}