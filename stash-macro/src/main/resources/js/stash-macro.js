(function($) {
    $(document).ready(function() {
        console.log("loading stash-macro.js")
        // var StashMacro = function() {};
        //
        // StashMacro.prototype.fields = {
        //     "string":{
        //         "title" : function(param, options){
        //             return new AJS.MacroBrowser.Field()
        //         }
        //     }
        // }
        // AJS.MacroBrowser.Macros["stash-macro"]
        function callRest() {
            AJS.$.ajax({
                async: true,
                url: AJS.contextPath() + "/rest/myrestresource/1.0/stash",
                //rest/myrestresource/1.0/stash myrestresource is defined in plugins.xml
                dataType: 'json',
                timeout: 10000,
                error: function (xhr, textStatus, errorThrown) {
                    console.log(errorThrown);
                    console.log("THERE WAS AN ERROR");
                },
                success: function (response) {
                    console.log(response);
                    var jsOverrides = {
                        "fields": {
                            "enum": {
                                "project": function (params, options) {
                                    var projects = [''];
                                    
                                    for ( i=0; i < response.values.length; i++) {
                                        projects.push(response.values[i].name);
                                    }
                                    params.enumValues = projects;
                                    var field = AJS.MacroBrowser.ParameterFields["enum"](params, options);
                                    return field;
                                }
                            }
                        }
                    };
                    console.log(jsOverrides);
                    AJS.MacroBrowser.setMacroJsOverride("stash-macro", jsOverrides);
                }
            });
        }

        var jsOverrides = {

            "fields": {
                "string": {
                    "name-override": function (params, options) {
                        var field = AJS.MacroBrowser.ParameterFields["string"](params, options);
                        field.setValue("another desiredValue");
                        return field;
                    }
                }
            }
        };
        callRest();
        // AJS.MacroBrowser.setMacroJsOverride("stash-macro", jsOverrides);
    });
})(AJS.$);