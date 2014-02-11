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
Ext.define('Ssp.model.Placement', {
    extend: 'Ssp.model.AbstractBase',
    fields: [{name:'score',type:'string'},
             {name:'status',type:'string'},
             {name:'name',type:'string'},
             {name:'outcome',type:'string'},
             {name:'testProviderLink',type:'string'},
             {name:'hasDetails',type:'boolean'},
             {name:'subTestName',type:'string'},
             {
            	 name: 'type',
            	 convert: function(value, record) {
            		 return record.get('name') + ' '+ record.get('subTestName');
            	 }
             },             
             {name: 'takenDate',type: 'date', dateFormat: 'c'}]
});