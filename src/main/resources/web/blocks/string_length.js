// string_length.js

Blockly.Blocks['string_length'] = {
    init: function () {
        this.appendValueInput('VALUE')
            .setCheck('String')
            .appendField('length of');
        this.setOutput(true, 'Number');
        this.setColour(160);
        this.setTooltip('Returns number of characters in the provided text.');
        this.setHelpUrl('http://www.w3schools.com/jsref/jsref_length_string.asp');
    }
};

Blockly.JavaScript['string_length'] = function (block) {
    var value = Blockly.JavaScript.valueToCode(block, 'VALUE', Blockly.JavaScript.ORDER_FUNCTION_CALL) || '\'\'';
    var code = value + '.length';
    return [code, Blockly.JavaScript.ORDER_MEMBER];
};
