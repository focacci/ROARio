import eventlet
import json
import socket

from flask import Flask, send_from_directory, request, redirect, url_for
from flask_socketio import SocketIO
from threading import Thread


eventlet.monkey_patch()

app = Flask(__name__)
server = SocketIO(app)

users = []
sidToUsername = {}
usernameToSid = {}

activeGames = []


scala_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
scala_socket.connect(('localhost', 4242))

delimiter = "~"


def send_to_clients(data):
    server.emit('gs', data, broadcast=True)


def socket_connection_test():
    data = "Connected"
    server.emit('test', data, broadcast=True)


def listen_to_scala(the_socket):
    buffer = ""
    while True:
        buffer += the_socket.recv(1024).decode()
        while delimiter in buffer:
            message = buffer[:buffer.find(delimiter)]
            buffer = buffer[buffer.find(delimiter)+1:]
            # print("Received GameState:  " + message)
            send_to_clients(message)


def send_to_scala(data):
    scala_socket.sendall((json.dumps(data) + delimiter).encode())


Thread(target=listen_to_scala, args=(scala_socket,)).start()


@server.on('connect')
def connect():
    print(request.sid + " connected")


@server.on('disconnect')
def disconnect():
    print(request.sid + " disconnected")
    message = {"username": sidToUsername[request.sid], "action": "disconnected"}
    send_to_scala(message)


@server.on('inputs')
def key_state(json_inputs):     # inputs are w, a, s, d, space, up, down, left, right
    inputs = json.loads(json_inputs)
    message = {
        "username": sidToUsername[request.sid],
        "action": "inputs",
        "inputs": inputs
    }
    print(message)
    send_to_scala(message)


@server.on('register')
def register(json_username):
    username = json.loads(json_username)
    print(username)
    response = True
    if username not in usernameToSid.keys():
        sidToUsername[request.sid] = username
        usernameToSid[username] = request.sid
        server.emit('register',
                    data=json.dumps(response),
                    room=request.sid)
    else:
        response = False
        server.emit('register',
                    data=json.dumps(response),
                    room=request.sid)


@server.on('addPlayer')
def addUser():
    username = sidToUsername[request.sid]
    print("Adding:      " + username)
    message = {"username": username, "action": "connected"}
    send_to_scala(message)


@app.route('/')
def index():
    return send_from_directory("static_files", "index.html")


@app.route('/users', methods=['POST'])
def login():
    username = json.loads(request.data)
    if username not in users:
        users.append(username)
        return json.dumps({
            "status": "success",
            "username": username
        })
    else:
        return json.dumps({
            "status": "failed"
        })


@app.route('/game')
def game():
    print("Sending:     game.html")
    return send_from_directory("static_files", "game.html")


@app.route('/<path:filename>')
def static_file(filename):
    print("sending:     " + filename)
    return send_from_directory("static_files", filename)


port = 1234

print("listening on port %d" % port)
server.run(app, port=port)
