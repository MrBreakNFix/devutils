// chat_events.js

Blockly.Blocks['chat_events'] = {
    // This block triggers when a chat message is received, and provides the message
    init: function () {
        this.appendDummyInput()
            .appendField('On Chat Message');
        this.appendStatementInput('DO')
            .setCheck(null);
        this.setColour(230);
        this.setTooltip('Triggers when a chat message is received');
        this.setHelpUrl('');
    }
}

Blockly.JavaScript['chat_events'] = function (block) {
    const statements_do = Blockly.JavaScript.statementToCode(block, 'DO');
    return `
        (function() {
            function onChatEvent(message) {
                ${statements_do}
            }
            window.addEventListener('chat', onChatEvent);
        })();
    `;
};