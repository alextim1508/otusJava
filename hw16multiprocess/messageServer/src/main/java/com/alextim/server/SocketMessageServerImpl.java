package com.alextim.server;

import com.alextim.messages.*;

public class SocketMessageServerImpl extends SocketMessageServer {

    @Override
    public Message handlerMessage(Message message, String id) {
        if(message instanceof RegistrationMessage) {
            RegistrationMessage registrationMessage = (RegistrationMessage)message;
            setClientId(id, IdClient.valueOf(registrationMessage.getId()));
            return new OkMessage();
        }
        else if(message instanceof SaveUserMassage) {
            SaveUserMassage saveUserMassage = (SaveUserMassage)message;
            if(id.equals(IdClient.FRONTEND.toString())) {
                sendMessageToClientById(message, IdClient.BACKEND.toString());
                return new OkMessage();
            }
            else {
                return new UnknownMessage();
            }
        }
        else if(message instanceof SavedUserMassage) {
            SavedUserMassage savedUserMassage = (SavedUserMassage)message;
            if(id.equals(IdClient.BACKEND.toString())) {
                sendMessageToClientById(new ShowUserMessage(savedUserMassage.getUser()), IdClient.FRONTEND.toString());
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
            GetAllUsersMessage getAllUsersMessage = (GetAllUsersMessage)message;
            if(id.equals(IdClient.FRONTEND.toString())) {
                sendMessageToClientById(message, IdClient.BACKEND.toString());
                return new OkMessage();
            }
            else {
                return new UnknownMessage();
            }
        }
        else if(message instanceof UsersMessage) {
            UsersMessage usersMessage = (UsersMessage)message;
            if(id.equals(IdClient.BACKEND.toString())) {
                sendMessageToClientById(new ShowUsersMessage(usersMessage.getUsers()), IdClient.FRONTEND.toString());
                return new OkMessage();
            }
            else {
                return new UnknownMessage();
            }
        }
        else if(message instanceof ErrorMessage) {
            ErrorMessage errorMessage = (ErrorMessage)message;
            if(id.equals(IdClient.BACKEND.toString())) {
                sendMessageToClientById(new ShowErrorMessage(errorMessage.getMessageException()), IdClient.FRONTEND.toString() );
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
