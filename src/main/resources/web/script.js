let running = false;

function stopScript() {
    running = false;
    areEventsEnabled = false;
    document.getElementById('toggleButton').textContent = 'Run Code';
    console.log('stopScript');

    // Set the background image to stop.png after a short delay
    setTimeout(() => {
        document.getElementById('toggleButton').style.backgroundImage = 'url("/assets/run.png")';
    }, 1);
    clean();
}

async function startScript() {
    console.log('startScript');
    if (running) return; // Prevent starting if already running
    running = true;

    areEventsEnabled = true;
    const workspace = Blockly.getMainWorkspace();
    const code = translate(workspace);
    document.getElementById('codeOutput').textContent = code;

    try {
        eval(code);

        // Check if there are event blocks in the workspace
        const hasEventBlocks = hasEventBlocksInWorkspace(workspace);

        if (!hasEventBlocks) {
            // Wait until all tasks (including web requests) are complete
            await new Promise((resolve, reject) => {
                setTimeout(resolve, 0); // Ensures promise runs asynchronously
            });

            // If we reach this point and no event blocks, stop the script
            stopScript();
        } else {
            document.getElementById('toggleButton').textContent = 'Stop Code';
            document.getElementById('toggleButton').style.backgroundImage = 'url("/assets/stop.png")';
        }
    } catch (error) {
        console.error('Error while running script:', error);
        stopScript();
    }
}

function hasEventBlocksInWorkspace(workspace) {
    // Check if there are any event blocks in the workspace
    const blocks = workspace.getAllBlocks();
    for (let i = 0; i < blocks.length; i++) {
        // if the block contains "events"
        if (blocks[i].type.includes('events')) {
            console.log('Event blocks:', blocks[i].type);
            return true;
        }
    }
    return false;
}

document.addEventListener('DOMContentLoaded', (event) => {
    setupWebSocketListener(getPort());

    var workspace = Blockly.inject('blocklyDiv', {
        toolbox:  `
            <xml id="toolbox" style="display: none;">
                <category name="Logic" colour="#5C81A6">
                    <block type="controls_if"></block>
                    <block type="logic_compare"></block>
                    <block type="logic_operation"></block>
                    <block type="logic_negate"></block>
                    <block type="logic_boolean"></block>
                    <block type="logic_null"></block>
                    <block type="logic_ternary"></block>
                </category>
                <category name="Math" colour="#5CA65C">
                    <block type="math_number"></block>
                    <block type="math_arithmetic"></block>
                    <block type="string_length"></block>
                    <block type="math_modulo"></block>
                    <block type="math_random_int"></block>
                    <block type="math_random_float"></block>
                    <block type="math_round"></block>
                </category>
                <category name="Text" colour="#5CA68D">
                    <block type="text"></block>
                    <block type="text_join"></block>
                    <block type="string_value_of"></block>
                    <block type="text_print"></block>
                    <block type="text_prompt_ext"></block>
                    <block type="text_indexOf"></block>
                    <block type="text_charAt"></block>
                    <block type="text_getSubstring"></block>                    
                </category>
                <category name="Variables" colour="#A65C81">
                    <block type="variables_get"></block>
                    <block type="variables_set"></block>
                </category>
                <category name="Control" colour="#4C97D0">
                    <block type="controls_repeat_ext"></block>
                    <block type="controls_whileUntil"></block>
                    <block type="controls_for"></block>
                    <block type="controls_forEach"></block>
                    <block type="controls_flow_statements"></block>
                </category>
                <category name="Functions" colour="#FFD500">
                    <block type="procedures_defreturn"></block>
                    <block type="procedures_defnoreturn"></block>
                    <block type="procedures_callreturn"></block>
                    <block type="procedures_callnoreturn"></block>
                    <block type="procedures_ifreturn"></block>
                </category>
                <category name="Triggers / Events" colour="#FFAB19">
                    <block type="tick_events"></block>
                    <block type="screen_events"></block>
                    <block type="chat_events"></block>
                </category>
                <category name="Web" colour="#FF6666">
                    <block type="end_script"></block>
                </category>
                <category name="Player Information" colour="#FF6666">
                    <block type="player_pos"></block>
                </category>
                <category name="Player Actions" colour="#00FF00">
                    <block type="chatlog"></block>
                    <block type="chat"></block>
                </category>
            </xml>
        `
    });

    // Load the workspace state from local storage
    loadWorkspaceState(workspace);

    // Save the workspace state to local storage on change
    workspace.addChangeListener(() => {
        saveWorkspaceState(workspace);
    });

    document.getElementById('toggleButton').addEventListener('click', () => {
        if (running) {
            stopScript();
        } else {
            startScript();
        }
    });

    document.getElementById('clearButton').addEventListener('click', () => {
        workspace.clear();
        saveWorkspaceState(workspace);
    });

    document.getElementById('importExportButton').addEventListener('click', () => {
        exportCode(workspace);
        showPopup();
    });

    document.getElementById('closePopupButton').addEventListener('click', () => {
        if (document.getElementById('workspaceCode').value.trim() !== '') {
            importCode(workspace);
        }
        hidePopup();
    });

    document.getElementById('saveToFileButton').addEventListener('click', () => {
        saveToFile(document.getElementById('workspaceCode').value);
    });

    document.getElementById('loadFromFileButton').addEventListener('click', () => {
        document.getElementById('loadFromFileInput').click();
    });

    document.getElementById('loadFromFileInput').addEventListener('change', (event) => {
        const file = event.target.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = (e) => {
                document.getElementById('workspaceCode').value = e.target.result;
            };
            reader.readAsText(file);
        }
    });
});

function translate(workspace) {
    var code = Blockly.JavaScript.workspaceToCode(workspace);
    console.log(code);
    return code;
}

function getPort() {
    var url = '/api/wsport';
    var xhr = new XMLHttpRequest();
    xhr.open('POST', url, false);
    xhr.send();
    return xhr.responseText;
}

function exportCode(workspace) {
    const xml = Blockly.Xml.workspaceToDom(workspace);
    const xmlText = Blockly.Xml.domToText(xml);
    document.getElementById('workspaceCode').value = xmlText;
}

function importCode(workspace) {
    const xmlText = document.getElementById('workspaceCode').value;
    const xml = Blockly.Xml.textToDom(xmlText);
    Blockly.Xml.domToWorkspace(xml, workspace);
}

function saveToFile(content) {
    const blob = new Blob([content], { type: 'text/xml' });
    const a = document.createElement('a');
    a.href = URL.createObjectURL(blob);
    a.download = 'workspace.xml';
    a.click();
}

function showPopup() {
    // modify the popup css to render with flex for the popup id
    document.getElementById('popup').style.display = 'flex';
    console.log('showPopup');
}

function hidePopup() {
    document.getElementById('popup').style.display = 'none';
    console.log('hidePopup')
}

document.addEventListener('keydown', (event) => {
    if (event.key === 'Escape') {
        hidePopup();
    }
});

function saveWorkspaceState(workspace) {
    const xml = Blockly.Xml.workspaceToDom(workspace);
    const xmlText = Blockly.Xml.domToText(xml);
    localStorage.setItem('workspaceState', xmlText);
}

function loadWorkspaceState(workspace) {
    const xmlText = localStorage.getItem('workspaceState');
    if (xmlText) {
        const xml = Blockly.Xml.textToDom(xmlText);
        Blockly.Xml.domToWorkspace(xml, workspace);
    }
}
