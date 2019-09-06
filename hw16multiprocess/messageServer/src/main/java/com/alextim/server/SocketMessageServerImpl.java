package com.alextim.server;

import com.alextim.messages.*;

public class SocketMessageServerImpl extends SocketMessageServer {

    private LoadBalancer balancer = new LoadBalancer();

    @Override
    public Message handlerMessage(Message message, String id) {

        if(message instanceof RegistrationMessage) {
            RegistrationMessage registrationMessage = (RegistrationMessage)message;
            setClientId(id, IdClient.valueOf(registrationMessage.getId()));
            return new OkMessage();
        }
        else if(message instanceof SaveUserMassage) {
            if(IdClient.isFrontendId(id)) {
                IdClient idBackendClient = balancer.getBackendId();
                sendMessageToClientById(message, idBackendClient.toString());
                balancer.requestBackendCountInc(idBackendClient);
                return new OkMessage();
            }
            else {
                return new UnknownMessage();
            }
        }
        else if(message instanceof SavedUserMassage) {
            SavedUserMassage savedUserMassage = (SavedUserMassage)message;
            if(IdClient.isBackendId(id)) {
                IdClient idFrontendClient = balancer.getFrontendId();
                sendMessageToClientById(new ShowUserMessage(savedUserMassage.getUser()), idFrontendClient.toString());
                balancer.requestFrontendCountInc(idFrontendClient);
                return new OkMessage();
            }
            else {
                return new UnknownMessage();
            }
        }
        else if(message instanceof ShownUserMessage) {
            return null;
        }
        else if(message instanceof GetAllUsersMessage) {
            if(IdClient.isFrontendId(id)) {
                IdClient idBackendClient = balancer.getBackendId();
                sendMessageToClientById(message, idBackendClient.toString());
                balancer.requestBackendCountInc(idBackendClient);
                return new OkMessage();
            }
            else {
                return new UnknownMessage();
            }
        }
        else if(message instanceof UsersMessage) {
            UsersMessage usersMessage = (UsersMessage)message;
            if(IdClient.isBackendId(id)) {
                IdClient idFrontendClient = balancer.getFrontendId();
                sendMessageToClientById(new ShowUsersMessage(usersMessage.getUsers()), idFrontendClient.toString());
                balancer.requestFrontendCountInc(idFrontendClient);
                return new OkMessage();
            }
            else {
                return new UnknownMessage();
            }
        }
        else if(message instanceof ErrorMessage) {
            ErrorMessage errorMessage = (ErrorMessage)message;
            if(IdClient.isBackendId(id)) {
                IdClient idFrontendClient = balancer.getFrontendId();
                sendMessageToClientById(new ShowErrorMessage(errorMessage.getMessageException()), idFrontendClient.toString() );
                balancer.requestFrontendCountInc(idFrontendClient);
                return new OkMessage();
            }
            else {
                return new UnknownMessage();
            }
        }
        else if(message instanceof ShownErrorMessage) {
            return null;
        }
        else if(message instanceof ShownUsersMessage) {
            return null;
        }

        return new UnknownMessage();
    }
}
