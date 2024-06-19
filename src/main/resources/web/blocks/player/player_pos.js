// Define Blockly block for player position
Blockly.Blocks['player_pos'] = {
    init: function () {
        this.appendDummyInput()
            .appendField('Player Pos:')
            .appendField(new Blockly.FieldDropdown([
                ['X', 'x'],
                ['Y', 'y'],
                ['Z', 'z'],
                ['Block Pos: X', 'blockX'],
                ['Block Pos: Y', 'blockY'],
                ['Block Pos: Z', 'blockZ']
            ]), 'POS');
        this.setOutput(true, 'String');
        this.setColour(160);
        this.setTooltip('Get the player\'s position');
    }
};

Blockly.JavaScript['player_pos'] = function(block) {
    const pos = block.getFieldValue('POS');

    const code = `
    (function() {
        var xhr = new XMLHttpRequest();
        xhr.open('POST', '/api/playerinfo', false); // synchronous request
        xhr.setRequestHeader('Content-Type', 'application/json');
        xhr.send(JSON.stringify({ wants: '${pos}' }));
        if (xhr.status === 200) {
            return xhr.responseText;
        } else {
            throw new Error('Request failed: ' + xhr.status);
        }
    })()
    `;

    return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};
