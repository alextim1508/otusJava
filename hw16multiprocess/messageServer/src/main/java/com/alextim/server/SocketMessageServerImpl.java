package com.alextim.server;

import com.alextim.messages.*;

public class SocketMessageServerImpl extends SocketMessageServer {

    LoadBalancer balancer = new LoadBalancer();

    @Override
    public Message handlerMessage(Message message, String id) {

        if(message instanceof RegistrationMessage) {
            RegistrationMessage registrationMessage = (RegistrationMessage)message;
            setClientId(id, IdClient.valueOf(registrationMessage.getWaitingAnswerId()));
            return new OkMessage(ID);
        }
        else if(message instanceof SaveUserMassage) {
            if(IdClient.isFrontendId(id)) {
                IdClient backendId = balancer.getBackendId();
                sendMessageToClientById(message, backendId.toString());
                balancer.requestBackendCountInc(backendId);
                return new OkMessage(ID);
            }
            else {
                return new UnknownMessage(ID);
            }
        }
        else if(message instanceof SavedUserMassage) {
            SavedUserMassage savedUserMassage = (SavedUserMassage)message;
            if(IdClient.isBackendId(id)) {
                sendMessageToClientById(new ShowUserMessage(savedUserMassage.getUser(), ID), savedUserMassage.getWaitingAnswerId());
                return new OkMessage(ID);
            }
            else {
                return new UnknownMessage(ID);
            }
        }
        else if(message instanceof ShownUserMessage) {
            return null;
        }
        else if(message instanceof GetAllUsersMessage) {
            if(IdClient.isFrontendId(id)) {
                IdClient backendId = balancer.getBackendId();
                sendMessageToClientById(message, backendId.toString());
                balancer.requestBackendCountInc(backendId);
                return new OkMessage(ID);
            }
            else {
                return new UnknownMessage(ID);
            }
        }
        else if(message instanceof UsersMessage) {
            UsersMessage usersMessage = (UsersMessage)message;
            if(IdClient.isBackendId(id)) {
                sendMessageToClientById(new ShowUsersMessage(usersMessage.getUsers(), ID), message.getWaitingAnswerId());
                return new OkMessage(ID);
            }
            else {
                return new UnknownMessage(ID);
            }
        }
        else if(message instanceof ErrorMessage) {
            ErrorMessage errorMessage = (ErrorMessage)message;
            if(IdClient.isBackendId(id)) {
                sendMessageToClientById(new ShowErrorMessage(errorMessage.getMessageException(), ID), ((ErrorMessage) message).getMessageException());
                return new OkMessage(ID);
            }
            else {
                return new UnknownMessage(ID);
            }
        }
        else if(message instanceof ShownErrorMessage) {
            return null;
        }
        else if(message instanceof ShownUsersMessage) {
            return null;
        }

        return new UnknownMessage(ID);
    }
}
