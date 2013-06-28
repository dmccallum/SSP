/*
 * Licensed to Jasig under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Jasig licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
Ext.define('Ssp.controller.tool.notes.NotesViewController', {
    extend: 'Deft.mvc.ViewController',
    mixins: [ 'Deft.mixin.Injectable' ],
    inject: {
    	apiProperties: 'apiProperties',
    	service: 'personNoteService',
        person: 'currentPerson',
        store: 'personNotesStore',
    },
	init: function() {
		var me=this;
		var schoolId = me.person.get('schoolId');

        me.store.removeAll();
		if(schoolId != ""){
	    	me.loadNotes(schoolId);
	    }
		
		return this.callParent(arguments);
    },
    
    loadNotes: function(schoolId){
		var me = this;
		if(schoolId != ""){
			me.service.getPersonNotes( schoolId, {
				success: me.getTranscriptSuccess,
				failure: me.getTranscriptFailure,
				scope: me			
			});
		}
	},
	
    getTranscriptSuccess: function( r, scope ){
    	var me=scope;

        var notes = [];
        var note = new Ssp.model.external.PersonNote(r);


        me.store.loadData(r);
		me.store.sort([
		    {
		        property : 'date',
		        direction: 'DESC'
		    },
		    {
		        property : 'author',
		        direction: 'ASC'
		    }]);
        me.getView().setLoading( false );
    },
    
    getTranscriptFailure: function( response, scope ){
    	var me=scope;
    	me.getView().setLoading( false );  	
    }
});