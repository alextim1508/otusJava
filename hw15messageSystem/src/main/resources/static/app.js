let stompClient = null;

const setConnected = (connected) => {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
}

const connect = () => {
    stompClient = Stomp.over(new SockJS('/gs-guide-websocket'));
    stompClient.connect({}, (frame) => {
        setConnected(true);
        console.log('Connected: ' + frame);

        stompClient.subscribe('/topic/response', (user) => showUser(JSON.parse(user.body)));

        stompClient.subscribe('/topic/response2', (users) => showUsers(JSON.parse(users.body)));

        stompClient.subscribe('/topic/response3', (message) => showLog(JSON.parse(message.body).msg));
    });
}

const disconnect = () => {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

const sendUser = () => stompClient.send("/app/user", {}, JSON.stringify(
    {'name': $("#nameId").val(),
    'cryptPassword': $("#passwordId").val(),
    'gender': $("#genderId").val(),
    'address': $("#addressId").val(),
    'phones':  $("#phonesId").val()
    }))

const getUsers = () => stompClient.send("/app/users", {},  {})

const showUser = (user) =>
    $("#listUsers").append("<tr><td>" + user.id + "</td><td>" + user.name + "</td><td>" + user.gender + "</td><td>" + user.address + "</td><td>" + user.phones + "</td></tr>");

const showUsers = (users) => {
for(var i=0; i<users.length; i++)
    showUser(users[i]);
}

const showLog = (msg) =>  $("#log").append("<br>" + msg)


$(function () {
    $("form").on('submit', (event) => {
        event.preventDefault();
    });
    $("#connect").click(connect);
    $("#disconnect").click(disconnect);
    $("#addId").click(sendUser);
    $("#getId").click(getUsers);
});