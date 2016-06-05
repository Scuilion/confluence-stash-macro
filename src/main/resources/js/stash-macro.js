// (function($) {
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
    var jsOverrides = {
        
        "fields" : {
            "string" : {
                "name-override" : function(params,options){
                    var field = AJS.MacroBrowser.ParameterFields["string"](params, options);
                    field.setValue("desiredValue");
                    return field;
                }
            }
        }
    };
    AJS.MacroBrowser.setMacroJsOverride("stash-macro", jsOverrides);
// })(AJS.$);