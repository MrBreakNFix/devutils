// player_pos.js

// Define Blockly block for player position
Blockly.Blocks['player_pos'] = {
    init: function () {
        this.appendDummyInput()
            .appendField('Player:')
            .appendField(new Blockly.FieldDropdown([
                ['x', 'x'],
                ['y', 'y'],
                ['z', 'z']
            ]), 'POS');
        this.setOutput(true, 'String');
        this.setColour(160);
        this.setTooltip('Get the player\'s position');
    }
};

// Define Blockly JavaScript generator function for player position block
Blockly.JavaScript['player_pos'] = function(block) {
    var pos = block.getFieldValue('POS');
    // POST /api/playerinfo

};

