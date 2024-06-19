// tick_events.js
Blockly.Blocks['tick_events'] = {
    init: function () {
        this.appendDummyInput()
            .appendField('On Tick');
        this.appendStatementInput('DO')
            .setCheck(null);
        this.setColour(230);
        this.setTooltip('Triggers every tick');
        this.setHelpUrl('');
    }
};

Blockly.JavaScript['tick_events'] = function (block) {
    const statements_do = Blockly.JavaScript.statementToCode(block, 'DO');
    return `
        (function() {
            function onTick() {
                ${statements_do}
            }
            window.addEventListener('tick', onTick);
        })();
    `;
};
