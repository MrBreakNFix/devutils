// screen_events.js
Blockly.Blocks['screen_events'] = {
    init: function () {
        this.appendDummyInput()
            .appendField('On Screen: ')
            .appendField(new Blockly.FieldDropdown([
                ['Before Init', 'BeforeInit'],
                ['After Init', 'AfterInit'],
                ['Close', 'Close']
            ]), 'EVENT');
        this.appendStatementInput('DO')
            .setCheck(null);
        this.setColour(230);
        this.setTooltip('Triggers on screen events');
        this.setHelpUrl('');
    }
}

Blockly.JavaScript['screen_events'] = function (block) {
    const event = block.getFieldValue('EVENT');
    const statements_do = Blockly.JavaScript.statementToCode(block, 'DO');
    return `
        (function() {
            function onScreenEvent(screenEvent) {
                if (screenEvent === '${event}') {
                    ${statements_do}
                }
            }
            window.addEventListener('screen', onScreenEvent);
        })();
    `;
};
