var keyPressed = {
    "w": false,
    "a": false,
    "s": false,
    "d": false,
    "space": false,
    "up": false,
    "down": false,
    "left": false,
    "right": false
};

function setState(key, value) {
    if (keyPressed[key] !== value) {
        keyPressed[key] = value;
        if (connected) {
            socket.emit(
              'inputs',
              JSON.stringify(keyPressed)
            );
        }
    }
}

function handleEvent(event, toSet) {
    setState(event.key, toSet);
}


document.addEventListener("keydown", function (event) {
    handleEvent(event, true)
});

document.addEventListener("keyup", function (event) {
    handleEvent(event, false)
});