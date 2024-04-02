module ma.enset.projectsma01 {
    requires javafx.controls;
    requires javafx.fxml;
    requires jade;


    opens ma.enset.projectsma01 to javafx.fxml;
    exports ma.enset.projectsma01;
    exports ma.enset.projectsma01.containers;
    exports ma.enset.projectsma01.agents;
}
