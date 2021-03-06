/*
 * Licensed to Apereo under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Apereo licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License.  You may obtain a
 * copy of the License at the following location:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
Ext.define('Ssp.model.reference.ConfidentialityDisclosureAgreement', {
    extend: 'Ssp.model.reference.AbstractReference',
    mixins: [ 'Deft.mixin.Injectable' ],
    inject: {
    	apiProperties: 'apiProperties'
    },    
    fields: [{name:'text',type:'string'}],

	constructor: function(){
		Ext.apply(this.getProxy(), 
				{ 
			url: this.apiProperties.createUrl( this.apiProperties.getItemUrl('confidentialityDisclosureAgreement') )
			    }
		);
		return this.callParent(arguments);
	}, 	
	
	proxy: {
		type: 'rest',
		url: '',
		actionMethods: {
			create: "POST", 
			read: "GET", 
			update: "PUT",
			destroy: "DELETE"
		},
		reader: {
			type: 'json'
		},
	    writer: {
	        type: 'json',
	        successProperty: 'success'
	    }
	}
});