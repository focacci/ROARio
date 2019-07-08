// LOGIN CODE //////////////////////////////////////////////////////////////////////////////////////////////////////////

var username;


function send_username_to_server() {
  username = document.getElementById("username").value;
  socket.emit(
    'register',
    JSON.stringify(username)
  );
}


function clear_username() {
  document.getElementById("username").value = "";
}

/*
function ajaxPostRequest(path, data, callback) {
  var request = new XMLHttpRequest();
  request.onreadystatechange = function() {
    if (this.readyState === 4 && this.status === 200) {
      callback(this.response);
    }
  };
  request.open("POST", path);
  request.send(data);
}
*/


// GAME CODE ///////////////////////////////////////////////////////////////////////////////////////////////////////////

var socket = io.connect({transports: ['websocket']});
socket.on('gs', displayGame);
socket.on('register', readResponse);
var game = document.getElementById("game");
var context = game.getContext("2d");
context.globalCompositeOperation = 'source-over';


function readResponse(json_response) {
  var response = JSON.parse(json_response);
  console.log(response.toString());
  if (response) {
    socket.emit('addPlayer');
  } else {
    clear_username();
    document.getElementById("message").innerHTML = "Username taken";
  }
}


//const scaleFactor = 20
var connected = false;

function displayGame(json_game) {
  connected = true;
  console.log("Received:  " + json_game);
  var game = JSON.parse(json_game);
  var width = game["dimensions"]["width"];
  var height = game["dimensions"]["height"];

  var platforms = game["platforms"];

  makeGameGrid(width, height, platforms);

  var players = game["players"];
  var bullets = game["bullets"];

  for (var p = 0; p < players.length; p++) {
    placeCircle(
      players[p]["location"]["x"],
      players[p]["location"]["y"],
      10,
      p["username"] === socket.id ? '#0ec51f' : '#ce000d');
  }

  for (var b = 0; b < bullets.length; b ++) {
    placeCircle(
      bullets[b]["x"],
      bullets[b]["y"],
      3,
      '#000000');
  }


}


function placeCircle(x, y, size, color) {
  context.fillStyle = color;
  context.beginPath();
  context.arc(
    x, y, size, 0, 2*Math.PI
  );
  context.fill();
  context.strokeStyle = 'black';
  context.stroke();
}


function makeGameGrid(width, height, platforms) {
  context.clearRect(0, 0, width, height);
  context.strokeStyle = '#000000';

  game.setAttribute('width', width);
  game.setAttribute('height', height);

  // top border
  context.beginPath();
  context.moveTo(0, 0);
  context.lineTo(width, 0);
  context.stroke();

  // right border
  context.beginPath();
  context.moveTo(width, 0);
  context.lineTo(width, height);
  context.stroke();

  // bottom border
  context.beginPath();
  context.moveTo(width, height);
  context.lineTo(0, height);
  context.stroke();

  // left border
  context.beginPath();
  context.moveTo(0, height);
  context.lineTo(0, 0);
  context.stroke();

  // platforms    list {"start": {x: _, y: _}, "end": {x: _, y: _}}
  for (p = 0; p < platforms.length; p++) {
    context.beginPath();
    context.moveTo(platforms[p]["start"]["x"], platforms[p]["start"]["y"]);
    context.lineTo(platforms[p]["end"]["x"], platforms[p]["end"]["y"]);
    context.stroke();
  }
}
