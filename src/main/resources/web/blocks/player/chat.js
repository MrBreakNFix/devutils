// chatlog.js


Blockly.Blocks['chat'] = {
    init: function () {
        this.appendValueInput('VALUE')
            .setCheck('String')
            .appendField('Chat:');
        this.setColour(160);
        this.setTooltip('Sends a message as you in minecraft chat');
        this.setPreviousStatement(true, null);
        this.setNextStatement(true, null);
    }
};

Blockly.JavaScript['chat'] = function (block) {
    const value = Blockly.JavaScript.valueToCode(block, 'VALUE', Blockly.JavaScript.ORDER_NONE) || '\'\'';
    return `fetch('/api/chat', { method: 'POST', body: JSON.stringify({ message: ${value} }) });\n`;
};
