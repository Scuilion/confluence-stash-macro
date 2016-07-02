(function ($) {
    $(document).ready(function () {

            function updateRepos(repoDropDown, projectName) {
                AJS.$.ajax({
                    async: true,
                    url: AJS.contextPath() + "/rest/stash_resource/1.0/stash/repositories?projectName=" + projectName,
                    dataType: 'json',
                    timeout: 10000,
                    error: function (xhr, textStatus, errorThrown) {
                        AJS.logError(errorThrown);
                        console.log("THERE WAS AN ERROR");
                    },
                    success: function (response) {
                        repoDropDown.empty();
                        repoDropDown.prop('disabled', false);
                        repoDropDown.append($('<option></option>').val("one").html("one"));
                    }
                });
            };
            function loadProjects() {
                AJS.$.ajax({
                    async: true,
                    url: AJS.contextPath() + "/rest/stash_resource/1.0/stash/projects",
                    dataType: 'json',
                    timeout: 10000,
                    error: function (xhr, textStatus, errorThrown) {
                        AJS.logError(errorThrown);
                        console.log("THERE WAS AN ERROR");
                    },
                    success: function (response) {
                        var jsOverrides = {
                            "fields": {
                                "enum": {
                                    "project": function (params, options) {
                                        var projects = [''];

                                        for (i = 0; i < response.values.length; i++) {
                                            projects.push(response.values[i].name);
                                        }
                                        params.enumValues = projects;
                                        var field = AJS.MacroBrowser.ParameterFields["enum"](params, options);
                                        return field;
                                    },
                                    "repo": function (params, options) {
                                        var paramDiv = AJS.$(Confluence.Templates.MacroBrowser.macroParameterSelect());
                                        var select = AJS.$("select", paramDiv);
                                        // var foo = $("select#macro-param-repo") //.prop('disabled', true);
                                        select.empty();
                                        select.append($("<option>None</option>").attr("value", ""));
                                        select.prop('disabled', true);

                                        $('select#macro-param-project').change(function () {
                                            updateRepos(select, this.value);
                                        });
                                        return new AJS.MacroBrowser.Field(paramDiv, select, options);
                                    }
                                }
                            }
                        };
                        AJS.MacroBrowser.setMacroJsOverride("stash-macro", jsOverrides);
                    }
                });
            };

            loadProjects();

        }
    );
})(AJS.$);