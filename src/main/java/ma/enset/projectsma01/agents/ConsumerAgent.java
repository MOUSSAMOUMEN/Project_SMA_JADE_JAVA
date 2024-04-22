package ma.enset.projectsma01.agents;


import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import ma.enset.projectsma01.containers.ConsumerContainer;


public class ConsumerAgent extends GuiAgent {

    //reference vers linterface graphique
    protected transient ConsumerContainer gui; //c est a dire que l'interface n est pas serialisable
    protected void setup(){
        if(getArguments().length==1){
            gui= (ConsumerContainer) getArguments()[0];
            gui.setConsumerAgent(this);
        }
        ParallelBehaviour parallelBehaviour=new ParallelBehaviour();
        addBehaviour(parallelBehaviour);
        parallelBehaviour.addSubBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {

                ACLMessage aclMessage=receive();
                if (aclMessage != null) {
                    switch (aclMessage.getPerformative()){

                        case ACLMessage.INFORM:
                            gui.logMessage(aclMessage);
                            break;

                        default:
                            break;}

                } else block();}});}

    @Override
    public void onGuiEvent(GuiEvent params) {
      if(params.getType()==1){
          String livre =  params.getParameter(0).toString();
          ACLMessage aclMessage=new ACLMessage(ACLMessage.REQUEST);
          aclMessage.setContent(livre);
          aclMessage.addReceiver(new AID("ACHETEUR",AID.ISLOCALNAME)); // ajouter le receiver de message
          send(aclMessage);}}}
