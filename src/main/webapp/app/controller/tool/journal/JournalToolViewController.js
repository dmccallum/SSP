Ext.define('Ssp.controller.tool.journal.JournalToolViewController', {
    extend: 'Deft.mvc.ViewController',	
    mixins: [ 'Deft.mixin.Injectable'],
    inject: {
    	formUtils: 'formRendererUtils',
        store: 'confidentialityLevelsStore'
    },
    config: {
    	containerToLoadInto: 'tools',
    	formToDisplay: 'editjournal'
    },
    control: {
    	'addButton': {
			click: 'onAddClick'
		},
		
		'editButton': {
			click: 'onEditClick'
		},
		
		'viewHistoryButton': {
			click: 'onViewHistoryClick'
		}
	},
    constructor: function() {
    	this.store.load();
		return this.callParent(arguments);
    },
    
    onAddClick: function(button){
		var comp = this.formUtils.loadDisplay(this.getContainerToLoadInto(), this.getFormToDisplay(), true, {});
    },
    
    onEditClick: function(button){
		var comp = this.formUtils.loadDisplay(this.getContainerToLoadInto(), this.getFormToDisplay(), true, {});
    },

    onViewHistoryClick: function(button){
	 console.log('JournalToolViewController->onViewHistoryClick');
    }
});