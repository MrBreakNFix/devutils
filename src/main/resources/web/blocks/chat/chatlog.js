// chatlog.js


Blockly.Blocks['chatlog'] = {
    init: function () {
        this.appendValueInput('VALUE')
            .setCheck('String')
            .appendField('Chat Log:');
        this.setColour(160);
        this.setTooltip('Logs a string to your Minecraft chat');
        this.setPreviousStatement(true, null);
        this.setNextStatement(true, null);
    }
};

Blockly.JavaScript['chatlog'] = function (block) {
    const value = Blockly.JavaScript.valueToCode(block, 'VALUE', Blockly.JavaScript.ORDER_NONE) || '\'\'';
    // make value a string, if it is not one

    return `fetch('/api/chatlog', { method: 'POST', body: JSON.stringify({ message: ${value} }) });\n`;
};
