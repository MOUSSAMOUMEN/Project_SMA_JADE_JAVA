package ma.enset.projectsma01.containers;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
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
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import ma.enset.projectsma01.agents.ConsumerAgent;


public class ConsumerContainer extends Application {

    //reference veres l'agent Consommateur
    protected ConsumerAgent consumerAgent;
     ObservableList<String> observableList;

    public static void main(String[] args)  {
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {
        // Appel à la méthode startContainer() pour créer le conteneur d'agent
        startContainer();
        HBox hBox = new HBox();
        hBox.setPadding(new Insets(10));
        hBox.setSpacing(20);
        Label label = new Label("Livre :");
        label.setStyle("-fx-font-size: 14pt; -fx-text-fill: #333333;");
        TextField textFieldLivre = new TextField();
        textFieldLivre.setStyle("-fx-font-size: 14pt;");
        Button buttonAcheter = new Button("Acheter");
        buttonAcheter.setStyle("-fx-font-size: 14pt; -fx-background-color: #4CAF50; -fx-text-fill: white;"); // Style du bouton
        hBox.getChildren().addAll(label, textFieldLivre, buttonAcheter);

        VBox vBox = new VBox();
        vBox.setPadding(new Insets(10));
        observableList= FXCollections.observableArrayList();
        ListView<String> listViewMessage = new ListView<>(observableList);
        vBox.getChildren().add(listViewMessage);

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(hBox);
        borderPane.setCenter(vBox);


        borderPane.setStyle("-fx-background-color: #f0f0f0;");
        primaryStage.setTitle("INTERFACE CONSOMMATEUR AGENT");

        Scene scene = new Scene(borderPane, 600, 400);
        scene.setFill(Color.rgb(240, 240, 240));
        scene.setFill(Color.LIGHTBLUE);

        primaryStage.setScene(scene);
        primaryStage.show();

       buttonAcheter.setOnAction(evt->{
           String livre=textFieldLivre.getText();
           //observableList.add(livre);
           GuiEvent event=new GuiEvent(this,1);
           event.addParameter(livre);
           consumerAgent.onGuiEvent(event);
       });}
    public void startContainer() throws Exception {
        Runtime runtime = Runtime.instance();
        ProfileImpl profile = new ProfileImpl();
        profile.setParameter(ProfileImpl.MAIN_HOST, "localhost");
        AgentContainer container = runtime.createAgentContainer(profile);
        AgentController agentController = container.
               createNewAgent("consumer", "ma.enset.projectsma01.agents.ConsumerAgent", new Object[]{this});
       container.start();
       agentController.start();}

    public void setConsumerAgent(ConsumerAgent consumerAgent) {
        this.consumerAgent = consumerAgent;
    }
    public void logMessage(ACLMessage aclMessage){
        Platform.runLater(()->{
            observableList.add(aclMessage.getContent()
                    +", " +aclMessage.getSender().getName()
            );});}}