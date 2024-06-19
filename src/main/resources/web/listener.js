// listener.js
let socket;
let areEventsEnabled = false;

const eventListeners = {
    tick: [],
    death: [],
    screen: []
};

function setupWebSocketListener(port) {
    socket = new WebSocket('ws://localhost:' + port + '/ws');

    socket.onopen = function() {
        console.log('WebSocket connection established');
    };

    socket.onmessage = function(event) {
        const message = event.data;
        if (areEventsEnabled) {
            if (message === 'tick') {
                triggerEvent('tick');
            }

            if (message === 'death') {
                triggerEvent('death');
            }

            if (message.startsWith('screen:')) {
                const screenEvent = message.split(':')[1];
                triggerEvent('screen', screenEvent);
            }
        }

        if (message !== 'tick') {
            console.log('MSG:', message);
        }
    };

    socket.onclose = function() {
        console.log('WebSocket connection closed');
        if (areEventsEnabled) {
            setTimeout(() => {
                setupWebSocketListener(port);
            }, 1000);
        }
    };

    socket.onerror = function(error) {
        console.error('WebSocket error:', error);
    };
}

function triggerEvent(eventType, arg) {
    if (eventListeners[eventType]) {
        eventListeners[eventType].forEach(listener => listener(arg));
    }
}

function addEventListener(eventType, listener) {
    if (eventListeners[eventType]) {
        eventListeners[eventType].push(listener);
    }
}

function removeEventListener(eventType, listener) {
    if (eventListeners[eventType]) {
        eventListeners[eventType] = eventListeners[eventType].filter(l => l !== listener);
    }
}

function clean() {
    Object.keys(eventListeners).forEach(eventType => {
        eventListeners[eventType] = [];
    });
}

function exportWorkspace(workspace) {
    const xml = Blockly.Xml.workspaceToDom(workspace);
    const xmlText = Blockly.Xml.domToText(xml);
    return xmlText;
}

function importWorkspace(workspace, xmlText) {
    const xml = Blockly.Xml.textToDom(xmlText);
    Blockly.Xml.clearWorkspaceAndLoadFromXml(xml, workspace);
}

window.setupWebSocketListener = setupWebSocketListener;
window.addEventListener = addEventListener;
window.removeEventListener = removeEventListener;
window.clean = clean;
window.exportWorkspace = exportWorkspace;
window.importWorkspace = importWorkspace;
