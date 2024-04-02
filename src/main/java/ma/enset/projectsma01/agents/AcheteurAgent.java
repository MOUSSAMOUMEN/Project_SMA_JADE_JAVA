package ma.enset.projectsma01.agents;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.ArrayList;
import java.util.List;

public class AcheteurAgent extends GuiAgent {
    //reference vers l'interface graphique
    protected AcheteurGui  gui;

    //declarer un tableau des vendeurs (agents vendeurs)
    protected AID[] vendeurs;
    protected void setup(){

        if(getArguments().length==1){
            gui= (AcheteurGui) getArguments()[0];
            gui.acheteurAgent=this;
        }
        //le comportement de l'agent ACHETEUR
        ParallelBehaviour parallelBehaviour=new ParallelBehaviour();
        addBehaviour(parallelBehaviour);

        parallelBehaviour.addSubBehaviour(new TickerBehaviour(this,5000) {
            @Override
            protected void onTick() {
                DFAgentDescription dfAgentDescription=new DFAgentDescription();
                ServiceDescription serviceDescription=new ServiceDescription();
                serviceDescription.setType("transaction");
                serviceDescription.setName("vente-livres");
                dfAgentDescription.addServices(serviceDescription);

                try {
                    DFAgentDescription[] results=
                            DFService.search(myAgent,dfAgentDescription);
                    //recuperer la liste des vendeurs
                    vendeurs=new AID[results.length];
                    for(int i=0;i<vendeurs.length;i++){
                        vendeurs[i]=results[i].getName();
                    }
                } catch (FIPAException e) {
                    e.printStackTrace();
                }
            }
        });
        parallelBehaviour.addSubBehaviour(new CyclicBehaviour() {
            private int counter=0;
            private List<ACLMessage> replies=new ArrayList<ACLMessage>();
            @Override
            public void action() {
               //filtrer les actes de communication
               MessageTemplate messageTemplate=
                       MessageTemplate.or(MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
                               MessageTemplate.or(MessageTemplate.MatchPerformative(ACLMessage.PROPOSE),
                                       MessageTemplate.or(MessageTemplate.MatchPerformative(ACLMessage.AGREE),
                                               MessageTemplate.MatchPerformative(ACLMessage.REFUSE)
                                       )));

                // pour que l'agent  attend la reception du message en utilise un event loop (boucle infini)
                ACLMessage aclMessage=receive(messageTemplate);
                if(aclMessage!=null){
                    switch (aclMessage.getPerformative()){

                        case ACLMessage.REQUEST:
                            String livre=aclMessage.getContent();
                            ACLMessage aclMessage2=new ACLMessage(ACLMessage.CFP);
                            aclMessage2.setContent(livre);
                            for(AID aid:vendeurs){
                                aclMessage2.addReceiver(aid);
                            }
                            // envoyer le message
                            send(aclMessage2);
                            break;
                        case ACLMessage.PROPOSE:
                            ++counter;
                            replies.add(aclMessage);
                            if(counter==vendeurs.length){
                                ACLMessage meilleurOffre=replies.get(0);
                                double min= Double.parseDouble(meilleurOffre.getContent());
                                for (ACLMessage offre:replies){
                                    double price=Double.parseDouble(offre.getContent());
                                    if(price<min){
                                        meilleurOffre=offre;
                                        min=price;
                                    }
                                }

                                ACLMessage aclMessageAccept=meilleurOffre.createReply();
                                aclMessage.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                                send(aclMessageAccept);

                            }
                            break;
                        case ACLMessage.AGREE:

                            ACLMessage aclMessage3=new ACLMessage(ACLMessage.CONFIRM);
                            aclMessage3.addReceiver(new AID("consumer",AID.ISLOCALNAME));
                            aclMessage3.setContent(aclMessage.getContent());
                            send(aclMessage3);

                            break;
                        case ACLMessage.REFUSE:
                            break;

                        default:
                            break;}

                    String livre=aclMessage.getContent();
                    gui.logMessage(aclMessage);
                    ACLMessage reply=aclMessage.createReply();
                    reply.setContent("ok pour "+aclMessage.getContent());
                    send(reply);
                    ACLMessage aclMessage1=new ACLMessage(ACLMessage.CFP);
                    aclMessage1.setContent(livre);
                    aclMessage1.addReceiver(new AID("VENDEUR",AID.ISLOCALNAME));
                    send(aclMessage1);
                 }else block();}});}
    @Override
    protected void onGuiEvent(GuiEvent guiEvent) {}}
