var stompClient = null;

$(document).ready(function () {
    console.log("Index page is ready");
    connect();

    $("#send").click(function () {
        sendMessage();
    });


    $("#send-private").click(function () {
        sendPrivateMessage();
    });

});

function connect() {
    var socket = new SockJS('/websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {

        console.log('Connected: ' + frame);

        stompClient.subscribe('/topic/messages/', function (message) {
            const newMessage = JSON.parse(message.body);

            showMessage(newMessage.text + " by");
        });

        stompClient.subscribe('/user/topic/private-messages', function (message) {
            const userMessage = JSON.parse(message.body);

            showMessage(userMessage);
        });

    });
}

function showMessage(message) {
    var url = "/img/" + message.user.avatarName;
    var img = document.createElement('img');
    img.setAttribute("src", url);
    img.setAttribute("alt", "Photo not found");
    img.setAttribute("width", "90");
    img.setAttribute('height', "78");

    var div = document.getElementById("messages")

    $(div).prepend("<nobr>" + " : " +  message.text + "</nobr>" + "<br>" +
        "<strong>" + "date of send :" + message.time +  "</strong>"
    );

    $(div).prepend(img);

    $(div).prepend("<br>" +
        "<nobr style='color: blue'>" + message.user.username + "</nobr>")
}

function sendMessage() {
    console.log("sending message");
    stompClient.send("/ws/message", {}, JSON.stringify(
        {'text': $("#message").val()}));
}

function sendPrivateMessage() {
    console.log("sending private message");
    stompClient.send("/ws/chat", {}, JSON.stringify({'text': $("#private-message").val()}));
}

$(document).ready(function () {
    $("#btn").click(
        function () {
            sendPrivateMessageTest();
            return false;
        }
    );
});

function sendPrivateMessageTest() {
    var text = document.forms["form"]["text"].value;
    var chatId = document.forms["form"]["chatId"].value;

    $.ajax({
        type: "POST",
        url: "/send-private-message",
        data: {
            text: text,
            chatId: chatId,
        },
        success: function () {
            if (xhr.readyState === 4) {
                console.log(xhr.status);
                console.log(xhr.responseText);
            }
        },
        dataType: "json"

    });

}

