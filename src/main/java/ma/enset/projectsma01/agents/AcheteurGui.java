package ma.enset.projectsma01.agents;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AcheteurGui extends Application {

     //reference vers l'agent
    protected AcheteurAgent acheteurAgent;
    protected ObservableList<String> observableList;

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage stage) throws Exception {
        startContainer();

        BorderPane borderPane = new BorderPane();
        borderPane.setStyle("-fx-background-color: #f0f0f0;");

        VBox vBox = new VBox();
        vBox.setStyle("-fx-padding: 10; -fx-spacing: 10;");

        observableList = FXCollections.observableArrayList();
        ListView<String> listView = new ListView<>(observableList);
        listView.setStyle("-fx-font-size: 14pt; -fx-pref-width: 400px; -fx-pref-height: 300px;"); // Style du ListView

        vBox.getChildren().add(listView);
        borderPane.setCenter(vBox);

        Scene scene = new Scene(borderPane, 500, 400);
        stage.setTitle("INTERFACE ACHETEUR AGENT");
        stage.setScene(scene);
        stage.show();

    }

    private void startContainer() throws Exception {

        Runtime runtime=Runtime.instance();
        ProfileImpl profile=new ProfileImpl();
        profile.setParameter(ProfileImpl.MAIN_HOST,"localhost");
        AgentContainer container=runtime.createAgentContainer(profile);
        AgentController agentController=container.createNewAgent("ACHETEUR",
                "ma.enset.projectsma01.agents.AcheteurAgent",new Object[]{this});
        agentController.start();
        container.start();

    }

    public void logMessage(ACLMessage aclMessage){

        Platform.runLater(()->{
            observableList.add(aclMessage.getContent()
                    +", " +aclMessage.getSender());
        });}}
