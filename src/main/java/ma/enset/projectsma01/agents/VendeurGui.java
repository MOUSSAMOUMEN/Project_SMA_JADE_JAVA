package ma.enset.projectsma01.agents;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class VendeurGui extends Application {

     //reference vers l'agent vendeur
    protected VendeurAgent vendeurAgent;
    protected ObservableList<String> observableList;
    protected AgentContainer agentContainer;

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage stage) throws Exception {
        startContainer();
        BorderPane borderPane = new BorderPane();
        borderPane.setStyle("-fx-background-color: #f0f0f0;"); // Style du BorderPane

        HBox hBox = new HBox();
        hBox.setPadding(new Insets(10));
        hBox.setSpacing(10);

        Label label = new Label("Nom de l'Agent :");
        TextField textFieldAgentName = new TextField();
        Button buttonDeploy = new Button("Déployer");

        hBox.setStyle("-fx-padding: 10; -fx-spacing: 10;");

        label.setStyle("-fx-font-size: 14pt; -fx-font-weight: bold;");

        textFieldAgentName.setStyle("-fx-font-size: 14pt; -fx-pref-width: 200px;");

        buttonDeploy.setStyle("-fx-font-size: 14pt; -fx-pref-width: 100px; -fx-background-color: #4CAF50; -fx-text-fill: white;");

        hBox.getChildren().addAll(label, textFieldAgentName, buttonDeploy);
        VBox vBox = new VBox();
        observableList = FXCollections.observableArrayList();
        ListView<String> listView = new ListView<>(observableList);
        vBox.getChildren().add(listView);

        vBox.setStyle("-fx-padding: 10; -fx-spacing: 10;");

        borderPane.setCenter(vBox);
        borderPane.setTop(hBox);

        Scene scene = new Scene(borderPane, 500, 400);
        stage.setTitle("INTERFACE VENDEUR AGENT");
        stage.setScene(scene);
        stage.show();

        // pour deployer un agent
        buttonDeploy.setOnAction((evt)->{

            String nom=textFieldAgentName.getText();
            try {
                AgentController agentController=agentContainer.createNewAgent(nom,
                        "ma.enset.projectsma01.agents.VendeurAgent",new Object[]{this});
                agentController.start();
            } catch (StaleProxyException e) {
                e.printStackTrace();
            }
        });

    }

    private void startContainer() throws Exception {

        Runtime runtime=Runtime.instance();
        ProfileImpl profile=new ProfileImpl();
        profile.setParameter(ProfileImpl.MAIN_HOST,"localhost");
        agentContainer=runtime.createAgentContainer(profile);
        agentContainer.start();

    }

    public void logMessage(ACLMessage aclMessage){

        Platform.runLater(()->{
            observableList.add(aclMessage.getContent()
                    +", " +aclMessage.getSender());
        });}}
