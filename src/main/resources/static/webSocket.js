var stompClient = null;

$(document).ready(function () {
    console.log("Index page is ready");
    connect();

    $("#send").click(function () {
        sendMessage();
    });

});

function connect() {
    var socket = new SockJS('/our-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/messages', function (message) {
            const newMessage = JSON.parse(message.body);
            showMessage(newMessage.content + ' time : ' + newMessage.time);
        });

    });
}

function showMessage(message) {
    $("#messages").append("<tr><td>" + message + "</td></tr>");
}

function sendMessage() {
    console.log("sending message");
    stompClient.send("/ws/message", {}, JSON.stringify(
        {'content': $("#message").val()}));
}