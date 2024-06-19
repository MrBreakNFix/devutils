// end_script.js
Blockly.Blocks['end_script'] = {
    // a block that attaches to another block
    init: function () {
        this.appendDummyInput()
            .appendField('End Script');
        this.setColour(230);
        this.setTooltip('Ends the script');
        this.setPreviousStatement(true, null); // Allows chaining
    }
};

Blockly.JavaScript['end_script'] = function (block) {
    return `
        (function() {
            stopScript();
        })();
    `;
};