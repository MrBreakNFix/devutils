// text_join.js

Blockly.Blocks['text_join'] = {
    // Allows you to join multiple strings, allows you to keep adding more strings as inputs
    init: function () {
        this.appendValueInput('STRING1')
            .setCheck('String')
            .appendField('Join');
        this.appendValueInput('STRING2')
            .setCheck('String');
        this.setOutput(true, 'String');
        this.setColour(160);
        this.setTooltip('Joins multiple strings together');
    }
};

Blockly.JavaScript['text_join'] = function (block) {
    var value1 = Blockly.JavaScript.valueToCode(block, 'STRING1', Blockly.JavaScript.ORDER_NONE) || '\'\'';
    var value2 = Blockly.JavaScript.valueToCode(block, 'STRING2', Blockly.JavaScript.ORDER_NONE) || '\'\'';
    return [`${value1} + ${value2}`, Blockly.JavaScript.ORDER_ATOMIC];
}