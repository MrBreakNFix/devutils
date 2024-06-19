// string_value_of.js

Blockly.Blocks['string_value_of'] = {
    // Attaches to a block which accepts a string with and accepts a number, boolean, or variable
    init: function () {
        this.appendValueInput('VALUE')
            .appendField('Value Of');
        this.setOutput(true, 'String');
        this.setColour(160);
        this.setTooltip('Converts a number, boolean, or variable to a string');
    }
}

Blockly.JavaScript['string_value_of'] = function (block) {
    var value = Blockly.JavaScript.valueToCode(block, 'VALUE', Blockly.JavaScript.ORDER_NONE) || '\'\'';
    return [`String(${value})`, Blockly.JavaScript.ORDER_ATOMIC];
}