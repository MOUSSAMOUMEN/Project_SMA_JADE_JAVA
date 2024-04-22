package ma.enset.projectsma01.agents;

import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;

import java.util.Random;

public class VendeurAgent extends GuiAgent {

    //reference vers l'interface VendeurGui
    protected VendeurGui gui;

    @Override
    protected void setup() {
        if(getArguments().length==1){
            gui= (VendeurGui) getArguments()[0];
            gui.vendeurAgent=this;
        }
        //le comportement des agents vendeurs
        ParallelBehaviour parallelBehaviour=new ParallelBehaviour();
        addBehaviour(parallelBehaviour);

        //pour faire l'operation une seul fois
        parallelBehaviour.addSubBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                DFAgentDescription agentDescription=new DFAgentDescription();
                agentDescription.setName(getAID());
                // cree un service
                ServiceDescription serviceDescription=new ServiceDescription();
                serviceDescription.setType("transaction");
                serviceDescription.setName("vente-livres");
                //ajouter le service au description
                agentDescription.addServices(serviceDescription);

                try {
                    DFService.register(myAgent,agentDescription);
                } catch (FIPAException e) {
                    e.printStackTrace();
                }}
        });

        //Entrer dans un boucle loop
        parallelBehaviour.addSubBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                // pour que l'agent  attend la reception du message en utilise un event loop (boucle infini)
                ACLMessage aclMessage=receive();
                if(aclMessage!=null){
                    gui.logMessage(aclMessage);

                    switch(aclMessage.getPerformative()){
                        case ACLMessage.CFP :
                            ACLMessage reply=aclMessage.createReply();
                            reply.setPerformative(ACLMessage.PROPOSE);
                            reply.setContent(String.valueOf(500+new Random().nextInt(1000)));
                            send(reply);
                            break;

                        case ACLMessage.ACCEPT_PROPOSAL :
                            ACLMessage aclMessage1=aclMessage.createReply();
                            aclMessage1.setPerformative(ACLMessage.AGREE);
                            aclMessage1.setContent(aclMessage.getContent());
                            send(aclMessage1);
                            break;
                        }
                }else block();}});}

    @Override
    protected void onGuiEvent(GuiEvent event) {}

    //il faut supprimer les services de l'agent lorsque ce dernieur est detruit
    @Override
    protected void takeDown() {
        try {
            DFService.deregister(this);
        } catch (FIPAException e) {
            e.printStackTrace();
        }}}
